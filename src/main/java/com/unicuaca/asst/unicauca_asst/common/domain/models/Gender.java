package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Modelo que representa el género de una persona.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Gender {

    private Long id;
    private String name;
}
