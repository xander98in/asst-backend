package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.mappers.CatalogPersistenceMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEvaluatedDetailsEntity;

/**
 * Mapper para convertir entre la entidad de persistencia
 * y el modelo de dominio de los detalles de una persona evaluada (PersonEvaluatedDetails).
 *
 * <p>Utiliza MapStruct para generar automáticamente las implementaciones
 * de los métodos de mapeo, facilitando la conversión bidireccional
 * entre las representaciones de datos en la base de datos y en el dominio.</p>
 */
@Mapper(
    componentModel = "spring",
    uses = {
        BatteryManagementRecordPersistenceMapper.class,
        CatalogPersistenceMapper.class
    }
)
public interface PersonEvaluatedDetailsPersistenceMapper {

    /**
     * Convierte una entidad de persistencia a un modelo de dominio.
     * 
     * @param entity la entidad de persistencia
     * @return el modelo de dominio correspondiente
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "batteryManagementRecord", source = "batteryManagementRecord")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "civilStatus", source = "civilStatus")
    @Mapping(target = "educationLevel", source = "educationLevel")
    @Mapping(target = "profession", source = "profession")
    @Mapping(target = "residenceCity", source = "residenceCity", qualifiedByName = "toCityDomainWithDepartment")
    @Mapping(target = "socioeconomicLevel", source = "socioeconomicLevel")
    @Mapping(target = "housingType", source = "housingType")
    @Mapping(target = "dependentsCount", source = "dependentsCount")
    @Mapping(target = "workCity", source = "workCity", qualifiedByName = "toCityDomainWithDepartment")
    @Mapping(target = "yearsAtCompany", source = "yearsAtCompany")
    @Mapping(target = "jobTitle", source = "jobTitle")
    @Mapping(target = "jobPositionType", source = "jobPositionType")
    @Mapping(target = "yearsInPosition", source = "yearsInPosition")
    @Mapping(target = "workAreaName", source = "workAreaName")
    @Mapping(target = "contractType", source = "contractType")
    @Mapping(target = "dailyWorkHours", source = "dailyWorkHours")
    @Mapping(target = "salaryType", source = "salaryType")
    PersonEvaluatedDetails toDomain(PersonEvaluatedDetailsEntity entity);

    /**
     * Convierte un modelo de dominio a una entidad de persistencia.
     * 
     * @param domain el modelo de dominio
     * @return la entidad de persistencia correspondiente
     */
    @InheritInverseConfiguration
    @Mapping(target = "residenceCity", source = "residenceCity", qualifiedByName = "toCityEntityWithDepartment")
    @Mapping(target = "workCity", source = "workCity", qualifiedByName = "toCityEntityWithDepartment")
    PersonEvaluatedDetailsEntity toEntity(PersonEvaluatedDetails domain);
}
