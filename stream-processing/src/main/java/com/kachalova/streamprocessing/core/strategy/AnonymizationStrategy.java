
package com.kachalova.streamprocessing.core.strategy;

import reactor.core.publisher.Mono;
import java.util.Map;

public interface AnonymizationStrategy {
    Mono<String> anonymize(String fieldName, String value, Map<String, Object> parameters);
}
