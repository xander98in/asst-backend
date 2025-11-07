package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.*;

/**
 * Representa el estado civil de una persona.
 * Incluye información como el ID y el nombre del estado civil.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class CivilStatus {

    private Long id;
    private String name;
}
