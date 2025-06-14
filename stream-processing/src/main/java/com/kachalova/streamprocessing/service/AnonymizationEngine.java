
package com.kachalova.streamprocessing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kachalova.streamprocessing.model.FieldRule;
import com.kachalova.streamprocessing.service.strategy.AnonymizationStrategy;
import com.kachalova.streamprocessing.service.strategy.StrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnonymizationEngine {

    @Autowired
    private StrategyFactory strategyFactory;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<Map<String, Object>> anonymize(Map<String, Object> inputData, List<FieldRule> fieldRules) {

        Map<String, Mono<String>> fieldMonos = new HashMap<>();

        for (FieldRule rule : fieldRules) {
            String fieldName = rule.getFieldName();
            String strategyName = rule.getStrategy();
            String paramsJson = rule.getParamsJson();

            try {
                Map<String, Object> params = objectMapper.readValue(paramsJson, Map.class);
                params.put("field_name", fieldName);

                AnonymizationStrategy strategy = strategyFactory.getStrategy(strategyName);
                Object fieldValue = inputData.get(fieldName);

                if (fieldValue != null) {
                    Mono<String> anonymizedMono = strategy.anonymize(fieldValue, params);
                    fieldMonos.put(fieldName, anonymizedMono);
                }
            } catch (Exception e) {
                return Mono.error(new RuntimeException("Error processing field: " + fieldName, e));
            }
        }

        return Flux.fromIterable(fieldMonos.entrySet())
                .flatMap(entry -> entry.getValue().map(val -> Map.entry(entry.getKey(), val)))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }
}
