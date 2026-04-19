package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta con el resumen grupal de un cuestionario, incluyendo totales,
 * promedios y distribución de riesgo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionnaireGroupSummaryDTO {

    /** Total de personas evaluadas en el cuestionario. */
    @Schema(description = "Total de personas evaluadas")
    private int totalPersons;

    /** Cantidad de personas evaluadas con Forma A (ILA). */
    @Schema(description = "Cantidad de personas evaluadas con Forma A (ILA)")
    private int formaACount;

    /** Cantidad de personas evaluadas con Forma B (ILB). */
    @Schema(description = "Cantidad de personas evaluadas con Forma B (ILB)")
    private int formaBCount;

    /** Promedio del puntaje transformado del grupo. */
    @Schema(description = "Promedio del puntaje transformado del grupo")
    private double averageTransformedScore;

    /** Nivel de riesgo calculado a partir del puntaje promedio del grupo. */
    @Schema(description = "Nivel de riesgo más frecuente en el grupo (moda)")
    private String averageRiskLevel;

    /** Distribución de personas por nivel de riesgo. */
    @Schema(description = "Distribución de personas por nivel de riesgo")
    private RiskDistributionDTO distribution;
}
