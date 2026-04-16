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
public class DomainsAndDimensionsResponseDTO {

    @Schema(description = "Dominios intralaborales con sus dimensiones y distribuciones")
    private List<DomainDistributionDTO> intralaboralDomains;

    @Schema(description = "Dimensiones extralaborales con sus distribuciones")
    private List<DimensionDistributionDTO> extralaboralDimensions;
}
