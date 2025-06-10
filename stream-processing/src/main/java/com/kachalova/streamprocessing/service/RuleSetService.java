package com.kachalova.streamprocessing.service;

import com.kachalova.streamprocessing.model.RuleSet;
import com.kachalova.streamprocessing.repository.RuleSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RuleSetService {

    private final RuleSetRepository ruleSetRepository;

    public Mono<RuleSet> save(RuleSet ruleSet) {
        if (ruleSet.getId() == null) {
            ruleSet.setId(UUID.randomUUID());
        }
        return ruleSetRepository.save(ruleSet);
    }

    public Mono<RuleSet> findById(UUID id) {
        return ruleSetRepository.findById(id);
    }

    public Flux<RuleSet> findAll() {
        return ruleSetRepository.findAll();
    }

    public Mono<Void> deleteById(UUID id) {
        return ruleSetRepository.deleteById(id);
    }
}
