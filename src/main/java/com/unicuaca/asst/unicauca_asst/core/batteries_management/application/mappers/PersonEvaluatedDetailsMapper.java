package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicuaca.asst.unicauca_asst.common.domain.models.City;
import com.unicuaca.asst.unicauca_asst.common.domain.models.CivilStatus;
import com.unicuaca.asst.unicauca_asst.common.domain.models.ContractType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.EducationLevel;
import com.unicuaca.asst.unicauca_asst.common.domain.models.Gender;
import com.unicuaca.asst.unicauca_asst.common.domain.models.HousingType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.JobPositionType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.SalaryType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.SocioeconomicLevel;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;


@Mapper(componentModel = "spring")
public interface PersonEvaluatedDetailsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "batteryManagementRecordId", target = "batteryManagementRecord")
    @Mapping(target = "personEvaluated", ignore = true)
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
    PersonEvaluatedDetails toDomain(PersonEvaluatedDetailsCreateRequestDTO dto);

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
  