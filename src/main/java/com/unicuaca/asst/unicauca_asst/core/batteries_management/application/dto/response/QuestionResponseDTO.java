package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de respuesta para preguntas sin información del cuestionario asociado.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponseDTO {

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

}
