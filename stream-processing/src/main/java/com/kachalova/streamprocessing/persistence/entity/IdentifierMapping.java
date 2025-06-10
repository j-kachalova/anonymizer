
package com.kachalova.streamprocessing.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("identifier_mapping")
public class IdentifierMapping {

    @Id
    private Long id;

    private String fieldName;

    private String originalValue;

    private String identifier;
}
