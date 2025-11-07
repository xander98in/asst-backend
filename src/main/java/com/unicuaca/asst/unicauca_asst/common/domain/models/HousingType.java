package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.*;

/**
 * Representa un tipo de vivienda.
 * Incluye información como el ID y el nombre del tipo de vivienda.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class HousingType {

    private Long id;
    private String name;

}
