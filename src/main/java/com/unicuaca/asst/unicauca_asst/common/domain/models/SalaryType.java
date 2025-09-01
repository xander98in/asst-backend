package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa un tipo de salario en el sistema.
 * Incluye información sobre su identificación y nombre.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class SalaryType {

    private Long id;
    private String name;
}
