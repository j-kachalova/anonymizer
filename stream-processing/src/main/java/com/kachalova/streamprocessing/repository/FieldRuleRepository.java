
package com.kachalova.streamprocessing.repository;

import com.kachalova.streamprocessing.entity.FieldRule;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FieldRuleRepository extends ReactiveCrudRepository<FieldRule, Long> {
    Flux<FieldRule> findByRuleSetId(Long ruleSetId);
}
