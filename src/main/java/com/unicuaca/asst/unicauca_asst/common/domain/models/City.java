package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.*;

/**
 * Representa una ciudad en el dominio.
 * Cada ciudad pertenece a un departamento.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class City {

    private Long id;
    private String code;
    private String name;

    /**
     * Departamento al que pertenece la ciudad.
     * Nota: Cuida el mapeo para no generar ciclos con Department.cities.
     */
    private Department department;
}
