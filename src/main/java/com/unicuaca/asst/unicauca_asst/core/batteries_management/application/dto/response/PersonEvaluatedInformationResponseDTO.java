package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class PersonEvaluatedInformationResponseDTO {  //a

    @Schema(example = "1", description = "ID de la persona evaluada")
    private Long id;

    @Schema(example = "Cédula de ciudadanía", description = "Tipo de identificación de la persona evaluada")
    private String identificationType;

    @Schema(example = "CC", description = "Abreviatura del tipo de identificación de la persona evaluada")
    private String identificacionAbbreviation;

    @Schema(example = "123456789", description = "Número de identificación de la persona evaluada")
    private String identificationNumber;

    @Schema(example = "Juan", description = "Primer nombre de la persona evaluada")
    private String firstName;

    @Schema(example = "Pérez", description = "Apellido de la persona evaluada")
    private String lastName;

    @Schema(example = "1990", description = "Año de nacimiento de la persona evaluada")
    private Integer birthYear;

    @Schema(example = "Con registro", description = "Estado de la persona evaluada")
    private String status;
}
