package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa el estado civil de una persona.
 * Incluye información como el ID y el nombre del estado civil.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class CivilStatus {

    private Long id;
    private String name;

}
