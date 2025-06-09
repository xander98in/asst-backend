package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.validation.ValidBirthYear;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonEvaluatedUpdateRequestDTO {

    /**
     * Nombres de la persona evaluada.
     */
    @NotBlank(message = "{person.firstName.notBlank}")
    @Size(min = 1, max = 80, message = "{person.firstName.size}")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "{person.firstName.pattern}")
    private String firstName;

    /**
     * Apellidos de la persona evaluada.
     */
    @NotBlank(message = "{person.lastName.notBlank}")
    @Size(min = 2, max = 80, message = "{person.lastName.size}")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "{person.lastName.pattern}")
    private String lastName;

    /**
     * Año de nacimiento.
     */
    @NotNull(message = "{person.birthYear.notNull}")
    @ValidBirthYear
    private Integer birthYear;

    /**
     * Correo electrónico actualizado.
     */
    @NotBlank(message = "{person.email.notBlank}")
    @Size(min = 10, max = 100, message = "{person.email.size}")
    @Email(message = "{person.email.pattern}")
    private String email;

    /**
     * ID del nuevo género.
     */
    @NotNull(message = "{person.genderId.notNull}")
    private Long genderId;

}
