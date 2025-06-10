
package com.kachalova.streamprocessing.core.strategy;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.Map;

@Component
public class NoChangeStrategy implements AnonymizationStrategy {

    @Override
    public Mono<String> anonymize(String fieldName, String value, Map<String, Object> parameters) {
        return Mono.just(value);
    }
}
