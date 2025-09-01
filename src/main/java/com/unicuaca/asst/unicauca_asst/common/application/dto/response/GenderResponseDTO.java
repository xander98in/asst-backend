package com.unicuaca.asst.unicauca_asst.common.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para representar el género de una persona.
 * Incluye información como el ID y el nombre del género.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenderResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Masculino")
    private String name;
}
