package com.unicuaca.asst.unicauca_asst.common.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para representar el nivel educativo de una persona.
 * Incluye información como el ID y la descripción del nivel educativo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationLevelResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Profesional completo")
    private String name;
}
