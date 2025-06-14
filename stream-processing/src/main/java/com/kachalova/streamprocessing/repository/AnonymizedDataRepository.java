
package com.kachalova.streamprocessing.repository;

import com.kachalova.streamprocessing.model.AnonymizedData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnonymizedDataRepository extends ReactiveCrudRepository<AnonymizedData, Long> {
}
