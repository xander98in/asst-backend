package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.*;

/**
 * Modelo que representa el género de una persona.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Gender {

    private Long id;
    private String name;
}
