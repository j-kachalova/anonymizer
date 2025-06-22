package com.kachalova.streamprocessing.repository;

import com.kachalova.streamprocessing.model.DecomposedContact;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecomposedContactRepository extends ReactiveCrudRepository<DecomposedContact, Long> {
}
