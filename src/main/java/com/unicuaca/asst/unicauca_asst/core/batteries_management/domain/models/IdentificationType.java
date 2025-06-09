package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Representa un tipo de documento (Cédula, Pasaporte, etc.) de una persona evaluada.
 */
@Getter
@AllArgsConstructor
@Builder
public class IdentificationType {

    private Long id;
    private String description;

}
