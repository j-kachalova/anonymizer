package com.kachalova.streamprocessing.controller;

import com.kachalova.streamprocessing.service.DataDeletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/decompose")
@RequiredArgsConstructor
public class DecompositionController {

    private final DataDeletionService deletionService;


    @DeleteMapping("/batch")
    public Mono<ResponseEntity<Object>> deleteBatch(@RequestBody List<Long> ids) {
        return deletionService.deleteAllByIds(ids)
                .then(Mono.fromSupplier(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).<Object>build()))
                .onErrorResume(NoSuchElementException.class,
                        ex -> Mono.fromSupplier(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).<Object>build()));
    }


}
