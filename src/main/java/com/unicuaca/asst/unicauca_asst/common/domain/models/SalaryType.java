package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.*;

/**
 * Representa un tipo de salario en el sistema.
 * Incluye información sobre su identificación y nombre.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class SalaryType {

    private Long id;
    private String name;
}
