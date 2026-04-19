package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa el estado de una persona evaluada.
 *
 * <p>Incluye un identificador único y un nombre descriptivo que indica si la
 * persona tiene o no un registro de batería asociado (por ejemplo, "Sin registro",
 * "Con registro").</p>
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class StatusPersonEvaluated {

    /** Identificador único del estado. */
    private Long id;

    /** Nombre descriptivo del estado. */
    private String name;
}
