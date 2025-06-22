package com.kachalova.streamprocessing.repository;

import com.kachalova.streamprocessing.model.DecomposedIdentity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecomposedIdentityRepository extends ReactiveCrudRepository<DecomposedIdentity, Long> {

}
