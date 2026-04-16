package com.unicuaca.asst.unicauca_asst.core.reports.application.mappers;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.DimensionDistributionDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.DomainDistributionDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.DomainsAndDimensionsResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.GroupSummaryResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.QuestionnaireGroupSummaryDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.RiskDistributionDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.RiskMatrixEntryDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.RiskMatrixResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.GroupReportEngine;
import org.mapstruct.Mapper;

/**
 * Mapper MapStruct para convertir records del motor grupal a DTOs de respuesta.
 */
@Mapper(componentModel = "spring")
public interface GroupReportMapper {

    GroupSummaryResponseDTO toGroupSummaryDTO(GroupReportEngine.GroupSummaryResult result);

    QuestionnaireGroupSummaryDTO toQuestionnaireSummaryDTO(GroupReportEngine.QuestionnaireGroupSummary summary);

    RiskDistributionDTO toRiskDistributionDTO(GroupReportEngine.RiskDistribution distribution);

    DomainsAndDimensionsResponseDTO toDomainsAndDimensionsDTO(GroupReportEngine.DomainsAndDimensionsResult result);

    DomainDistributionDTO toDomainDistributionDTO(GroupReportEngine.DomainDistribution domain);

    DimensionDistributionDTO toDimensionDistributionDTO(GroupReportEngine.DimensionDistribution dimension);

    RiskMatrixResponseDTO toRiskMatrixDTO(GroupReportEngine.RiskMatrixResult result);

    RiskMatrixEntryDTO toRiskMatrixEntryDTO(GroupReportEngine.RiskMatrixEntry entry);
}
