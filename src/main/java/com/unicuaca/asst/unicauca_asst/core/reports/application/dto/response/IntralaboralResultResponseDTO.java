package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta que representa el resultado completo del cuestionario intralaboral.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntralaboralResultResponseDTO {

    @Schema(description = "Dominios con sus dimensiones")
    private List<DomainScoreResponseDTO> domains;

    @Schema(description = "Puntaje bruto total intralaboral", example = "120")
    private int totalRawScore;

    @Schema(description = "Puntaje transformado total intralaboral", example = "28.6")
    private double totalTransformedScore;

    @Schema(description = "Nivel de riesgo total intralaboral", example = "Riesgo bajo")
    private String totalRiskLevel;
}
