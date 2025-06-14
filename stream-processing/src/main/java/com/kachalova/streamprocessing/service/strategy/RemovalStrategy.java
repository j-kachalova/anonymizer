
package com.kachalova.streamprocessing.service.strategy;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class RemovalStrategy implements AnonymizationStrategy {
    @Override
    public Mono<String> anonymize(Object input, Map<String, Object> params) {
        return Mono.just(""); // Remove the value
    }
}
