package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa un tipo de contrato en el sistema
 * Incluye información como el ID y el nombre del tipo de contrato.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ContractType {

    private Long id;
    private String name;
}
