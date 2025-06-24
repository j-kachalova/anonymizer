
package com.kachalova.streamprocessing.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Table("identifier_mapping")
public class IdentifierMapping {

    @Id
    private Long id;

    @Column("field_name")
    private String fieldName;

    @Column("original_value_hash")
    private String originalValueHash;

    @Column("identifier_value")
    private String identifierValue;

    @Column("rule_set_id")
    private Long ruleSetId;

    @Column("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getOriginalValueHash() { return originalValueHash; }
    public void setOriginalValueHash(String originalValueHash) { this.originalValueHash = originalValueHash; }

    public String getIdentifierValue() { return identifierValue; }
    public void setIdentifierValue(String identifierValue) { this.identifierValue = identifierValue; }

    public Long getRuleSetId() { return ruleSetId; }
    public void setRuleSetId(Long ruleSetId) { this.ruleSetId = ruleSetId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
