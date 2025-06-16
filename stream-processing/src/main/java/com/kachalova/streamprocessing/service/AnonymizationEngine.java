package com.kachalova.streamprocessing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kachalova.streamprocessing.model.FieldRule;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<Map<String, Object>> anonymize(Map<String, Object> inputData, List<FieldRule> fieldRules) {
        // Группировка по полю с сохранением порядка
        Map<String, List<FieldRule>> rulesByField = fieldRules.stream()
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
                        return strategy.anonymize(currentValue, params);
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error in strategy: " + rule.getStrategy(), e));
                    }
                });
            }

            fieldMonos.put(fieldName, resultMono);
        }

        List<String> orderedKeys = new ArrayList<>(fieldMonos.keySet());
        return Mono.zip(fieldMonos.values(), results -> {
            Map<String, Object> finalResult = new LinkedHashMap<>();
            for (int i = 0; i < orderedKeys.size(); i++) {
                finalResult.put(orderedKeys.get(i), results[i]);
            }
            return finalResult;
        });
    }
}
