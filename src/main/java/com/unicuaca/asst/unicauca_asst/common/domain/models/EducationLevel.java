package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa un nivel educativo en el sistema.
 * Incluye información como el ID y el nombre del nivel educativo.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class EducationLevel {

    private Long id;
    private String name;

}
