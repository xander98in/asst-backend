package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa a una persona evaluada en el proceso de riesgo psicosocial.
 * Incluye datos personales y su tipo de documento y sexo.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class PersonEvaluated {

    private Long id;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private String email;
    private IdentificationType identificationType;
    private Gender gender;
}
