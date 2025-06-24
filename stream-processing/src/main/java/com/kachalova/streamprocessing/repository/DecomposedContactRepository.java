package com.kachalova.streamprocessing.repository;

import com.kachalova.streamprocessing.entity.DecomposedContact;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecomposedContactRepository extends ReactiveCrudRepository<DecomposedContact, Long> {
}
