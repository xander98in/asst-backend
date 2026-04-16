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
public class GroupSummaryResponseDTO {

    @Schema(description = "Total de personas evaluadas en el espacio de análisis")
    private int totalPersons;

    @Schema(description = "Cantidad de personas evaluadas con Forma A (ILA)")
    private int formaACount;

    @Schema(description = "Cantidad de personas evaluadas con Forma B (ILB)")
    private int formaBCount;

    @Schema(description = "Resumen grupal del cuestionario intralaboral")
    private QuestionnaireGroupSummaryDTO intralaboral;

    @Schema(description = "Resumen grupal del cuestionario extralaboral")
    private QuestionnaireGroupSummaryDTO extralaboral;

    @Schema(description = "Resumen grupal del puntaje total general")
    private QuestionnaireGroupSummaryDTO generalTotal;

    @Schema(description = "Resumen grupal del cuestionario de estrés")
    private QuestionnaireGroupSummaryDTO stress;
}
