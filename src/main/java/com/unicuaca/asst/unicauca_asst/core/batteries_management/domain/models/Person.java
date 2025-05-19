package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Person {

    private Long id;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private IdentificationType identificationType;
    private Gender gender;
}
