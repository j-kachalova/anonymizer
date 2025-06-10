
package com.kachalova.streamprocessing.core.strategy;

import com.kachalova.streamprocessing.persistence.entity.IdentifierMapping;
import com.kachalova.streamprocessing.persistence.repository.IdentifierMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdentifierReplacementStrategy implements AnonymizationStrategy {

    private final IdentifierMappingRepository repository;

    @Override
    public Mono<String> anonymize(String fieldName, String value, Map<String, Object> parameters) {
        return repository.findByFieldNameAndOriginalValue(fieldName, value)
                .map(IdentifierMapping::getIdentifier)
                .switchIfEmpty(repository.save(
                        IdentifierMapping.builder()
                                .fieldName(fieldName)
                                .originalValue(value)
                                .identifier(UUID.randomUUID().toString())
                                .build()
                ).map(IdentifierMapping::getIdentifier));
    }
}
