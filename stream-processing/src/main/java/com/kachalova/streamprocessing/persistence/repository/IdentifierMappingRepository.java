
package com.kachalova.streamprocessing.persistence.repository;

import com.kachalova.streamprocessing.persistence.entity.IdentifierMapping;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IdentifierMappingRepository extends ReactiveCrudRepository<IdentifierMapping, Long> {
    Mono<IdentifierMapping> findByFieldNameAndOriginalValue(String fieldName, String originalValue);
}
