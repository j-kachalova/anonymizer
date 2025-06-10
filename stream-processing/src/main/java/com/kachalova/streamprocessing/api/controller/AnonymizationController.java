
package com.kachalova.streamprocessing.api.controller;

import com.kachalova.streamprocessing.api.dto.AnonymizeRequest;
import com.kachalova.streamprocessing.core.engine.AnonymizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/anonymize")
@RequiredArgsConstructor
public class AnonymizationController {

    private final AnonymizationService anonymizationService;

    @PostMapping
    public Mono<Map<String, String>> anonymize(@RequestBody AnonymizeRequest request) {
        return anonymizationService.anonymize(request);
    }
}
