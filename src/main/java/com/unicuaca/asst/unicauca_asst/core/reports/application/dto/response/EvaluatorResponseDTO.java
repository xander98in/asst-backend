package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta con los datos de un evaluador.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluatorResponseDTO {

    /** Identificador único del evaluador. */
    @Schema(description = "ID del evaluador", example = "1")
    private Long id;

    /** Nombre completo del evaluador. */
    @Schema(description = "Nombre completo del evaluador", example = "Ana María Pérez")
    private String fullName;

    /** Número de identificación del evaluador. */
    @Schema(description = "Número de cédula del evaluador", example = "1061234567")
    private String identificationNumber;

    /** Profesión del evaluador. */
    @Schema(description = "Profesión del evaluador", example = "Psicóloga")
    private String profession;

    /** Título de posgrado del evaluador (especialización, maestría o doctorado). */
    @Schema(description = "Postgrado del evaluador", example = "Esp. Seguridad y Salud en el Trabajo")
    private String postgraduateDegree;

    /** Número de tarjeta profesional del evaluador. */
    @Schema(description = "Número de tarjeta profesional", example = "TP-12345")
    private String professionalCardNumber;

    /** Número de licencia en seguridad y salud en el trabajo del evaluador. */
    @Schema(description = "Número de licencia en salud ocupacional", example = "LIC-98765")
    private String occupationalHealthLicense;

    /** Fecha de expiración de la licencia en seguridad y salud en el trabajo. */
    @Schema(description = "Fecha de expedición de la licencia", example = "2024-03-15")
    private LocalDate licenseExpirationDate;

    /** Fecha y hora en que se creó el registro del evaluador. */
    @Schema(description = "Fecha y hora de creación del registro", example = "2025-05-01T08:00:00")
    private LocalDateTime createdAt;
}
