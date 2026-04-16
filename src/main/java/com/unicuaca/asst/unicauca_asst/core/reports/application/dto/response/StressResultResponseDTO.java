package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta que representa el resultado del cuestionario de estrés.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StressResultResponseDTO {

    @Schema(description = "Puntaje bruto de estrés", example = "18.5")
    private double rawScore;

    @Schema(description = "Puntaje transformado de estrés", example = "30.2")
    private double transformedScore;

    @Schema(description = "Nivel de estrés", example = "Medio")
    private String stressLevel;
}
