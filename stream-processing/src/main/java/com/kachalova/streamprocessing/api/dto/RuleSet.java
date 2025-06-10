
package com.kachalova.streamprocessing.api.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class RuleSet {
    private String id;
    private String description;
    private Map<String, FieldRule> rules;
}
