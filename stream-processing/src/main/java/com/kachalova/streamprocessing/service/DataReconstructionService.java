package com.kachalova.streamprocessing.service;

import com.kachalova.streamprocessing.dto.OriginalDataDto;
import com.kachalova.streamprocessing.model.DecomposedContact;
import com.kachalova.streamprocessing.model.DecomposedIdentity;
import com.kachalova.streamprocessing.model.DecomposedMeta;
import com.kachalova.streamprocessing.repository.DecomposedContactRepository;
import com.kachalova.streamprocessing.repository.DecomposedIdentityRepository;
import com.kachalova.streamprocessing.repository.DecomposedMetaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DataReconstructionService {

    private final DecomposedIdentityRepository identityRepository;
    private final DecomposedContactRepository contactRepository;
    private final DecomposedMetaRepository metaRepository;

    public DataReconstructionService(DecomposedIdentityRepository identityRepository,
                                     DecomposedContactRepository contactRepository,
                                     DecomposedMetaRepository metaRepository) {
        this.identityRepository = identityRepository;
        this.contactRepository = contactRepository;
        this.metaRepository = metaRepository;
    }

    public Mono<OriginalDataDto> reconstructById(Long id) {
        Mono<DecomposedIdentity> identityMono = identityRepository.findById(id);
        Mono<DecomposedContact> contactMono = contactRepository.findById(id);
        Mono<DecomposedMeta> metaMono = metaRepository.findById(id);

        return Mono.zip(identityMono, contactMono, metaMono)
                .map(tuple -> {
                    DecomposedIdentity identity = tuple.getT1();
                    DecomposedContact contact = tuple.getT2();
                    DecomposedMeta meta = tuple.getT3();

                    return OriginalDataDto.builder()
                            .id(id)
                            .firstName(identity.getFirstName())
                            .lastName(identity.getLastName())
                            .patronymic(identity.getPatronymic())
                            .gender(identity.getGender())
                            .phoneNumber(contact.getPhoneNumber())
                            .email(contact.getEmail())
                            .createdAt(meta.getCreatedAt())
                            .build();
                });
    }
}
