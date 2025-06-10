package com.kachalova.streamprocessing.core.strategy;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class SemanticChangeStrategy implements AnonymizationStrategy {

    @Override
    public Mono<String> anonymize(String fieldName, String value, Map<String, Object> parameters) {
        // Простейший пример — возвращаем маску (или просто звездочки)
        String maskPattern = (String) parameters.getOrDefault("maskPattern", "****");
        return Mono.just(maskPattern);
    }
}
