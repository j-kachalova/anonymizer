
package com.kachalova.streamprocessing.core.engine;

import com.kachalova.streamprocessing.api.dto.*;
import com.kachalova.streamprocessing.core.strategy.AnonymizationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnonymizationService {

    private final Map<AnonymizationMethod, AnonymizationStrategy> strategyMap;

    public Mono<RuleSet> loadRuleSet(String ruleSetId) {
        return Mono.just(
                RuleSet.builder()
                        .id(ruleSetId)
                        .description("Example RuleSet")
                        .rules(Map.of(
                                "fullName", FieldRule.builder()
                                        .method(AnonymizationMethod.IDENTIFIER)
                                        .build(),
                                "phoneNumber", FieldRule.builder()
                                        .method(AnonymizationMethod.SEMANTIC_CHANGE)
                                        .parameters(Map.of("maskPattern", "+7(***)***-**-**"))
                                        .build()
                        ))
                        .build()
        );
    }

    public Mono<Map<String, String>> anonymize(AnonymizeRequest request) {
        return loadRuleSet(request.getRuleSetId())
                .flatMap(ruleSet -> {
                    Map<String, Mono<String>> resultMap = new HashMap<>();
                    request.getData().forEach((field, value) -> {
                        FieldRule rule = ruleSet.getRules().getOrDefault(field,
                                FieldRule.builder().method(AnonymizationMethod.NO_CHANGE).build());
                        AnonymizationStrategy strategy = strategyMap.get(rule.getMethod());
                        Mono<String> anonymized = strategy.anonymize(field, value, rule.getParameters());
                        resultMap.put(field, anonymized);
                    });
                    return Flux.fromIterable(resultMap.entrySet())
                            .flatMap(entry -> entry.getValue().map(v -> Map.entry(entry.getKey(), v)))
                            .collectMap(Map.Entry::getKey, Map.Entry::getValue);
                });
    }
}
