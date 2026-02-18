package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa el detalle de una respuesta individual dentro de un cuestionario.
 * Incluye información de la respuesta, la pregunta asociada y la opción seleccionada.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionnaireAnswerResponseDTO {

    @Schema(description = "ID único del registro de respuesta", example = "501")
    private Long id;

    // --- Información de la Pregunta ---
    @Schema(description = "ID de la pregunta", example = "10")
    private Long questionId;

    @Schema(description = "Texto de la pregunta", example = "¿Siente que tiene demasiado trabajo?")
    private String questionText;

    @Schema(description = "Orden de la pregunta en el cuestionario", example = "1")
    private Integer questionOrder;

    // --- Información de la Opción de Respuesta ---
    @Schema(description = "ID de la opción de respuesta seleccionada", example = "3")
    private Long answerOptionId;

    @Schema(description = "Texto de la opción seleccionada", example = "Siempre")
    private String answerOptionText;

    @Schema(description = "Valor numérico de la opción seleccionada", example = "5")
    private Integer answerOptionValue;

    @Schema(description = "Orden visual de la opción de respuesta", example = "1")
    private Integer answerOptionOrder;
}
