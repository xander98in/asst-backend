package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa un tipo de cargo.
 * Incluye información como el ID y el nombre del tipo de cargo.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class JobPositionType {

    private Long id;
    private String name;
}
