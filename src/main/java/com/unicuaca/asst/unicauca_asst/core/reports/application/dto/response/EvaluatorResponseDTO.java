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

    @Schema(description = "ID del evaluador", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del evaluador", example = "Ana María Pérez")
    private String fullName;

    @Schema(description = "Número de cédula del evaluador", example = "1061234567")
    private String identificationNumber;

    @Schema(description = "Profesión del evaluador", example = "Psicóloga")
    private String profession;

    @Schema(description = "Postgrado del evaluador", example = "Esp. Seguridad y Salud en el Trabajo")
    private String postgraduateDegree;

    @Schema(description = "Número de tarjeta profesional", example = "TP-12345")
    private String professionalCardNumber;

    @Schema(description = "Número de licencia en salud ocupacional", example = "LIC-98765")
    private String occupationalHealthLicense;

    @Schema(description = "Fecha de expedición de la licencia", example = "2024-03-15")
    private LocalDate licenseExpirationDate;

    @Schema(description = "Fecha y hora de creación del registro", example = "2025-05-01T08:00:00")
    private LocalDateTime createdAt;
}
