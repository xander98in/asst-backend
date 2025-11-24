package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de respuesta para preguntas con información del cuestionario asociado.
 * (Sin objetos anidados, estructura completamente plana)
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionWithQuestionnaireResponseDTO {

    @Schema(example = "1", description = "ID de la pregunta")
    private Long id;

    @Schema(
        example = "En mi trabajo el tiempo asignado a mis tareas es suficiente.",
        description = "Texto de la pregunta"
    )
    private String text;

    @Schema(
        example = "1",
        description = "Orden o posición de la pregunta dentro del cuestionario"
    )
    private Integer order;

    /* ==== Datos del cuestionario asociado (PLANO) ==== */

    @Schema(example = "2", description = "ID del cuestionario al que pertenece la pregunta")
    private Long questionnaireId;

    @Schema(
        example = "Cuestionario Intralaboral - Forma A",
        description = "Nombre del cuestionario asociado"
    )
    private String questionnaireName;

    @Schema(
        example = "ILA",
        description = "Abreviatura del cuestionario asociado"
    )
    private String questionnaireAbbreviation;

    @Schema(
        example = "Instrumento para evaluar factores intralaborales en cargos con responsabilidad",
        description = "Descripción del cuestionario asociado"
    )
    private String questionnaireDescription;

}
