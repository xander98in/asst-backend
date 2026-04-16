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
public class RiskMatrixEntryDTO {

    @Schema(description = "Nombre de la dimensión, dominio o total")
    private String name;

    @Schema(description = "Magnitud del riesgo en porcentaje")
    private double riskMagnitudePercent;

    @Schema(description = "Índice de asociación con estrés")
    private double associationIndex;

    @Schema(description = "Semáforo de magnitud: VERDE, AMARILLO o ROJO")
    private String magnitudeSemaphore;

    @Schema(description = "Semáforo de asociación: VERDE, AMARILLO o ROJO")
    private String associationSemaphore;
}
