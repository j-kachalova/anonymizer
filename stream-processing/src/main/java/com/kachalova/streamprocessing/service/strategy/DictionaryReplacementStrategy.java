
package com.kachalova.streamprocessing.service.strategy;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class DictionaryReplacementStrategy implements AnonymizationStrategy {
    private final Random random = new Random();

    @Override
    public Mono<String> anonymize(Object input, Map<String, Object> params) {
        List<String> dictionary = (List<String>) params.get("dictionary");
        if (dictionary == null || dictionary.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Dictionary is required"));
        }
        int index = random.nextInt(dictionary.size());
        return Mono.just(dictionary.get(index));
    }
}
