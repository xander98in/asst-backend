package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request;

import com.unicuaca.asst.unicauca_asst.common.validation.FirstGroup;
import com.unicuaca.asst.unicauca_asst.common.validation.SecondGroup;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.validation.ValidBirthYear;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.validation.ValidIdentificationNumber;

import jakarta.validation.GroupSequence;
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
@ValidIdentificationNumber(
    fieldIdentificationNumber = "identificationNumber",
    fieldIdentificationType = "identificationType",
    message = "El número de identificación no es válido para el tipo de identificación proporcionado."
)
@GroupSequence({FirstGroup.class, SecondGroup.class, PersonEvaluatedUpdateRequestDTO.class})
public class PersonEvaluatedUpdateRequestDTO {

    /**
     * ID del tipo de identificación seleccionado (ej: 1 para Cédula, 2 para Pasaporte...).
     */
    @NotBlank(message = "{person.identificationType.notBlank}", groups = FirstGroup.class)
    @Size(min = 1, max = 3, message = "{person.identificationType.size}", groups = SecondGroup.class)
    private String identificationType;

    /**
     * Número de identificación de la persona (único).
     */
    @NotBlank(message = "{person.identificationNumber.notBlank}", groups = FirstGroup.class)
    @Size(min = 6, max = 20, message = "{person.identificationNumber.size}", groups = SecondGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "{person.identificationNumber.pattern}", groups = SecondGroup.class)
    @Pattern(regexp = "^[^\s].*[^\s]$", message = "{person.identificationNumber.noTrailingSpaces}", groups = SecondGroup.class)
    private String identificationNumber;

    /**
     * Año de nacimiento.
     */
    @NotNull(message = "{person.birthYear.notNull}")
    @ValidBirthYear(groups = SecondGroup.class)
    private Integer birthYear;

    /**
     * ID del nuevo género.
     */
    @NotNull(message = "{person.genderId.notNull}", groups = FirstGroup.class)
    private Long genderId;

    /**
     * Nombres de la persona evaluada.
     */
    @NotBlank(message = "{person.firstName.notBlank}", groups = FirstGroup.class)
    @Size(min = 1, max = 80, message = "{person.firstName.size}", groups = SecondGroup.class)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 ]+$", message = "{person.firstName.pattern}", groups = SecondGroup.class) 
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$).*", message = "{person.firstName.noTrailingSpaces}", groups = SecondGroup.class)
    private String firstName;

    /**
     * Apellidos de la persona evaluada.
     */
    @NotBlank(message = "{person.lastName.notBlank}", groups = FirstGroup.class)
    @Size(min = 1, max = 80, message = "{person.lastName.size}", groups = SecondGroup.class)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 ]+$", message = "{person.lastName.pattern}", groups = SecondGroup.class)
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$).*", message = "{person.lastName.noTrailingSpaces}", groups = SecondGroup.class)
    private String lastName;

    /**
     * Correo electrónico actualizado.
     */
    @NotBlank(message = "{person.email.notBlank}", groups = FirstGroup.class)
    @Size(min = 10, max = 100, message = "{person.email.size}", groups = SecondGroup.class)
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "{person.email.pattern}",
        groups = SecondGroup.class
    )
    @Pattern(regexp = "^[^\s].*[^\s]$", message = "{person.email.noTrailingSpaces}", groups = SecondGroup.class)
    private String email;
}
