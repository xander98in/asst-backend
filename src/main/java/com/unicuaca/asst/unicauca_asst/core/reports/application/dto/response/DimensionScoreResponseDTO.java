package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta que representa el resultado de calificación de una dimensión.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DimensionScoreResponseDTO {

    /** Nombre de la dimensión. */
    @Schema(description = "Nombre de la dimensión", example = "Liderazgo y relaciones sociales en el trabajo")
    private String dimensionName;

    /** Puntaje bruto obtenido en la dimensión. */
    @Schema(description = "Puntaje bruto de la dimensión", example = "28")
    private int rawScore;

    /** Puntaje transformado de la dimensión según el baremo aplicado. */
    @Schema(description = "Puntaje transformado de la dimensión", example = "35.0")
    private double transformedScore;

    /** Nivel de riesgo asignado a la dimensión tras aplicar el baremo. */
    @Schema(description = "Nivel de riesgo de la dimensión", example = "Riesgo medio")
    private String riskLevel;
}
