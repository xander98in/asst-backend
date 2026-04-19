package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.*;

/**
 * Representa una opción de respuesta posible en los cuestionarios.
 *
 * <p>Cada opción tiene un texto visible, un valor numérico utilizado en el cálculo
 * de puntajes y un orden que define su posición en la lista de opciones.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AnswerOption {

    /** Identificador único de la opción de respuesta. */
    private Long id;

    /** Texto visible de la opción de respuesta. */
    private String text;

    /** Valor numérico asociado a la opción, utilizado en el cálculo de puntajes. */
    private Integer value;

    /** Posición de la opción dentro del conjunto de opciones disponibles. */
    private Integer order;
}
