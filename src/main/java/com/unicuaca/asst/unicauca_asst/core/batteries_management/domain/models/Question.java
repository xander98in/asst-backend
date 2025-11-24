package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa una pregunta asociada a un cuestionario.
 * 
 * Incluye el texto de la pregunta, su orden dentro del cuestionario y el cuestionario al que pertenece.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Question {

    /** Identificador único de la pregunta. */
    private Long id;

    /** Texto de la pregunta. */
    private String text;

    /** Posición u orden de la pregunta dentro de un cuestionario. */
    private Integer order;

    /** Cuestionario al que pertenece esta pregunta. */
    private Questionnaire questionnaire;

}
