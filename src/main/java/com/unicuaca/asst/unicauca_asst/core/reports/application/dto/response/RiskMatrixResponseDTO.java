package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta con la matriz de riesgo del grupo: magnitud del riesgo e índice
 * de asociación con estrés para dimensiones, dominios y totales intralaboral y extralaboral.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskMatrixResponseDTO {

    /** Entradas de la matriz para las dimensiones intralaborales. */
    @Schema(description = "Dimensiones intralaborales de la matriz de riesgo")
    private List<RiskMatrixEntryDTO> intralaboralDimensions;

    /** Entradas de la matriz para los dominios intralaborales. */
    @Schema(description = "Dominios intralaborales de la matriz de riesgo")
    private List<RiskMatrixEntryDTO> intralaboralDomains;

    /** Entrada de la matriz para el total intralaboral. */
    @Schema(description = "Total intralaboral de la matriz de riesgo")
    private RiskMatrixEntryDTO intralaboralTotal;

    /** Entradas de la matriz para las dimensiones extralaborales. */
    @Schema(description = "Dimensiones extralaborales de la matriz de riesgo")
    private List<RiskMatrixEntryDTO> extralaboralDimensions;

    /** Entrada de la matriz para el total extralaboral. */
    @Schema(description = "Total extralaboral de la matriz de riesgo")
    private RiskMatrixEntryDTO extralaboralTotal;
}
