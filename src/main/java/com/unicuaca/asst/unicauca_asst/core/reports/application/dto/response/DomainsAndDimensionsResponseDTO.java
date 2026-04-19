package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta con el desglose grupal por dominios y dimensiones
 * para los cuestionarios intralaboral y extralaboral.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainsAndDimensionsResponseDTO {

    /** Dominios intralaborales con sus dimensiones y distribuciones de riesgo. */
    @Schema(description = "Dominios intralaborales con sus dimensiones y distribuciones")
    private List<DomainDistributionDTO> intralaboralDomains;

    /** Dimensiones extralaborales con sus distribuciones de riesgo. */
    @Schema(description = "Dimensiones extralaborales con sus distribuciones")
    private List<DimensionDistributionDTO> extralaboralDimensions;
}
