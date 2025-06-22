package com.kachalova.streamprocessing.repository;

import com.kachalova.streamprocessing.model.DecomposedMeta;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecomposedMetaRepository extends ReactiveCrudRepository<DecomposedMeta, Long> {
}
