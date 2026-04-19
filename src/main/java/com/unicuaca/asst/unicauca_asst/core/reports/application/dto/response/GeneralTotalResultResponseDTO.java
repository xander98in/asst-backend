package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta que representa el resultado del puntaje total general (intralaboral + extralaboral).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralTotalResultResponseDTO {

    /** Puntaje transformado total general (intralaboral + extralaboral) según el baremo aplicado. */
    @Schema(description = "Puntaje transformado total general", example = "30.1")
    private double transformedScore;

    /** Nivel de riesgo total general tras aplicar el baremo. */
    @Schema(description = "Nivel de riesgo total general", example = "Riesgo bajo")
    private String riskLevel;
}
