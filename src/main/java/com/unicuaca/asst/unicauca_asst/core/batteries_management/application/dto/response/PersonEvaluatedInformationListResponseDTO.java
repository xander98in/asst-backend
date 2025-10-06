package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la información de una persona evaluada en una lista.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonEvaluatedInformationListResponseDTO {

    private Long id;
    private String identificationType;
    private String identificacionAbbreviation;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private String gender;
    private Integer birthYear;
    private String status;

}
