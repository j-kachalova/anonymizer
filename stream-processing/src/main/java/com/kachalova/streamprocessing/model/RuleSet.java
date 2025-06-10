package com.kachalova.streamprocessing.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("rulesets")
public class RuleSet {

    @Id
    private UUID id;
    private String name;
    private String description;
    private String jsonDefinition;
    private LocalDateTime createdAt;
}
