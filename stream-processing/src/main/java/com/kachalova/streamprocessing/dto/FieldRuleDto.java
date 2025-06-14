package com.kachalova.streamprocessing.dto;

public class FieldRuleDto {

    private String fieldName;
    private String strategy;
    private String paramsJson;

    // getters and setters

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }

    public String getParamsJson() { return paramsJson; }
    public void setParamsJson(String paramsJson) { this.paramsJson = paramsJson; }
}
