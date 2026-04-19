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

    /**
     * Convierte un espacio de análisis de dominio a DTO de respuesta con el detalle completo
     * incluyendo las baterías asociadas.
     *
     * @param space modelo de dominio a convertir
     * @return DTO de respuesta equivalente
     */
    AnalysisSpaceResponseDTO toResponseDTO(AnalysisSpace space);

    /**
     * Convierte una batería asociada a un espacio de análisis a su DTO de respuesta.
     *
     * @param battery modelo de dominio de la batería asociada
     * @return DTO de respuesta equivalente
     */
    AnalysisSpaceBatteryResponseDTO toBatteryDTO(AnalysisSpaceBattery battery);

    /**
     * Convierte un espacio de análisis a un DTO resumido, calculando la cantidad
     * de baterías asociadas en lugar de incluir el detalle.
     *
     * @param space modelo de dominio a convertir
     * @return DTO resumido con el conteo de baterías
     */
    @Mapping(target = "batteryCount", expression = "java(countBatteries(space))")
    AnalysisSpaceSummaryResponseDTO toSummaryDTO(AnalysisSpace space);

    /**
     * Cuenta la cantidad de baterías asociadas a un espacio de análisis,
     * retornando cero si la lista de baterías es nula.
     *
     * @param space espacio de análisis a evaluar
     * @return cantidad de baterías asociadas, o cero si la lista es nula
     */
    default int countBatteries(AnalysisSpace space) {
        return space.getBatteries() != null ? space.getBatteries().size() : 0;
    }
}
