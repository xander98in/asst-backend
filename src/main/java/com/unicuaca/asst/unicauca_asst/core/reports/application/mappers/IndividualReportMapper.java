package com.unicuaca.asst.unicauca_asst.core.reports.application.mappers;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.DimensionScoreResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.DomainScoreResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.ExtralaboralResultResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.GeneralTotalResultResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.IndividualReportResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.IntralaboralResultResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.StressResultResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.ScoringEngine;
import org.mapstruct.Mapper;

/**
 * Mapper que convierte los records de resultado del {@link ScoringEngine}
 * a sus DTOs de respuesta correspondientes.
 */
@Mapper(componentModel = "spring")
public interface IndividualReportMapper {

    IndividualReportResponseDTO toResponseDTO(ScoringEngine.IndividualScoringResult result);

    IntralaboralResultResponseDTO toIntralaboralDTO(ScoringEngine.IntralaboralResult result);

    ExtralaboralResultResponseDTO toExtralaboralDTO(ScoringEngine.ExtralaboralResult result);

    StressResultResponseDTO toStressDTO(ScoringEngine.StressResult result);

    GeneralTotalResultResponseDTO toGeneralTotalDTO(ScoringEngine.GeneralTotalResult result);

    DomainScoreResponseDTO toDomainScoreDTO(ScoringEngine.DomainScore score);

    DimensionScoreResponseDTO toDimensionScoreDTO(ScoringEngine.DimensionScore score);
}
