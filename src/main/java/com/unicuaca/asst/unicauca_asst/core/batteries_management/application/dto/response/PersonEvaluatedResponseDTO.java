package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

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

    private Long id;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private String email;
    private String identificationType;
    private String gender;
    
}
