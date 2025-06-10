package com.kachalova.streamprocessing.controller;

import com.kachalova.streamprocessing.model.RuleSet;
import com.kachalova.streamprocessing.service.RuleSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rulesets")
@RequiredArgsConstructor
public class RuleSetController {

    private final RuleSetService ruleSetService;

    @PostMapping
    public Mono<RuleSet> createRuleSet(@RequestBody RuleSet ruleSet) {
        return ruleSetService.save(ruleSet);
    }

    @GetMapping("/{id}")
    public Mono<RuleSet> getRuleSet(@PathVariable UUID id) {
        return ruleSetService.findById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteRuleSet(@PathVariable UUID id) {
        return ruleSetService.deleteById(id);
    }

    @GetMapping
    public Flux<RuleSet> listAll() {
        return ruleSetService.findAll();
    }
}
