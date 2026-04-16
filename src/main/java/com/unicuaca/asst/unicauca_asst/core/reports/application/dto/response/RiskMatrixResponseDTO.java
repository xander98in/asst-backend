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
public class RiskMatrixResponseDTO {

    @Schema(description = "Dimensiones intralaborales de la matriz de riesgo")
    private List<RiskMatrixEntryDTO> intralaboralDimensions;

    @Schema(description = "Dominios intralaborales de la matriz de riesgo")
    private List<RiskMatrixEntryDTO> intralaboralDomains;

    @Schema(description = "Total intralaboral de la matriz de riesgo")
    private RiskMatrixEntryDTO intralaboralTotal;

    @Schema(description = "Dimensiones extralaborales de la matriz de riesgo")
    private List<RiskMatrixEntryDTO> extralaboralDimensions;

    @Schema(description = "Total extralaboral de la matriz de riesgo")
    private RiskMatrixEntryDTO extralaboralTotal;
}
