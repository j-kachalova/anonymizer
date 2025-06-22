
package com.kachalova.streamprocessing.service.strategy;

import reactor.core.publisher.Mono;
import java.util.Map;

public interface AnonymizationStrategy {
    Mono<String> anonymize(Object input, Map<String, Object> params);

    default boolean requiresFullDto() {
        return false;
    }
}
