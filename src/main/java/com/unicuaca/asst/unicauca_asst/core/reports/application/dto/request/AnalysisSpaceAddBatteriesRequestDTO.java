package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.validation.FirstGroup;
import com.unicuaca.asst.unicauca_asst.common.validation.SecondGroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO para agregar baterías cerradas a un espacio de análisis.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({FirstGroup.class, SecondGroup.class, AnalysisSpaceAddBatteriesRequestDTO.class})
@Schema(description = "IDs de baterías cerradas a agregar al espacio de análisis")
public class AnalysisSpaceAddBatteriesRequestDTO {

    @NotNull(message = "{analysisSpace.batteryRecordIds.notNull}", groups = FirstGroup.class)
    @NotEmpty(message = "{analysisSpace.batteryRecordIds.notEmpty}", groups = FirstGroup.class)
    @Schema(description = "Lista de IDs de registros de gestión de batería", example = "[1, 2, 3]")
    private List<Long> batteryRecordIds;
}
