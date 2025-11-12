package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa un tipo de identificación.
 * Incluye información como el nombre y la abreviatura.
 */
@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
public class IdentificationType {

    private Long id;
    private String name;
    private String abbreviation;

}
