
package com.kachalova.streamprocessing.api.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class FieldRule {
    private AnonymizationMethod method;
    private Map<String, Object> parameters;
}
