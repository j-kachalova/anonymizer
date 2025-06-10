package com.kachalova.streamprocessing.controller;

import com.kachalova.streamprocessing.model.dto.AnonymizedResultDto;
import com.kachalova.streamprocessing.model.dto.InputDataDto;
import com.kachalova.streamprocessing.service.AnonymizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/anonymize")
@RequiredArgsConstructor
public class AnonymizationController {

    private final AnonymizationService anonymizationService;

    @PostMapping
    public Mono<AnonymizedResultDto> anonymize(
            @RequestParam UUID rulesetId,
            @RequestBody InputDataDto inputData
    ) {
        return anonymizationService.anonymize(inputData, rulesetId);
    }
}
