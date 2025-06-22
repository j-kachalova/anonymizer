package com.kachalova.streamprocessing.service;

import com.kachalova.streamprocessing.dto.AnonymizedDataDto;
import com.kachalova.streamprocessing.dto.OriginalDataDto;
import com.kachalova.streamprocessing.mapper.AnonymizedDataMapper;
import com.kachalova.streamprocessing.mapper.OriginalDataMapper;
import com.kachalova.streamprocessing.repository.AnonymizedDataRepository;
import com.kachalova.streamprocessing.repository.OriginalDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DataStorageService {

    private final OriginalDataRepository originalDataRepository;
    private final AnonymizedDataRepository anonymizedDataRepository;
    private final OriginalDataMapper originalDataMapper;
    private final AnonymizedDataMapper anonymizedDataMapper;

    @Autowired
    public DataStorageService(
            OriginalDataRepository originalDataRepository,
            AnonymizedDataRepository anonymizedDataRepository,
            OriginalDataMapper originalDataMapper,
            AnonymizedDataMapper anonymizedDataMapper
    ) {
        this.originalDataRepository = originalDataRepository;
        this.anonymizedDataRepository = anonymizedDataRepository;
        this.originalDataMapper = originalDataMapper;
        this.anonymizedDataMapper = anonymizedDataMapper;
    }

    public Mono<Void> saveOriginal(OriginalDataDto originalDto) {
        return originalDataRepository
                .save(originalDataMapper.toEntity(originalDto))
                .then();
    }

    public Mono<Void> saveAnonymized(AnonymizedDataDto anonymizedDto) {
        return anonymizedDataRepository
                .save(anonymizedDataMapper.toEntity(anonymizedDto))
                .then();
    }

    public Mono<AnonymizedDataDto> saveAll(OriginalDataDto originalDto, AnonymizedDataDto anonymizedDto) {
        return saveOriginal(originalDto)
                .then(saveAnonymized(anonymizedDto))
                .thenReturn(anonymizedDto);
    }
}
