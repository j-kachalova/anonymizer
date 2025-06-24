package com.kachalova.streamprocessing.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Table("person_dictionary")
@Data
public class PersonDictionary {
    @Id
    private Long id;

    @Column("last_name")
    private String lastName;

    @Column("first_name")
    private String firstName;

    @Column("patronymic")
    private String patronymic;

    @Column("gender")
    private String gender;

    // геттеры и сеттеры
}

