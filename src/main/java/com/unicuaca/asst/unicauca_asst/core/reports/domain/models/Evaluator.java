package com.unicuaca.asst.unicauca_asst.core.reports.domain.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Modelo de dominio que representa a un evaluador profesional autorizado
 * para gestionar evaluaciones psicosociales en la plataforma.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Evaluator {

    /** Identificador único del evaluador. */
    private Long id;

    /** Nombre completo del evaluador. */
    private String fullName;

    /** Número de identificación del evaluador. */
    private String identificationNumber;

    /** Profesión del evaluador (ej: Psicología). */
    private String profession;

    /** Título de posgrado del evaluador (especialización, maestría o doctorado). */
    private String postgraduateDegree;

    /** Número de tarjeta profesional del evaluador. */
    private String professionalCardNumber;

    /** Número de licencia en seguridad y salud en el trabajo del evaluador. */
    private String occupationalHealthLicense;

    /** Fecha de expedición de la licencia en seguridad y salud en el trabajo. */
    private LocalDate licenseIssueDate;

    /** Identificador del usuario del sistema que registró al evaluador. */
    private Long creatorUserId;

    /** Fecha y hora en que se creó el registro del evaluador. */
    private LocalDateTime createdAt;
}
