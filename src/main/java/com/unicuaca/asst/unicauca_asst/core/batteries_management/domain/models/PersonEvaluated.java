package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.IdentificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Representa a una persona evaluada en el proceso de riesgo psicosocial.
 *
 * <p>Incluye datos personales básicos, tipo y número de documento de identificación,
 * correo electrónico y estado actual (con o sin registro de batería asociada).</p>
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class PersonEvaluated {

    /** Identificador único de la persona evaluada. */
    private Long id;

    /** Fecha y hora de creación del registro. */
    private LocalDateTime createdAt;

    /** Fecha y hora de la última actualización del registro. */
    private LocalDateTime updatedAt;

    /** Tipo de documento de identificación (por ejemplo, CC, TI, CE, PA). */
    private IdentificationType identificationType;

    /** Número del documento de identificación. */
    private String identificationNumber;

    /** Nombres de la persona evaluada. */
    private String firstName;

    /** Apellidos de la persona evaluada. */
    private String lastName;

    /** Año de nacimiento de la persona evaluada. */
    private Integer birthYear;

    /** Correo electrónico de la persona evaluada. */
    private String email;

    /** Estado actual de la persona evaluada (con o sin registro de batería asociada). */
    private StatusPersonEvaluated status;
}
