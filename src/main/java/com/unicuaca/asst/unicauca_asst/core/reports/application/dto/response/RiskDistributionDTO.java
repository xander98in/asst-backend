package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskDistributionDTO {

    @Schema(description = "Cantidad de personas sin riesgo o riesgo despreciable")
    private int sinRiesgo;

    @Schema(description = "Cantidad de personas con riesgo bajo")
    private int bajo;

    @Schema(description = "Cantidad de personas con riesgo medio")
    private int medio;

    @Schema(description = "Cantidad de personas con riesgo alto")
    private int alto;

    @Schema(description = "Cantidad de personas con riesgo muy alto")
    private int muyAlto;

    @Schema(description = "Cantidad de evaluaciones inválidas (ítems sin responder)")
    private int invalido;

    @Schema(description = "Cantidad de personas no evaluadas en esta dimensión")
    private int noEvaluado;
}
