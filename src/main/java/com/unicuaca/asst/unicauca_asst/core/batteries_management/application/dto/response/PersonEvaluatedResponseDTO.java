package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para representar los datos públicos de una persona evaluada.
 * 
 * Incluye información básica como nombres, identificación, género, año de nacimiento y correo electrónico.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonEvaluatedResponseDTO {

    @Schema(example = "1", description = "ID único de la persona evaluada")
    private Long id;

    @Schema(example = "12345678", description = "Número de identificación de la persona evaluada")
    private String identificationNumber;
   
    @Schema(example = "Juan", description = "Primer nombre de la persona evaluada")
    private String firstName;

    @Schema(example = "Pérez", description = "Apellido de la persona evaluada")
    private String lastName;

    @Schema(example = "1985", description = "Año de nacimiento de la persona evaluada")
    private Integer birthYear;

    @Schema(example = "juan.perez@example.com", description = "Correo electrónico de la persona evaluada")
    private String email;

    @Schema(example = "Cédula de Ciudadanía", description = "Tipo de identificación")
    private String identificationType;

    @Schema(example = "Masculino", description = "Género de la persona evaluada")
    private String gender;
}
