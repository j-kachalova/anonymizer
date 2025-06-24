
package com.kachalova.streamprocessing.repository;

import com.kachalova.streamprocessing.entity.IdentifierMapping;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IdentifierMappingRepository extends ReactiveCrudRepository<IdentifierMapping, Long> {
    Mono<IdentifierMapping> findByOriginalValueHash(String originalValueHash);
}
