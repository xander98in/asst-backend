package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.*;

/**
 * Clase que representa el nivel socioeconómico.
 * Incluye información sobre el ID y el nombre del nivel.
 */
@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
public class SocioeconomicLevel {

    private Long id;
    private String name;

}
