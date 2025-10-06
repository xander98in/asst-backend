package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.BatteryManagementRecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        PersonEvaluatedPersistenceMapper.class,
        BatteryManagementRecordStatusPersistenceMapper.class
    },
    builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface BatteryManagementRecordPersistenceMapper {

    /**
     * Convierte una entidad JPA a un modelo de dominio.
     *
     * @param entity la entidad JPA
     * @return el modelo de dominio
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "personEvaluated", source = "personEvaluated")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "status", source = "status")
    BatteryManagementRecord toDomain(BatteryManagementRecordEntity entity);

    /**
     * Convierte un modelo de dominio a una entidad JPA.
     *
     * @param domain el modelo de dominio
     * @return la entidad JPA
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "personEvaluated", source = "personEvaluated")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", source = "status")
    BatteryManagementRecordEntity toEntity(BatteryManagementRecord domain);
}
