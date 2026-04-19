package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta con la distribución de riesgo grupal de un dominio,
 * su puntaje transformado promedio y las dimensiones que lo componen.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainDistributionDTO {

    /** Nombre del dominio. */
    @Schema(description = "Nombre del dominio")
    private String domainName;

    /** Distribución de personas por nivel de riesgo en el dominio. */
    @Schema(description = "Distribución de personas por nivel de riesgo en el dominio")
    private RiskDistributionDTO distribution;

    /** Promedio del puntaje transformado del dominio en el grupo. */
    @Schema(description = "Promedio del puntaje transformado del dominio en el grupo")
    private double averageTransformedScore;

    /** Dimensiones que componen el dominio con sus respectivas distribuciones. */
    @Schema(description = "Dimensiones del dominio con sus distribuciones")
    private List<DimensionDistributionDTO> dimensions;
}
