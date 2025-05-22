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
    @NotBlank(message = "{person.identificationNumber.notBlank}")
    @Size(min = 6, max = 20, message = "{person.identificationNumber.size}")
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "{person.identificationNumber.pattern}")
    private String identificationNumber;

    /**
     * Nombres de la persona.
     */
    @NotBlank(message = "{person.firstName.notBlank}")
    @Size(min = 1, max = 80, message = "{person.firstName.size}")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "{person.firstName.pattern}")
    private String firstName;

    /**
     * Apellidos de la persona.
     */
    @NotBlank(message = "{person.lastName.notBlank}")
    @Size(min = 2, max = 80, message = "{person.lastName.size}")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "{person.lastName.pattern}")
    private String lastName;

    /**
     * Año de nacimiento de la persona.
     */
    @NotNull(message = "{person.birthYear.notNull}")
    @ValidBirthYear
    private Integer birthYear;

    /**
     * ID del tipo de identificación seleccionado (ej: 1 para Cédula, 2 para Pasaporte...).
     */
    @NotNull(message = "{person.identificationTypeId.notNull}")
    private Long identificationTypeId;
    /**
     * ID del género seleccionado (ej: 1 para Masculino, 2 para Femenino...).
     */
    @NotNull(message = "{person.genderId.notNull}")
    private Long genderId;
}

