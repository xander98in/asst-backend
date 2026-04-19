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

    /**
     * Convierte el resultado completo del cálculo individual a su DTO de respuesta.
     *
     * @param result resultado agregado producido por el motor de cálculo individual
     * @return DTO de respuesta con el informe individual completo
     */
    IndividualReportResponseDTO toResponseDTO(ScoringEngine.IndividualScoringResult result);

    /**
     * Convierte el resultado del cuestionario intralaboral a su DTO de respuesta.
     *
     * @param result resultado intralaboral calculado por el motor
     * @return DTO de respuesta equivalente
     */
    IntralaboralResultResponseDTO toIntralaboralDTO(ScoringEngine.IntralaboralResult result);

    /**
     * Convierte el resultado del cuestionario extralaboral a su DTO de respuesta.
     *
     * @param result resultado extralaboral calculado por el motor
     * @return DTO de respuesta equivalente
     */
    ExtralaboralResultResponseDTO toExtralaboralDTO(ScoringEngine.ExtralaboralResult result);

    /**
     * Convierte el resultado del cuestionario de estrés a su DTO de respuesta.
     *
     * @param result resultado de estrés calculado por el motor
     * @return DTO de respuesta equivalente
     */
    StressResultResponseDTO toStressDTO(ScoringEngine.StressResult result);

    /**
     * Convierte el resultado del puntaje total general (intralaboral + extralaboral)
     * a su DTO de respuesta.
     *
     * @param result resultado total general calculado por el motor
     * @return DTO de respuesta equivalente
     */
    GeneralTotalResultResponseDTO toGeneralTotalDTO(ScoringEngine.GeneralTotalResult result);

    /**
     * Convierte el puntaje de un dominio intralaboral a su DTO de respuesta.
     *
     * @param score puntaje del dominio calculado por el motor
     * @return DTO de respuesta equivalente
     */
    DomainScoreResponseDTO toDomainScoreDTO(ScoringEngine.DomainScore score);

    /**
     * Convierte el puntaje de una dimensión (intralaboral o extralaboral) a su DTO
     * de respuesta.
     *
     * @param score puntaje de la dimensión calculado por el motor
     * @return DTO de respuesta equivalente
     */
    DimensionScoreResponseDTO toDimensionScoreDTO(ScoringEngine.DimensionScore score);
}
