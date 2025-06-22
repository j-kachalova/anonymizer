package com.kachalova.streamprocessing.controller;

import com.kachalova.streamprocessing.dto.OriginalDataDto;
import com.kachalova.streamprocessing.service.DataReconstructionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reconstruct")
@RequiredArgsConstructor
public class ReconstructionController {

    private final DataReconstructionService reconstructionService;

    @GetMapping("/{id}")
    public Mono<OriginalDataDto> getOriginal(@PathVariable Long id) {
        return reconstructionService.reconstructById(id);
    }
}

