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
 * Mapper MapStruct para la conversión entre los records producidos por el motor
 * de cálculo grupal y los DTOs de respuesta de informes grupales.
 */
@Mapper(componentModel = "spring")
public interface GroupReportMapper {

    /**
     * Convierte el resumen general grupal del espacio de análisis a su DTO de respuesta.
     *
     * @param result resumen grupal calculado por el motor
     * @return DTO de respuesta con el resumen general grupal
     */
    GroupSummaryResponseDTO toGroupSummaryDTO(GroupReportEngine.GroupSummaryResult result);

    /**
     * Convierte el resumen grupal de un cuestionario (o total) a su DTO de respuesta.
     *
     * @param summary resumen grupal del cuestionario calculado por el motor
     * @return DTO de respuesta equivalente
     */
    QuestionnaireGroupSummaryDTO toQuestionnaireSummaryDTO(GroupReportEngine.QuestionnaireGroupSummary summary);

    /**
     * Convierte la distribución de riesgo (conteo por nivel) a su DTO de respuesta.
     *
     * @param distribution distribución de riesgo calculada por el motor
     * @return DTO de respuesta equivalente
     */
    RiskDistributionDTO toRiskDistributionDTO(GroupReportEngine.RiskDistribution distribution);

    /**
     * Convierte el resultado agregado de dominios y dimensiones intralaborales y
     * dimensiones extralaborales a su DTO de respuesta.
     *
     * @param result resultado de dominios y dimensiones calculado por el motor
     * @return DTO de respuesta equivalente
     */
    DomainsAndDimensionsResponseDTO toDomainsAndDimensionsDTO(GroupReportEngine.DomainsAndDimensionsResult result);

    /**
     * Convierte la distribución grupal de un dominio intralaboral a su DTO de respuesta.
     *
     * @param domain distribución del dominio calculada por el motor
     * @return DTO de respuesta equivalente
     */
    DomainDistributionDTO toDomainDistributionDTO(GroupReportEngine.DomainDistribution domain);

    /**
     * Convierte la distribución grupal de una dimensión (intralaboral o extralaboral)
     * a su DTO de respuesta.
     *
     * @param dimension distribución de la dimensión calculada por el motor
     * @return DTO de respuesta equivalente
     */
    DimensionDistributionDTO toDimensionDistributionDTO(GroupReportEngine.DimensionDistribution dimension);

    /**
     * Convierte la matriz de riesgo del grupo a su DTO de respuesta.
     *
     * @param result matriz de riesgo calculada por el motor
     * @return DTO de respuesta equivalente
     */
    RiskMatrixResponseDTO toRiskMatrixDTO(GroupReportEngine.RiskMatrixResult result);

    /**
     * Convierte una entrada individual de la matriz de riesgo (dimensión, dominio o total)
     * a su DTO de respuesta.
     *
     * @param entry entrada de la matriz de riesgo calculada por el motor
     * @return DTO de respuesta equivalente
     */
    RiskMatrixEntryDTO toRiskMatrixEntryDTO(GroupReportEngine.RiskMatrixEntry entry);
}
