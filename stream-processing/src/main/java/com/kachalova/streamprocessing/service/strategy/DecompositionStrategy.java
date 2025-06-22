package com.kachalova.streamprocessing.service.strategy;

import com.kachalova.streamprocessing.dto.OriginalDataDto;
import com.kachalova.streamprocessing.model.DecomposedContact;
import com.kachalova.streamprocessing.model.DecomposedIdentity;
import com.kachalova.streamprocessing.model.DecomposedMeta;
import com.kachalova.streamprocessing.repository.DecomposedContactRepository;
import com.kachalova.streamprocessing.repository.DecomposedIdentityRepository;
import com.kachalova.streamprocessing.repository.DecomposedMetaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DecompositionStrategy implements AnonymizationStrategy {

    private final DecomposedIdentityRepository identityRepository;
    private final DecomposedContactRepository contactRepository;
    private final DecomposedMetaRepository metaRepository;



    @Override
    public Mono<String> anonymize(Object input, Map<String, Object> params) {
        if (!(input instanceof OriginalDataDto dto)) {
            return Mono.error(new IllegalArgumentException("Input is not of type OriginalDataDto"));
        }
        log.info("DecompositionStrategy OriginalDataDto input: {}",input);
        Mono<Void> saveIdentity = identityRepository.save(
                DecomposedIdentity.builder()
                        .id(dto.getId())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .patronymic(dto.getPatronymic())
                        .gender(dto.getGender())
                        .build()
        ).then();

        Mono<Void> saveContact = contactRepository.save(
                DecomposedContact.builder()
                        .id(dto.getId())
                        .phoneNumber(dto.getPhoneNumber())
                        .email(dto.getEmail())
                        .build()
        ).then();
        Mono<Void> saveMeta = metaRepository.save(
                DecomposedMeta.builder()
                        .id(dto.getId())
                        .ruleSetId((Long) params.get("rule_set_id"))
                        .createdAt(LocalDateTime.now())
                        .build()
        ).then();

        return Mono.when(saveIdentity, saveContact, saveMeta)
                .thenReturn("DECOMPOSED");
    }
    @Override
    public boolean requiresFullDto() {
        return true;
    }

}

