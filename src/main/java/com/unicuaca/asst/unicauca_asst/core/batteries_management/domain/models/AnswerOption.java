package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.*;

/**
 * Representa una opción de respuesta posible en los cuestionarios.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AnswerOption {

    private Long id;
    private String text;
    private Integer value;
    private Integer order;
}
