package com.unicuaca.asst.unicauca_asst.common.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de los niveles socioeconómicos.
 * Incluye información sobre el ID y el nombre del nivel.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocioeconomicLevelResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "2 | Finca | No sé")
    private String name;
}
