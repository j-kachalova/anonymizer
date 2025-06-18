package com.kachalova.streamprocessing.controller;

import com.kachalova.streamprocessing.dto.AnonymizedDataDto;
import com.kachalova.streamprocessing.dto.OriginalDataDto;
import com.kachalova.streamprocessing.mapper.AnonymizedDataMapper;
import com.kachalova.streamprocessing.mapper.OriginalDataMapper;
import com.kachalova.streamprocessing.model.FieldRule;
import com.kachalova.streamprocessing.model.RuleSet;
import com.kachalova.streamprocessing.repository.AnonymizedDataRepository;
import com.kachalova.streamprocessing.repository.FieldRuleRepository;
import com.kachalova.streamprocessing.repository.OriginalDataRepository;
import com.kachalova.streamprocessing.repository.RuleSetRepository;
import com.kachalova.streamprocessing.service.AnonymizationEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
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

    @Autowired
    private OriginalDataMapper originalDataMapper;

    @Autowired
    private AnonymizedDataMapper anonymizedDataMapper;

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

                    // Сохраняем оригинальные данные
                    return originalDataRepository.save(
                            originalDataMapper.toEntity(originalDto)
                    ).then(
                            anonymizationEngine.anonymize(originalDto, fieldRules, ruleSetId)
                    ).flatMap(anonymizedDto ->
                            anonymizedDataRepository.save(
                                    anonymizedDataMapper.toEntity(anonymizedDto)
                            ).thenReturn(anonymizedDto)
                    );
                });
    }
}
