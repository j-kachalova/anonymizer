package com.kachalova.streamprocessing.controller;

import com.kachalova.streamprocessing.dto.AnonymizedDataDto;
import com.kachalova.streamprocessing.dto.OriginalDataDto;
import com.kachalova.streamprocessing.model.FieldRule;
import com.kachalova.streamprocessing.model.RuleSet;
import com.kachalova.streamprocessing.repository.FieldRuleRepository;
import com.kachalova.streamprocessing.repository.RuleSetRepository;
import com.kachalova.streamprocessing.service.AnonymizationEngine;
import com.kachalova.streamprocessing.service.DataStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/anonymize")
public class AnonymizationController {

    private RuleSetRepository ruleSetRepository;

    private FieldRuleRepository fieldRuleRepository;

    private AnonymizationEngine anonymizationEngine;

    private DataStorageService dataStorageService;

    @PostMapping
    public Mono<AnonymizedDataDto> anonymize(
            @RequestParam Long ruleSetId,
            @RequestBody OriginalDataDto originalDto
    ) {
        log.info("AnonymizationController originalDto: {}", originalDto);

        Mono<RuleSet> ruleSetMono = ruleSetRepository.findById(ruleSetId);
        Flux<FieldRule> fieldRulesFlux = fieldRuleRepository.findByRuleSetId(ruleSetId);

        return ruleSetMono.zipWith(fieldRulesFlux.collectList())
                .flatMap(tuple -> {
                    List<FieldRule> fieldRules = tuple.getT2();
                    return anonymizationEngine.anonymize(originalDto, fieldRules, ruleSetId)
                            .flatMap(anonymizedDto ->
                                    dataStorageService.saveAll(originalDto, anonymizedDto)
                                            .thenReturn(anonymizedDto)
                            );
                });
    }
}
