package com.unicuaca.asst.unicauca_asst.core.catalog.domain.models;

import lombok.*;

/**
 * Representa un tipo de contrato en el sistema
 * Incluye información como el ID y el nombre del tipo de contrato.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class ContractType {

    private Long id;
    private String name;
}
