package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Modelo que representa los estados de una persona evaluada.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class StatusPersonEvaluated {

    private Long id;
    private String name;
}
