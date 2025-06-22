package com.kachalova.streamprocessing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("decomposition_meta")
public class DecomposedMeta {
    @Id
    private Long id;
    private Long ruleSetId;
    private LocalDateTime createdAt;
}

