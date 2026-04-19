package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpaceBattery;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.AnalysisSpaceBatteryEntity;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.AnalysisSpaceEntity;

/**
 * Mapper MapStruct para la conversión entre entidades JPA y modelos de dominio
 * de espacios de análisis.
 */
@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface AnalysisSpacePersistenceMapper {

    /**
     * Convierte una entidad JPA de espacio de análisis a modelo de dominio.
     * MapStruct usa automáticamente {@link #toBatteryDomain} para mapear la lista de baterías.
     *
     * @param entity entidad JPA a convertir
     * @return modelo de dominio equivalente
     */
    @Mapping(source = "evaluator.id", target = "evaluatorId")
    AnalysisSpace toDomain(AnalysisSpaceEntity entity);

    /**
     * Convierte un modelo de dominio a entidad JPA.
     * Las baterías se gestionan por separado (addBatteryToSpace/removeBatteryFromSpace).
     * El evaluador se asigna en el repositorio mediante getReferenceById.
     *
     * @param domain modelo de dominio a convertir
     * @return entidad JPA equivalente sin baterías ni evaluador asignados
     */
    @Mapping(target = "batteries", ignore = true)
    @Mapping(target = "evaluator", ignore = true)
    AnalysisSpaceEntity toEntity(AnalysisSpace domain);

    /**
     * Convierte una entidad de relación espacio-batería a modelo de dominio.
     *
     * @param entity entidad de relación espacio-batería a convertir
     * @return modelo de dominio equivalente con el ID del espacio asociado
     */
    @Mapping(source = "analysisSpace.id", target = "analysisSpaceId")
    AnalysisSpaceBattery toBatteryDomain(AnalysisSpaceBatteryEntity entity);
}
