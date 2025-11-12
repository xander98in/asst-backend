package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import com.unicuaca.asst.unicauca_asst.common.domain.models.Gender;
import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa a una persona evaluada en el proceso de riesgo psicosocial.
 * Incluye datos personales y su tipo de documento y sexo.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class PersonEvaluated {

    private Long id;
    private IdentificationType identificationType;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private String email;
    private StatusPersonEvaluated status;
}
