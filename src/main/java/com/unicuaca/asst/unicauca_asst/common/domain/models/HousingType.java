package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa un tipo de vivienda.
 * Incluye información como el ID y el nombre del tipo de vivienda.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class HousingType {

    private Long id;
    private String name;

}
