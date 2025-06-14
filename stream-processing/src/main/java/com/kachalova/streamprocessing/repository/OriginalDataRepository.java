
package com.kachalova.streamprocessing.repository;

import com.kachalova.streamprocessing.model.OriginalData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginalDataRepository extends ReactiveCrudRepository<OriginalData, Long> {
}
