package com.unicuaca.asst.unicauca_asst.core.reports.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceBatteryResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceSummaryResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpaceBattery;

/**
 * Mapper MapStruct para la conversión entre modelos de dominio y DTOs
 * de espacios de análisis.
 */
@Mapper(componentModel = "spring")
public interface AnalysisSpaceMapper {

    AnalysisSpaceResponseDTO toResponseDTO(AnalysisSpace space);

    AnalysisSpaceBatteryResponseDTO toBatteryDTO(AnalysisSpaceBattery battery);

    @Mapping(target = "batteryCount", expression = "java(countBatteries(space))")
    AnalysisSpaceSummaryResponseDTO toSummaryDTO(AnalysisSpace space);

    default int countBatteries(AnalysisSpace space) {
        return space.getBatteries() != null ? space.getBatteries().size() : 0;
    }
}
