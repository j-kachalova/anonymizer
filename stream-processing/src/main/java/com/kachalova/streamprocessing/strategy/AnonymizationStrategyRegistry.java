package com.kachalova.streamprocessing.strategy;

import com.kachalova.streamprocessing.strategy.impl.IdentifierReplacementStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AnonymizationStrategyRegistry {

    private final Map<String, AnonymizationStrategy> strategies = new ConcurrentHashMap<>();

    // Пример инициализации (можно сделать динамической)
    public AnonymizationStrategyRegistry(IdentifierReplacementStrategy identifierReplacementStrategy) {
        strategies.put("identifier_replacement", identifierReplacementStrategy);
        // добавим потом другие стратегии
    }

    public AnonymizationStrategy getStrategy(String methodName) {
        return strategies.get(methodName);
    }
}
