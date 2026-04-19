package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta que representa el resultado de calificación de un dominio con sus dimensiones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainScoreResponseDTO {

    /** Nombre del dominio. */
    @Schema(description = "Nombre del dominio", example = "Liderazgo y relaciones sociales en el trabajo")
    private String domainName;

    /** Puntaje bruto obtenido en el dominio. */
    @Schema(description = "Puntaje bruto del dominio", example = "56")
    private int rawScore;

    /** Puntaje transformado del dominio según el baremo aplicado. */
    @Schema(description = "Puntaje transformado del dominio", example = "29.2")
    private double transformedScore;

    /** Nivel de riesgo asignado al dominio tras aplicar el baremo. */
    @Schema(description = "Nivel de riesgo del dominio", example = "Riesgo bajo")
    private String riskLevel;

    /** Dimensiones que componen el dominio con sus respectivas calificaciones. */
    @Schema(description = "Dimensiones que componen el dominio")
    private List<DimensionScoreResponseDTO> dimensions;
}
