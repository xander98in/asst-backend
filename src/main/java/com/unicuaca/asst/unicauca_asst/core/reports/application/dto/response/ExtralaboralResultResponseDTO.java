package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta que representa el resultado completo del cuestionario extralaboral.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtralaboralResultResponseDTO {

    /** Dimensiones extralaborales con sus calificaciones individuales. */
    @Schema(description = "Dimensiones extralaborales")
    private List<DimensionScoreResponseDTO> dimensions;

    /** Puntaje bruto total del cuestionario extralaboral. */
    @Schema(description = "Puntaje bruto total extralaboral", example = "45")
    private int totalRawScore;

    /** Puntaje transformado total del cuestionario extralaboral según el baremo aplicado. */
    @Schema(description = "Puntaje transformado total extralaboral", example = "36.3")
    private double totalTransformedScore;

    /** Nivel de riesgo total extralaboral tras aplicar el baremo. */
    @Schema(description = "Nivel de riesgo total extralaboral", example = "Riesgo medio")
    private String totalRiskLevel;
}
