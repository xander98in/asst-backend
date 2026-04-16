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
public class DimensionDistributionDTO {

    @Schema(description = "Nombre de la dimensión")
    private String dimensionName;

    @Schema(description = "Distribución de personas por nivel de riesgo en la dimensión")
    private RiskDistributionDTO distribution;

    @Schema(description = "Promedio del puntaje transformado de la dimensión en el grupo")
    private double averageTransformedScore;
}
