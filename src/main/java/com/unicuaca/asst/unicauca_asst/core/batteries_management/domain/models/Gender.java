package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Representa un sexo (Masculino, Femenino, Otro) asignado a una persona evaluada.
 */
@Getter
@AllArgsConstructor
@Builder
public class Gender {

    private Long id;
    private String description;
}
