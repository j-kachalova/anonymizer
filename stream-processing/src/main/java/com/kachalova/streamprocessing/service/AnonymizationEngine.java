package com.kachalova.streamprocessing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kachalova.streamprocessing.dto.AnonymizedDataDto;
import com.kachalova.streamprocessing.dto.OriginalDataDto;
import com.kachalova.streamprocessing.mapper.AnonymizedDataMapper;
import com.kachalova.streamprocessing.entity.FieldRule;
import com.kachalova.streamprocessing.service.strategy.AnonymizationStrategy;
import com.kachalova.streamprocessing.service.strategy.StrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnonymizationEngine {

    @Autowired
    private StrategyFactory strategyFactory;

    @Autowired
    private AnonymizedDataMapper anonymizedDataMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<AnonymizedDataDto> anonymize(OriginalDataDto inputDto, List<FieldRule> fieldRules, Long ruleSetId) {
        Map<String, Object> inputData = toMap(inputDto);

        // 1. Вызываем DecompositionStrategy один раз, если есть специальная запись "__full__"
        Optional<FieldRule> decompositionRule = fieldRules.stream()
                .filter(rule -> "__full__".equals(rule.getFieldName()) &&
                        "decomposition".equals(rule.getStrategy()))
                .findFirst();

        Mono<String> decompositionMono = decompositionRule
                .map(rule -> {
                    try {
                        String json = Optional.ofNullable(rule.getParamsJson()).orElse("{}");
                        Map<String, Object> params = objectMapper.readValue(json, Map.class);
                        params.put("rule_set_id", ruleSetId);
                        AnonymizationStrategy strategy = strategyFactory.getStrategy("decomposition");
                        return strategy.anonymize(inputDto, params);
                    } catch (Exception e) {
                        return Mono.<String>error(new RuntimeException("Error in decomposition strategy", e));
                    }
                })
                .orElseGet(() -> Mono.<String>empty());


        // 2. Группируем остальные правила по полям (кроме "__full__")
        Map<String, List<FieldRule>> rulesByField = fieldRules.stream()
                .filter(rule -> !"__full__".equals(rule.getFieldName()))
                .collect(Collectors.groupingBy(
                        FieldRule::getFieldName,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        Map<String, Mono<String>> fieldMonos = new LinkedHashMap<>();

        for (Map.Entry<String, List<FieldRule>> entry : rulesByField.entrySet()) {
            String fieldName = entry.getKey();
            Object originalValue = inputData.get(fieldName);
            if (originalValue == null) continue;

            Mono<String> resultMono = Mono.just(String.valueOf(originalValue));

            for (FieldRule rule : entry.getValue().stream()
                    .sorted(Comparator.comparingInt(r -> Optional.ofNullable(r.getOrderIndex()).orElse(0)))
                    .toList()) {

                resultMono = resultMono.flatMap(currentValue -> {
                    try {
                        String json = Optional.ofNullable(rule.getParamsJson()).orElse("{}");
                        Map<String, Object> params = objectMapper.readValue(json, Map.class);
                        params.put("field_name", fieldName);
                        if (inputData.containsKey("gender")) {
                            params.put("gender", inputData.get("gender"));
                        }
                        AnonymizationStrategy strategy = strategyFactory.getStrategy(rule.getStrategy());
                        Object strategyInput = strategy.requiresFullDto() ? inputDto : currentValue;
                        return strategy.anonymize(strategyInput, params);
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error in strategy: " + rule.getStrategy(), e));
                    }
                });
            }

            fieldMonos.put(fieldName, resultMono);
        }

        List<String> processedKeys = new ArrayList<>(fieldMonos.keySet());

        // 3. Сначала вызываем декомпозицию (если есть), потом обработку остальных полей
        return decompositionMono.then(
                Mono.zip(fieldMonos.values(), results -> {
                    Map<String, Object> result = new LinkedHashMap<>();
                    int i = 0;
                    for (String key : processedKeys) {
                        result.put(key, results[i++]);
                    }

                    inputData.forEach((key, value) -> {
                        if (!result.containsKey(key)) {
                            result.put(key, value);
                        }
                    });

                    return fromMapToDto(result, ruleSetId);
                })
        );
    }

    private Map<String, Object> toMap(OriginalDataDto dto) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("last_name", dto.getLastName());
        map.put("first_name", dto.getFirstName());
        map.put("patronymic", dto.getPatronymic());
        map.put("gender", dto.getGender());
        map.put("phone_number", dto.getPhoneNumber());
        map.put("email", dto.getEmail());
        return map;
    }

    private AnonymizedDataDto fromMapToDto(Map<String, Object> map, Long ruleSetId) {
        return AnonymizedDataDto.builder()
                .ruleSetId(ruleSetId)
                .lastName((String) map.get("last_name"))
                .firstName((String) map.get("first_name"))
                .patronymic((String) map.get("patronymic"))
                .gender((String) map.get("gender"))
                .phoneNumber((String) map.get("phone_number"))
                .email((String) map.get("email"))
                .build();
    }
}
