package com.kachalova.streamprocessing.service;

import com.kachalova.streamprocessing.repository.DecomposedContactRepository;
import com.kachalova.streamprocessing.repository.DecomposedIdentityRepository;
import com.kachalova.streamprocessing.repository.DecomposedMetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DataDeletionService {

    private final DecomposedIdentityRepository identityRepository;
    private final DecomposedContactRepository contactRepository;
    private final DecomposedMetaRepository metaRepository;


    public Mono<Void> deleteById(Long id) {
        return Mono.when(
                identityRepository.deleteById(id),
                contactRepository.deleteById(id),
                metaRepository.deleteById(id)
        );
    }

    public Mono<Void> deleteAllByIds(List<Long> ids) {
        Mono<Boolean> hasAny =
                Mono.zip(
                                identityRepository.existsById(ids.get(0)), // быстрая проверка по первому
                                contactRepository.existsById(ids.get(0)),
                                metaRepository.existsById(ids.get(0))
                        )
                        .map(tuple -> tuple.getT1() || tuple.getT2() || tuple.getT3());

        Mono<Boolean> hasInAnyRepo = Flux.fromIterable(ids)
                .flatMap(id -> Mono.zip(
                        identityRepository.existsById(id),
                        contactRepository.existsById(id),
                        metaRepository.existsById(id)
                ))
                .any(tuple -> tuple.getT1() || tuple.getT2() || tuple.getT3());

        return hasInAnyRepo.flatMap(found -> {
            if (!found) {
                return Mono.error(new NoSuchElementException("Ни один из указанных id не найден"));
            }
            return Mono.when(
                    identityRepository.deleteAllById(ids).then(),
                    contactRepository.deleteAllById(ids).then(),
                    metaRepository.deleteAllById(ids).then()
            );
        });
    }

}
