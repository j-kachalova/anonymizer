package com.kachalova.streamprocessing.service.strategy;

import com.kachalova.streamprocessing.entity.PersonDictionary;
import com.kachalova.streamprocessing.repository.PersonDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

@Service
public class DictionaryReplacementStrategy implements AnonymizationStrategy {

    @Autowired
    private PersonDictionaryRepository personDictionaryRepository;

    private final Random random = new SecureRandom();

    @Override
    public Mono<String> anonymize(Object input, Map<String, Object> params) {
        String field = (String) params.get("field_name");
        String gender = (String) params.get("gender"); // не обязателен

        return (gender != null ? personDictionaryRepository.findByGender(gender)
                : personDictionaryRepository.findAll())
                .collectList()
                .flatMap(list -> {
                    if (list.isEmpty()) return Mono.just("UNKNOWN");

                    PersonDictionary entry = list.get(random.nextInt(list.size()));

                    return switch (field) {
                        case "last_name" -> Mono.just(entry.getLastName());
                        case "first_name" -> Mono.just(entry.getFirstName());
                        case "patronymic" -> Mono.just(entry.getPatronymic());
                        default -> Mono.just("UNKNOWN_FIELD");
                    };
                });
    }
}
