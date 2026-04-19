package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta con la distribución de riesgo grupal de una dimensión
 * y su puntaje transformado promedio.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DimensionDistributionDTO {

    /** Nombre de la dimensión. */
    @Schema(description = "Nombre de la dimensión")
    private String dimensionName;

    /** Distribución de personas por nivel de riesgo en la dimensión. */
    @Schema(description = "Distribución de personas por nivel de riesgo en la dimensión")
    private RiskDistributionDTO distribution;

    /** Promedio del puntaje transformado de la dimensión en el grupo. */
    @Schema(description = "Promedio del puntaje transformado de la dimensión en el grupo")
    private double averageTransformedScore;
}
