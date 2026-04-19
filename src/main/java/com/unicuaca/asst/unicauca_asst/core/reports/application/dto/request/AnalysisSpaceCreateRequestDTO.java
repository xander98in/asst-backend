package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request;

import com.unicuaca.asst.unicauca_asst.common.validation.FirstGroup;
import com.unicuaca.asst.unicauca_asst.common.validation.SecondGroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO para la creación de un espacio de análisis.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({FirstGroup.class, SecondGroup.class, AnalysisSpaceCreateRequestDTO.class})
@Schema(description = "Datos para crear un espacio de análisis")
public class AnalysisSpaceCreateRequestDTO {

    /** Nombre descriptivo del espacio de análisis. */
    @NotBlank(message = "{analysisSpace.name.notBlank}", groups = FirstGroup.class)
    @Size(max = 150, message = "{analysisSpace.name.size}", groups = SecondGroup.class)
    @Schema(description = "Nombre descriptivo del espacio de análisis", example = "Postgrados Mayo 2025")
    private String name;

    /** Identificador del evaluador asociado al espacio. */
    @NotNull(message = "{analysisSpace.evaluatorId.notNull}", groups = FirstGroup.class)
    @Schema(description = "ID del evaluador asociado al espacio", example = "3")
    private Long evaluatorId;
}
