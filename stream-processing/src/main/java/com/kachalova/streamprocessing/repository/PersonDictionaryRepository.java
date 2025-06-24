package com.kachalova.streamprocessing.repository;

import com.kachalova.streamprocessing.entity.PersonDictionary;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PersonDictionaryRepository extends ReactiveCrudRepository<PersonDictionary, Long> {
    Flux<PersonDictionary> findAll(); // или добавить фильтрацию по полу, если нужно
    Flux<PersonDictionary> findByGender(String gender);
}

