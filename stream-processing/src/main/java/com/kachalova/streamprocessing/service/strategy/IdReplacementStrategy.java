
package com.kachalova.streamprocessing.service.strategy;

import com.kachalova.streamprocessing.model.IdentifierMapping;
import com.kachalova.streamprocessing.repository.IdentifierMappingRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Service
public class IdReplacementStrategy implements AnonymizationStrategy {

    @Autowired
    private IdentifierMappingRepository identifierMappingRepository;

    @Override
    public Mono<String> anonymize(Object input, Map<String, Object> params) {
        String prefix = (String) params.getOrDefault("id_prefix", "ID");
        String originalValue = String.valueOf(input);
        String hash = DigestUtils.sha256Hex(originalValue);

        return identifierMappingRepository.findByOriginalValueHash(hash)
                .map(IdentifierMapping::getIdentifierValue)
                .switchIfEmpty(Mono.defer(() -> {
                    String identifier = prefix + "-" + UUID.randomUUID();

                    IdentifierMapping mapping = new IdentifierMapping();
                    mapping.setFieldName((String) params.getOrDefault("field_name", "unknown"));
                    mapping.setOriginalValueHash(hash);
                    mapping.setIdentifierValue(identifier);
                    mapping.setRuleSetId(null);

                    return identifierMappingRepository.save(mapping)
                            .map(IdentifierMapping::getIdentifierValue);
                }));
    }
}
