package com.kachalova.streamprocessing.service;

import com.kachalova.streamprocessing.model.RuleSet;
import com.kachalova.streamprocessing.model.dto.AnonymizedResultDto;
import com.kachalova.streamprocessing.model.dto.InputDataDto;
import com.kachalova.streamprocessing.repository.RuleSetRepository;
import com.kachalova.streamprocessing.strategy.AnonymizationStrategyRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnonymizationService {

    private final RuleSetRepository ruleSetRepository;
    private final AnonymizationStrategyRegistry strategyRegistry;

    public Mono<AnonymizedResultDto> anonymize(InputDataDto inputData, UUID rulesetId) {
        return ruleSetRepository.findById(rulesetId)
                .map(ruleSet -> applyRules(inputData, ruleSet))
                .map(result -> {
                    AnonymizedResultDto dto = new AnonymizedResultDto();
                    dto.setAnonymizedFields(result);
                    return dto;
                });
    }

    private Map<String, String> applyRules(InputDataDto inputData, RuleSet ruleSet) {
        // Разбор jsonDefinition → мапа правил (это можно делать с помощью ObjectMapper)

        // Здесь пока упрощённо — пока делаем пустой результат:
        Map<String, String> result = new HashMap<>();

        // TODO: здесь должен быть реальный вызов стратегий в зависимости от ruleset.jsonDefinition

        inputData.getFields().forEach((field, value) -> {
            // Пример: просто копируем, но будем заменять
            result.put(field, value); // В будущем будет result.put(field, strategy.anonymize(value, params));
        });

        return result;
    }
}
