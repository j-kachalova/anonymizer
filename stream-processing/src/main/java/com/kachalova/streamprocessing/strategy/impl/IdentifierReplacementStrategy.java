package com.kachalova.streamprocessing.strategy.impl;

import com.kachalova.streamprocessing.repository.IdentifierMappingRepository;
import com.kachalova.streamprocessing.strategy.AnonymizationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class IdentifierReplacementStrategy implements AnonymizationStrategy {

    private final IdentifierMappingRepository mappingRepository;

    @Override
    public String anonymize(String originalValue, Map<String, Object> params) {
        return mappingRepository.getOrCreateIdentifier(originalValue);
    }
}
