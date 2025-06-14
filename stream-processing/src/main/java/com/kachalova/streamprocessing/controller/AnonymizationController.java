
package com.kachalova.streamprocessing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kachalova.streamprocessing.model.AnonymizedData;
import com.kachalova.streamprocessing.model.OriginalData;
import com.kachalova.streamprocessing.model.RuleSet;
import com.kachalova.streamprocessing.model.FieldRule;
import com.kachalova.streamprocessing.repository.AnonymizedDataRepository;
import com.kachalova.streamprocessing.repository.OriginalDataRepository;
import com.kachalova.streamprocessing.repository.RuleSetRepository;
import com.kachalova.streamprocessing.repository.FieldRuleRepository;
import com.kachalova.streamprocessing.service.AnonymizationEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/anonymize")
public class AnonymizationController {

    @Autowired
    private RuleSetRepository ruleSetRepository;

    @Autowired
    private FieldRuleRepository fieldRuleRepository;

    @Autowired
    private AnonymizationEngine anonymizationEngine;

    @Autowired
    private AnonymizedDataRepository anonymizedDataRepository;

    @Autowired
    private OriginalDataRepository originalDataRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public Mono<Map<String, Object>> anonymize(@RequestParam Long ruleSetId, @RequestBody Map<String, Object> inputData) {
        Mono<RuleSet> ruleSetMono = ruleSetRepository.findById(ruleSetId);
        Flux<FieldRule> fieldRulesFlux = fieldRuleRepository.findByRuleSetId(ruleSetId);

        return ruleSetMono.zipWith(fieldRulesFlux.collectList())
                .flatMap(tuple -> {
                    RuleSet ruleSet = tuple.getT1();
                    List<FieldRule> fieldRules = tuple.getT2();

                    return anonymizationEngine.anonymize(inputData, fieldRules)
                            .flatMap(anonymizedData -> {
                                // Save original data
                                OriginalData originalData = new OriginalData();
                                originalData.setRuleSetId(ruleSetId);
                                try {
                                    originalData.setDataJson(objectMapper.writeValueAsString(inputData));
                                } catch (Exception e) {
                                    return Mono.error(new RuntimeException("Error serializing original data", e));
                                }

                                // Save anonymized data
                                AnonymizedData anonymized = new AnonymizedData();
                                anonymized.setRuleSetId(ruleSetId);
                                try {
                                    anonymized.setDataJson(objectMapper.writeValueAsString(anonymizedData));
                                } catch (Exception e) {
                                    return Mono.error(new RuntimeException("Error serializing anonymized data", e));
                                }

                                return originalDataRepository.save(originalData)
                                        .then(anonymizedDataRepository.save(anonymized))
                                        .thenReturn(anonymizedData);
                            });
                });
    }
}
