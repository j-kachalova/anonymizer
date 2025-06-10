package com.kachalova.streamprocessing.repository;

import com.kachalova.streamprocessing.model.RuleSet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RuleSetRepository extends ReactiveCrudRepository<RuleSet, UUID> {
}
