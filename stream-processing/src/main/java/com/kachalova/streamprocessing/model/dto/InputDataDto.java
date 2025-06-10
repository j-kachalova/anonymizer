package com.kachalova.streamprocessing.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class InputDataDto {
    private Map<String, String> fields;
}
