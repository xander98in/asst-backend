package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.BatteryManagementRecordStatusEntity;
import org.mapstruct.Mapper;

/**
 * Mapper MapStruct entre:
 * - BatteryManagementRecordStatusEntity (JPA/infraestructura)
 * - BatteryManagementRecordStatus (dominio)
 */
@Mapper(componentModel = "spring")
public interface BatteryManagementRecordStatusPersistenceMapper {

    /**
     * Convierte una entidad JPA a un modelo de dominio.
     *
     * @param entity la entidad JPA
     * @return el modelo de dominio
     */
    BatteryManagementRecordStatus toDomain(BatteryManagementRecordStatusEntity entity);

    /**
     * Convierte un modelo de dominio a una entidad JPA.
     *
     * @param domain el modelo de dominio
     * @return la entidad JPA
     */
    BatteryManagementRecordStatusEntity toEntity(BatteryManagementRecordStatus domain);
}
