package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa el nivel socioeconómico.
 * Incluye información sobre el ID y el nombre del nivel.
 */
@Setter
@Getter
@AllArgsConstructor
@Builder
public class SocioeconomicLevel {

    private Long id;
    private String name;

}
