package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonCreateRequestDTO {

    /**
     * Número de identificación de la persona (único).
     */
    private String identificationNumber;

    /**
     * Nombres de la persona.
     */
    private String firstName;

    /**
     * Apellidos de la persona.
     */
    private String lastName;

    /**
     * Año de nacimiento de la persona.
     */
    private Integer birthYear;

    /**
     * ID del tipo de identificación seleccionado (ej: 1 para Cédula, 2 para Pasaporte...).
     */
    private Long identificationTypeId;

    /**
     * ID del género seleccionado (ej: 1 para Masculino, 2 para Femenino...).
     */
    private Long genderId;

}
