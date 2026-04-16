package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta resumido para el listado de espacios de análisis,
 * sin el detalle de baterías — solo el conteo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisSpaceSummaryResponseDTO {

    @Schema(description = "ID del espacio de análisis", example = "1")
    private Long id;

    @Schema(description = "Nombre descriptivo del espacio", example = "Postgrados Mayo 2025")
    private String name;

    @Schema(description = "ID del evaluador asociado", example = "3")
    private Long evaluatorId;

    @Schema(description = "Cantidad de baterías en el espacio", example = "5")
    private int batteryCount;

    @Schema(description = "Fecha y hora de creación del espacio", example = "2025-05-01T08:00:00")
    private LocalDateTime createdAt;
}
