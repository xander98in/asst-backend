package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.validation.ValidBirthYear;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonCreateRequestDTO {

    /**
     * Número de identificación de la persona (único).
     */
    @NotBlank(message = "El número de identificación no puede estar vacío")
    @Size(min=6, max = 20, message = "El número de identificación debe tener entre 6 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "El número de identificación solo puede contener letras, números y guiones")
    private String identificationNumber;

    /**
     * Nombres de la persona.
     */
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 1, max = 80, message = "Los nombres deben tener entre 1 y 80 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "Los nombres solo pueden contener letras y espacios")
    private String firstName;

    /**
     * Apellidos de la persona.
     */
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 80, message = "Los apellidos deben tener entre 2 y 80 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "Los apellidos solo pueden contener letras y espacios")
    private String lastName;

    /**
     * Año de nacimiento de la persona.
     */
    @NotNull(message = "El año de nacimiento es obligatorio")
    @ValidBirthYear
    private Integer birthYear;

    /**
     * ID del tipo de identificación seleccionado (ej: 1 para Cédula, 2 para Pasaporte...).
     */
    @NotNull(message = "El tipo de identificación es obligatorio")
    private Long identificationTypeId;

    /**
     * ID del género seleccionado (ej: 1 para Masculino, 2 para Femenino...).
     */
    @NotNull(message = "El género es obligatorio")
    private Long genderId;
}

