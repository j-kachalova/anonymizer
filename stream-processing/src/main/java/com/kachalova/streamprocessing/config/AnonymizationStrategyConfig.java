
package com.kachalova.streamprocessing.config;

import com.kachalova.streamprocessing.api.dto.AnonymizationMethod;
import com.kachalova.streamprocessing.core.strategy.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class AnonymizationStrategyConfig {

    @Bean
    public Map<AnonymizationMethod, AnonymizationStrategy> strategyMap(
            IdentifierReplacementStrategy identifierReplacementStrategy,
            SemanticChangeStrategy semanticChangeStrategy,
            NoChangeStrategy noChangeStrategy) {

        return Map.of(
                AnonymizationMethod.IDENTIFIER, identifierReplacementStrategy,
                AnonymizationMethod.SEMANTIC_CHANGE, semanticChangeStrategy,
                AnonymizationMethod.NO_CHANGE, noChangeStrategy
        );
    }

}
