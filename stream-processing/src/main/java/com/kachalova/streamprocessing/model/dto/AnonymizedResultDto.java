package com.kachalova.streamprocessing.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AnonymizedResultDto {
    private Map<String, String> anonymizedFields;
}
