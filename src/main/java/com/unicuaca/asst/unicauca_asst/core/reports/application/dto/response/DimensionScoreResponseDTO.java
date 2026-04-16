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

    @Schema(description = "Nombre de la dimensión", example = "Liderazgo y relaciones sociales en el trabajo")
    private String dimensionName;

    @Schema(description = "Puntaje bruto de la dimensión", example = "28")
    private int rawScore;

    @Schema(description = "Puntaje transformado de la dimensión", example = "35.0")
    private double transformedScore;

    @Schema(description = "Nivel de riesgo de la dimensión", example = "Riesgo medio")
    private String riskLevel;
}
