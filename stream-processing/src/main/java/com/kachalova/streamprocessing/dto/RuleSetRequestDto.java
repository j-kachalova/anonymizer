package com.kachalova.streamprocessing.dto;

import java.util.List;

public class RuleSetRequestDto {

    private String name;
    private String description;
    private List<FieldRuleDto> fieldRules;

    // getters and setters

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<FieldRuleDto> getFieldRules() { return fieldRules; }
    public void setFieldRules(List<FieldRuleDto> fieldRules) { this.fieldRules = fieldRules; }
}
