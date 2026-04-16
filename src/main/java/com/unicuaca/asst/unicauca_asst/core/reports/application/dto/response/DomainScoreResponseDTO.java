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

    @Schema(description = "Nombre del dominio", example = "Liderazgo y relaciones sociales en el trabajo")
    private String domainName;

    @Schema(description = "Puntaje bruto del dominio", example = "56")
    private int rawScore;

    @Schema(description = "Puntaje transformado del dominio", example = "29.2")
    private double transformedScore;

    @Schema(description = "Nivel de riesgo del dominio", example = "Riesgo bajo")
    private String riskLevel;

    @Schema(description = "Dimensiones que componen el dominio")
    private List<DimensionScoreResponseDTO> dimensions;
}
