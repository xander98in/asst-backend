package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta con el resumen general grupal de un espacio de análisis,
 * incluyendo totales globales y resúmenes por cuestionario y forma intralaboral.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupSummaryResponseDTO {

    /** Total de personas evaluadas en el espacio de análisis. */
    @Schema(description = "Total de personas evaluadas en el espacio de análisis")
    private int totalPersons;

    /** Cantidad de personas evaluadas con Forma A (ILA). */
    @Schema(description = "Cantidad de personas evaluadas con Forma A (ILA)")
    private int formaACount;

    /** Cantidad de personas evaluadas con Forma B (ILB). */
    @Schema(description = "Cantidad de personas evaluadas con Forma B (ILB)")
    private int formaBCount;

    /** Resumen grupal del cuestionario intralaboral (todas las formas). */
    @Schema(description = "Resumen grupal del cuestionario intralaboral")
    private QuestionnaireGroupSummaryDTO intralaboral;

    /** Resumen grupal intralaboral limitado a personas con Forma A (ILA). */
    @Schema(description = "Resumen grupal intralaboral de personas con Forma A (ILA)")
    private QuestionnaireGroupSummaryDTO intralaboralFormaA;

    /** Resumen grupal intralaboral limitado a personas con Forma B (ILB). */
    @Schema(description = "Resumen grupal intralaboral de personas con Forma B (ILB)")
    private QuestionnaireGroupSummaryDTO intralaboralFormaB;

    /** Resumen grupal del cuestionario extralaboral. */
    @Schema(description = "Resumen grupal del cuestionario extralaboral")
    private QuestionnaireGroupSummaryDTO extralaboral;

    /** Resumen grupal del puntaje total general (intralaboral + extralaboral). */
    @Schema(description = "Resumen grupal del puntaje total general")
    private QuestionnaireGroupSummaryDTO generalTotal;

    /** Resumen grupal del cuestionario de estrés. */
    @Schema(description = "Resumen grupal del cuestionario de estrés")
    private QuestionnaireGroupSummaryDTO stress;
}
