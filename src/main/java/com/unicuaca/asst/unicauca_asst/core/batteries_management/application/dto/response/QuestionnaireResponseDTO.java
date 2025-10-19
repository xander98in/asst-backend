package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para cuestionarios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionnaireResponseDTO {

    @Schema(example = "1", description = "ID del cuestionario")
    private Long id;

    @Schema(example = "Cuestionario Intralaboral - Forma A", description = "Nombre del cuestionario")
    private String name;

    @Schema(example = "ILA", description = "Abreviatura del cuestionario")
    private String abbreviation;

    @Schema(example = "Instrumento para evaluar factores intralaborales en cargos con responsabilidad", description = "Descripción del cuestionario")
    private String description;
}
