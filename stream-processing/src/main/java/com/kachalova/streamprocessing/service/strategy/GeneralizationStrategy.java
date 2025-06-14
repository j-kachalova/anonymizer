
package com.kachalova.streamprocessing.service.strategy;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class GeneralizationStrategy implements AnonymizationStrategy {
    @Override
    public Mono<String> anonymize(Object input, Map<String, Object> params) {
        if (input == null) return Mono.just("");
        String strValue = input.toString();
        int visibleLength = (int) params.getOrDefault("visibleLength", 3);

        String result = strValue.length() <= visibleLength
                ? strValue
                : strValue.substring(0, visibleLength) + "...";

        return Mono.just(result);
    }
}
