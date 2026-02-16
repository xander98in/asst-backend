package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Representa la respuesta a una pregunta de un cuestionario.
 *
 * Incluye la referencia al registro de gestión del cuestionario, la pregunta respondida,
 * la opción de respuesta seleccionada, y las marcas de tiempo de creación y actualización.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class QuestionnaireResponse {

    private Long id;
    private QuestionnaireManagementRecord questionnaireManagementRecord;
    private Question question;
    private AnswerOption answerOption;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
