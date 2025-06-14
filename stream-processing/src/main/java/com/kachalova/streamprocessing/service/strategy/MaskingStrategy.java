
package com.kachalova.streamprocessing.service.strategy;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class MaskingStrategy implements AnonymizationStrategy {
    @Override
    public Mono<String> anonymize(Object input, Map<String, Object> params) {
        if (input == null) return Mono.just("");
        String strValue = input.toString();
        String maskChar = (String) params.getOrDefault("maskChar", "*");
        int visibleLength = (int) params.getOrDefault("visibleLength", 2);

        int maskLength = Math.max(0, strValue.length() - visibleLength);
        String masked = maskChar.repeat(maskLength) + strValue.substring(maskLength);

        return Mono.just(masked);
    }
}
