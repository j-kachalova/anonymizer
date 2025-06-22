
package com.kachalova.streamprocessing.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@Table("original_data")
public class OriginalData {

    @Id
    private Long id;
    @Column("last_name")
    private String lastName;
    @Column("first_name")
    private String firstName;
    private String patronymic;
    private String gender;
    @Column("phone_number")
    private String phoneNumber;
    private String email;
    @Column("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
