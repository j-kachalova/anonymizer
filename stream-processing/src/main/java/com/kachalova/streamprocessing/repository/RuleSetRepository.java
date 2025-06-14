
package com.kachalova.streamprocessing.repository;

import com.kachalova.streamprocessing.model.RuleSet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleSetRepository extends ReactiveCrudRepository<RuleSet, Long> {
}
