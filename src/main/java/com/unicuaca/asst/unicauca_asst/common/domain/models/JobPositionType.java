package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.*;

/**
 * Representa un tipo de cargo.
 * Incluye información como el ID y el nombre del tipo de cargo.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class JobPositionType {

    private Long id;
    private String name;
}
