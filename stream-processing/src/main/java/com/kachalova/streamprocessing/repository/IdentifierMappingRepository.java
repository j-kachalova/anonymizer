package com.kachalova.streamprocessing.repository;

import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class IdentifierMappingRepository {

    private final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();

    public String getOrCreateIdentifier(String originalValue) {
        return store.computeIfAbsent(originalValue, v -> "ID-" + v.hashCode());
    }
}
