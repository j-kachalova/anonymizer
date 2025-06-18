package com.kachalova.streamprocessing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnonymizedDataDto {
    private Long id;
    private Long ruleSetId;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String gender;
    private String phoneNumber;
    private String email;
    private LocalDateTime createdAt;
}

