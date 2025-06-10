
package com.kachalova.streamprocessing.api.dto;

import lombok.Data;
import java.util.Map;

@Data
public class AnonymizeRequest {
    private String ruleSetId;
    private Map<String, String> data;
}
