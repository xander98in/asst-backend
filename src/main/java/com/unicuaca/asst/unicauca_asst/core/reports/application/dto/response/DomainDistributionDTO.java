package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainDistributionDTO {

    @Schema(description = "Nombre del dominio")
    private String domainName;

    @Schema(description = "Distribución de personas por nivel de riesgo en el dominio")
    private RiskDistributionDTO distribution;

    @Schema(description = "Promedio del puntaje transformado del dominio en el grupo")
    private double averageTransformedScore;

    @Schema(description = "Dimensiones del dominio con sus distribuciones")
    private List<DimensionDistributionDTO> dimensions;
}
