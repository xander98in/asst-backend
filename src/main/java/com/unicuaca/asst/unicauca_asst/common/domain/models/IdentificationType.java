package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa un tipo de identificación.
 * Incluye información como la descripción y la abreviatura.
 */
@Setter
@Getter
@AllArgsConstructor
@Builder
public class IdentificationType {

    private Long idIdentificationType;
    private String description;
    private String abbreviation;

}
