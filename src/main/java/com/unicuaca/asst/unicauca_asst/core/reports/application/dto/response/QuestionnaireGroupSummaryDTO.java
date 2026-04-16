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
public class QuestionnaireGroupSummaryDTO {

    @Schema(description = "Total de personas evaluadas")
    private int totalPersons;

    @Schema(description = "Cantidad de personas evaluadas con Forma A (ILA)")
    private int formaACount;

    @Schema(description = "Cantidad de personas evaluadas con Forma B (ILB)")
    private int formaBCount;

    @Schema(description = "Promedio del puntaje transformado del grupo")
    private double averageTransformedScore;

    @Schema(description = "Nivel de riesgo más frecuente en el grupo (moda)")
    private String averageRiskLevel;

    @Schema(description = "Distribución de personas por nivel de riesgo")
    private RiskDistributionDTO distribution;
}
