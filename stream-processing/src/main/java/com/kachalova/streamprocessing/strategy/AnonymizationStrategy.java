package com.kachalova.streamprocessing.strategy;

import java.util.Map;

public interface AnonymizationStrategy {

    String anonymize(String originalValue, Map<String, Object> params);
}
