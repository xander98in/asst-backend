package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta con la distribución de personas por nivel de riesgo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskDistributionDTO {

    /** Cantidad de personas sin riesgo o riesgo despreciable. */
    @Schema(description = "Cantidad de personas sin riesgo o riesgo despreciable")
    private int sinRiesgo;

    /** Cantidad de personas con riesgo bajo. */
    @Schema(description = "Cantidad de personas con riesgo bajo")
    private int bajo;

    /** Cantidad de personas con riesgo medio. */
    @Schema(description = "Cantidad de personas con riesgo medio")
    private int medio;

    /** Cantidad de personas con riesgo alto. */
    @Schema(description = "Cantidad de personas con riesgo alto")
    private int alto;

    /** Cantidad de personas con riesgo muy alto. */
    @Schema(description = "Cantidad de personas con riesgo muy alto")
    private int muyAlto;

    /** Cantidad de evaluaciones inválidas por ítems sin responder. */
    @Schema(description = "Cantidad de evaluaciones inválidas (ítems sin responder)")
    private int invalido;

    /** Cantidad de personas no evaluadas en la dimensión. */
    @Schema(description = "Cantidad de personas no evaluadas en esta dimensión")
    private int noEvaluado;
}
