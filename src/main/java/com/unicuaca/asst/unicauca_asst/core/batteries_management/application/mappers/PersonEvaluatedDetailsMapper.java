package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedDetailsMetaResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedDetailsResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.City;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.CivilStatus;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.ContractType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.EducationLevel;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.Gender;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.HousingType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.JobPositionType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.SalaryType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.SocioeconomicLevel;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;


/**
 * Mapper de la capa de aplicación para convertir entre el modelo de dominio
 * {@link PersonEvaluatedDetails} y sus DTOs de request/response.
 *
 * <p>Utiliza MapStruct para mapear automáticamente los campos, incluyendo
 * atributos anidados como género, estado civil, nivel educativo, ciudad, tipo de cargo,
 * tipo de contrato y tipo de salario, construyendo objetos de solo-ID a partir de los
 * identificadores recibidos en los DTOs de entrada.</p>
 */
@Mapper(componentModel = "spring")
public interface PersonEvaluatedDetailsMapper {

    /**
     * Convierte un modelo de dominio {@link PersonEvaluatedDetails} a un DTO de respuesta completo.
     *
     * @param details modelo de dominio
     * @return DTO de respuesta {@link PersonEvaluatedDetailsResponseDTO}
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "genderId", source = "gender.id")
    @Mapping(target = "genderName", source = "gender.name")
    @Mapping(target = "civilStatusId", source = "civilStatus.id")
    @Mapping(target = "civilStatusName", source = "civilStatus.name")
    @Mapping(target = "educationLevelId", source = "educationLevel.id")
    @Mapping(target = "educationLevelName", source = "educationLevel.name")
    @Mapping(target = "profession", source = "profession")
    @Mapping(target = "residenceCityId", source = "residenceCity.id")
    @Mapping(target = "residenceCityCode", source = "residenceCity.code")
    @Mapping(target = "residenceCityName", source = "residenceCity.name")
    @Mapping(target = "residenceDepartmentId", source = "residenceCity.department.id")
    @Mapping(target = "residenceDepartmentCode", source = "residenceCity.department.code")
    @Mapping(target = "residenceDepartmentName", source = "residenceCity.department.name")
    @Mapping(target = "socioeconomicLevelId", source = "socioeconomicLevel.id")
    @Mapping(target = "socioeconomicLevelName", source = "socioeconomicLevel.name")
    @Mapping(target = "housingTypeId", source = "housingType.id")
    @Mapping(target = "housingTypeName", source = "housingType.name")
    @Mapping(target = "dependentsCount", source = "dependentsCount")
    @Mapping(target = "workCityId", source = "workCity.id")
    @Mapping(target = "workCityCode", source = "workCity.code")
    @Mapping(target = "workCityName", source = "workCity.name")
    @Mapping(target = "workDepartmentId", source = "workCity.department.id")
    @Mapping(target = "workDepartmentCode", source = "workCity.department.code")
    @Mapping(target = "workDepartmentName", source = "workCity.department.name")
    @Mapping(target = "yearsAtCompany", source = "yearsAtCompany")
    @Mapping(target = "jobTitle", source = "jobTitle")
    @Mapping(target = "jobPositionId", source = "jobPositionType.id")
    @Mapping(target = "jobPositionName", source = "jobPositionType.name")
    @Mapping(target = "yearsInPosition", source = "yearsInPosition")
    @Mapping(target = "workAreaName", source = "workAreaName")
    @Mapping(target = "contractTypeId", source = "contractType.id")
    @Mapping(target = "contractTypeName", source = "contractType.name")
    @Mapping(target = "dailyWorkHours", source = "dailyWorkHours")
    @Mapping(target = "salaryTypeId", source = "salaryType.id")
    @Mapping(target = "salaryTypeName", source = "salaryType.name")
    PersonEvaluatedDetailsResponseDTO toResponseDTO(PersonEvaluatedDetails details);

    /**
     * Convierte un objeto PersonEvaluatedDetails a su representación DTO de metadatos.
     *
     * @param details el objeto PersonEvaluatedDetails a convertir
     * @return el DTO de metadatos correspondiente
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "batteryManagementRecordId", source = "batteryManagementRecord.id")
    @Mapping(target = "jobPositionTypeId", source = "jobPositionType.id")
    @Mapping(target = "jobPositionTypeName", source = "jobPositionType.name")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    PersonEvaluatedDetailsMetaResponseDTO toMetaResponseDTO(PersonEvaluatedDetails details);

    /**
     * Convierte un DTO de creación {@link PersonEvaluatedDetailsCreateRequestDTO}
     * en un modelo de dominio {@link PersonEvaluatedDetails}.
     *
     * <p>Construye objetos anidados (género, ciudad, tipo de cargo, etc.) únicamente con su ID
     * a partir de los identificadores recibidos, ignorando los campos auditables.</p>
     *
     * @param dto DTO con los datos de creación
     * @return modelo de dominio listo para persistirse
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "batteryManagementRecordId", target = "batteryManagementRecord")
    @Mapping(source = "genderId", target = "gender")
    @Mapping(source = "civilStatusId", target = "civilStatus")
    @Mapping(source = "educationLevelId", target = "educationLevel")
    @Mapping(source = "profession", target = "profession")
    @Mapping(source = "residenceCityId", target = "residenceCity")
    @Mapping(source = "socioeconomicLevelId", target = "socioeconomicLevel")
    @Mapping(source = "housingTypeId", target = "housingType")
    @Mapping(source = "dependentsCount", target = "dependentsCount")
    @Mapping(source = "workCityId", target = "workCity")
    @Mapping(source = "yearsAtCompany", target = "yearsAtCompany")
    @Mapping(source = "jobTitle", target = "jobTitle")
    @Mapping(source = "jobPositionTypeId", target = "jobPositionType")
    @Mapping(source = "yearsInPosition", target = "yearsInPosition")
    @Mapping(source = "workAreaName", target = "workAreaName")
    @Mapping(source = "contractTypeId", target = "contractType")
    @Mapping(source = "dailyWorkHours", target = "dailyWorkHours")
    @Mapping(source = "salaryTypeId", target = "salaryType")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PersonEvaluatedDetails toDomain(PersonEvaluatedDetailsCreateRequestDTO dto);

    /**
     * Convierte un DTO de actualización {@link PersonEvaluatedDetailsUpdateRequestDTO}
     * en un modelo de dominio {@link PersonEvaluatedDetails}.
     *
     * <p>Construye objetos anidados únicamente con su ID a partir de los identificadores recibidos.
     * Ignora el ID de la entidad, la referencia al registro de batería y los campos auditables,
     * ya que estos se resuelven y completan en la capa de servicio durante la actualización.</p>
     *
     * @param dto DTO con los datos de actualización
     * @return modelo de dominio con los campos a actualizar
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "genderId", target = "gender")
    @Mapping(source = "civilStatusId", target = "civilStatus")
    @Mapping(source = "educationLevelId", target = "educationLevel")
    @Mapping(source = "profession", target = "profession")
    @Mapping(source = "residenceCityId", target = "residenceCity")
    @Mapping(source = "socioeconomicLevelId", target = "socioeconomicLevel")
    @Mapping(source = "housingTypeId", target = "housingType")
    @Mapping(source = "dependentsCount", target = "dependentsCount")
    @Mapping(source = "workCityId", target = "workCity")
    @Mapping(source = "yearsAtCompany", target = "yearsAtCompany")
    @Mapping(source = "jobTitle", target = "jobTitle")
    @Mapping(source = "jobPositionTypeId", target = "jobPositionType")
    @Mapping(source = "yearsInPosition", target = "yearsInPosition")
    @Mapping(source = "workAreaName", target = "workAreaName")
    @Mapping(source = "contractTypeId", target = "contractType")
    @Mapping(source = "dailyWorkHours", target = "dailyWorkHours")
    @Mapping(source = "salaryTypeId", target = "salaryType")
    @Mapping(target = "batteryManagementRecord", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PersonEvaluatedDetails toDomain(PersonEvaluatedDetailsUpdateRequestDTO dto);

    // ---------- Helpers (MapStruct los usa por firma) ----------
    default BatteryManagementRecord mapBattery(Long id) {
        return id == null ? null : new BatteryManagementRecord(id, null, null, null, null);
    }

    default Gender mapGender(Long id) {
        return id == null ? null : new Gender(id, null);
    }

    default CivilStatus mapCivilStatus(Long id) {
        return id == null ? null : new CivilStatus(id, null);
    }

    default EducationLevel mapEducationLevel(Long id) {
        return id == null ? null : new EducationLevel(id, null);
    }

    default City mapCity(Long id) {
        return id == null ? null : new City(id, null, null, null);
    }

    default SocioeconomicLevel mapSocioeconomicLevel(Long id) {
        return id == null ? null : new SocioeconomicLevel(id, null);
    }

    default HousingType mapHousingType(Long id) {
        return id == null ? null : new HousingType(id, null);
    }

    default JobPositionType mapJobPositionType(Long id) {
        return id == null ? null : new JobPositionType(id, null);
    }

    default ContractType mapContractType(Long id) {
        return id == null ? null : new ContractType(id, null);
    }

    default SalaryType mapSalaryType(Long id) {
        return id == null ? null : new SalaryType(id, null);
    }
}
  