package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Representa la respuesta a una pregunta de un cuestionario.
 *
 * <p>Incluye la referencia al registro de gestión del cuestionario, la pregunta
 * respondida, la opción de respuesta seleccionada y las marcas de tiempo
 * de creación y actualización.</p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class QuestionnaireResponse {

    /** Identificador único de la respuesta. */
    private Long id;

    /** Registro de gestión del cuestionario al que pertenece la respuesta. */
    private QuestionnaireManagementRecord questionnaireManagementRecord;

    /** Pregunta a la que corresponde esta respuesta. */
    private Question question;

    /** Opción de respuesta seleccionada para la pregunta. */
    private AnswerOption answerOption;

    /** Fecha y hora de creación de la respuesta. */
    private LocalDateTime createdAt;

    /** Fecha y hora de la última actualización de la respuesta. */
    private LocalDateTime updatedAt;
}
