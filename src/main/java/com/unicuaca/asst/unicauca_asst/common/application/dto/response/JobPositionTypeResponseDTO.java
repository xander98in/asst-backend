package com.unicuaca.asst.unicauca_asst.common.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para representar un tipo de cargo.
 * Incluye información como el ID y el nombre del tipo de cargo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPositionTypeResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Jefatura - tiene personal a cargo")
    private String name;
}
