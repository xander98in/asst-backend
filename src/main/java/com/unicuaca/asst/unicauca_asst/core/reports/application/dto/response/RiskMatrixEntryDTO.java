package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta con una entrada de la matriz de riesgo: magnitud del riesgo,
 * índice de asociación con estrés y los semáforos correspondientes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskMatrixEntryDTO {

    /** Nombre de la dimensión, dominio o total que identifica la entrada. */
    @Schema(description = "Nombre de la dimensión, dominio o total")
    private String name;

    /** Magnitud del riesgo expresada en porcentaje. */
    @Schema(description = "Magnitud del riesgo en porcentaje")
    private double riskMagnitudePercent;

    /** Índice de asociación entre el riesgo y el estrés. */
    @Schema(description = "Índice de asociación con estrés")
    private double associationIndex;

    /** Semáforo de magnitud del riesgo (VERDE, AMARILLO o ROJO). */
    @Schema(description = "Semáforo de magnitud: VERDE, AMARILLO o ROJO")
    private String magnitudeSemaphore;

    /** Semáforo de asociación con estrés (VERDE, AMARILLO o ROJO). */
    @Schema(description = "Semáforo de asociación: VERDE, AMARILLO o ROJO")
    private String associationSemaphore;
}
