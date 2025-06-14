
package com.kachalova.streamprocessing.service.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class StrategyFactory {

    @Autowired
    private List<AnonymizationStrategy> strategies;

    private Map<String, AnonymizationStrategy> strategyMap;

    @PostConstruct
    public void init() {
        strategyMap = new HashMap<>();
        for (AnonymizationStrategy strategy : strategies) {
            strategyMap.put(strategy.getClass().getSimpleName().replace("Strategy", "").toUpperCase(), strategy);
        }
    }

    public AnonymizationStrategy getStrategy(String strategyName) {
        return Optional.ofNullable(strategyMap.get(strategyName.toUpperCase()))
                .orElseThrow(() -> new IllegalArgumentException("Unknown strategy: " + strategyName));
    }
}
