package com.kachalova.streamprocessing.controller;

import com.kachalova.streamprocessing.dto.RuleSetRequestDto;
import com.kachalova.streamprocessing.dto.FieldRuleDto;
import com.kachalova.streamprocessing.model.RuleSet;
import com.kachalova.streamprocessing.model.FieldRule;
import com.kachalova.streamprocessing.repository.RuleSetRepository;
import com.kachalova.streamprocessing.repository.FieldRuleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rules")
public class RuleSetController {
    @Autowired
    private RuleSetRepository ruleSetRepository;
    @Autowired
    private FieldRuleRepository fieldRuleRepository;

    @PostMapping
    public Mono<RuleSet> createRuleSet(@RequestBody RuleSetRequestDto request) {
        RuleSet ruleSet = new RuleSet();
        ruleSet.setName(request.getName());
        ruleSet.setDescription(request.getDescription());

        return ruleSetRepository.save(ruleSet)
                .flatMap(savedRuleSet -> {
                    List<FieldRule> fieldRules = request.getFieldRules().stream()
                            .map(dto -> {
                                FieldRule fieldRule = new FieldRule();
                                fieldRule.setRuleSetId(savedRuleSet.getId());
                                fieldRule.setFieldName(dto.getFieldName());
                                fieldRule.setStrategy(dto.getStrategy());
                                fieldRule.setParamsJson(dto.getParamsJson());
                                return fieldRule;
                            })
                            .collect(Collectors.toList());

                    return fieldRuleRepository.saveAll(fieldRules)
                            .then(Mono.just(savedRuleSet));
                });
    }

    @GetMapping("/{id}")
    public Mono<RuleSet> getRuleSet(@PathVariable Long id) {
        return ruleSetRepository.findById(id);
    }
}
