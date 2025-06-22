package com.kachalova.streamprocessing.model;

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
@Table("decomposition_identity")
public class DecomposedIdentity {
    @Id
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String gender;
}

