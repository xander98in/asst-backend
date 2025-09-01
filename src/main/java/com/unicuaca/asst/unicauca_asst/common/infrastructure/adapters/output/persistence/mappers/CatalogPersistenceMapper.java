package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.Mapper;

import com.unicuaca.asst.unicauca_asst.common.domain.models.CivilStatus;
import com.unicuaca.asst.unicauca_asst.common.domain.models.ContractType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.EducationLevel;
import com.unicuaca.asst.unicauca_asst.common.domain.models.Gender;
import com.unicuaca.asst.unicauca_asst.common.domain.models.HousingType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.JobPositionType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.SalaryType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.SocioeconomicLevel;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.CivilStatusEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.ContractTypeEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.EducationLevelEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.GenderEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.HousingTypeEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.IdentificationTypeEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.JobPositionTypeEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.SalaryTypeEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.SocioeconomicLevelEntity;

@Mapper(componentModel = "spring")
public interface CatalogPersistenceMapper {

    /**
     * Convierte un objeto IdentificationType en un objeto IdentificationTypeEntity.
     *
     * @param identificationType el objeto IdentificationType a convertir
     * @return el objeto IdentificationTypeEntity resultante
     */
    IdentificationTypeEntity toIdentificationTypeEntity(IdentificationType identificationType);

    /**
     * Convierte un objeto IdentificationTypeEntity en un objeto IdentificationType.
     *
     * @param identificationTypeEntity el objeto IdentificationTypeEntity a convertir
     * @return el objeto IdentificationType resultante
     */
    IdentificationType toIdentificationTypeDomain(IdentificationTypeEntity identificationTypeEntity);

    /**
     * Convierte un objeto CivilStatus en un objeto CivilStatusEntity.
     *
     * @param civilStatus el objeto CivilStatus a convertir
     * @return el objeto CivilStatusEntity resultante
     */
    CivilStatusEntity toCivilStatusEntity(CivilStatus civilStatus);

    /**
     * Convierte un objeto CivilStatusEntity en un objeto CivilStatus.
     *
     * @param civilStatusEntity el objeto CivilStatusEntity a convertir
     * @return el objeto CivilStatus resultante
     */
    CivilStatus toCivilStatusDomain(CivilStatusEntity civilStatusEntity);   

    /**
     * Convierte un objeto EducationLevel en un objeto EducationLevelEntity.
     *
     * @param educationLevel el objeto EducationLevel a convertir
     * @return el objeto EducationLevelEntity resultante
     */
    EducationLevelEntity toEducationLevelEntity(EducationLevel educationLevel);

    /**
     * Convierte un objeto EducationLevelEntity en un objeto EducationLevel.
     *
     * @param educationLevelEntity el objeto EducationLevelEntity a convertir
     * @return el objeto EducationLevel resultante
     */
    EducationLevel toEducationLevelDomain(EducationLevelEntity educationLevelEntity);

    /**
     * Convierte un objeto HousingTypeEntity en un objeto HousingType.
     *
     * @param housingTypeEntity el objeto HousingTypeEntity a convertir
     * @return el objeto HousingType resultante
     */
    HousingType toHousingTypeDomain(HousingTypeEntity housingTypeEntity);

    /**
     * Convierte un objeto HousingType en un objeto HousingTypeEntity.
     *
     * @param housingType el objeto HousingType a convertir
     * @return el objeto HousingTypeEntity resultante
     */
    HousingTypeEntity toHousingTypeEntity(HousingType housingType);

    /**
     * Convierte un objeto SocioeconomicLevelEntity en un objeto SocioeconomicLevel.
     *
     * @param socioeconomicLevelEntity el objeto SocioeconomicLevelEntity a convertir
     * @return el objeto SocioeconomicLevel resultante
     */
    SocioeconomicLevel toSocioeconomicLevelDomain(SocioeconomicLevelEntity socioeconomicLevelEntity);

    /**
     * Convierte un objeto SocioeconomicLevel en un objeto SocioeconomicLevelEntity.
     *
     * @param socioeconomicLevel el objeto SocioeconomicLevel a convertir
     * @return el objeto SocioeconomicLevelEntity resultante
     */
    SocioeconomicLevelEntity toSocioeconomicLevelEntity(SocioeconomicLevel socioeconomicLevel);

    /**
     * Convierte un objeto JobPositionTypeEntity en un objeto JobPositionType.
     *
     * @param jobPositionTypeEntity el objeto JobPositionTypeEntity a convertir
     * @return el objeto JobPositionType resultante
     */
    JobPositionType toJobPositionTypeDomain(JobPositionTypeEntity jobPositionTypeEntity);

    /**
     * Convierte un objeto JobPositionType en un objeto JobPositionTypeEntity.
     *
     * @param jobPositionType el objeto JobPositionType a convertir
     * @return el objeto JobPositionTypeEntity resultante
     */
    JobPositionTypeEntity toJobPositionTypeEntity(JobPositionType jobPositionType);

    /**
     * Convierte un objeto ContractTypeEntity en un objeto ContractType.
     *
     * @param contractTypeEntity el objeto ContractTypeEntity a convertir
     * @return el objeto ContractType resultante
     */
    ContractType toContractTypeDomain(ContractTypeEntity contractTypeEntity);

    /**
     * Convierte un objeto ContractType en un objeto ContractTypeEntity.
     *
     * @param contractType el objeto ContractType a convertir
     * @return el objeto ContractTypeEntity resultante
     */
    ContractTypeEntity toContractTypeEntity(ContractType contractType);

    /**
     * Convierte un objeto SalaryTypeEntity en un objeto SalaryType.
     *
     * @param salaryTypeEntity el objeto SalaryTypeEntity a convertir
     * @return el objeto SalaryType resultante
     */
    SalaryType toSalaryTypeDomain(SalaryTypeEntity salaryTypeEntity);

    /**
     * Convierte un objeto SalaryType en un objeto SalaryTypeEntity.
     *
     * @param salaryType el objeto SalaryType a convertir
     * @return el objeto SalaryTypeEntity resultante
     */
    SalaryTypeEntity toSalaryTypeEntity(SalaryType salaryType);

    /**
     * Convierte un objeto GenderEntity en un objeto Gender.
     *
     * @param genderEntity el objeto GenderEntity a convertir
     * @return el objeto Gender resultante
     */
    Gender toGenderDomain(GenderEntity genderEntity);

    /**
     * Convierte un objeto Gender en un objeto GenderEntity.
     *
     * @param gender el objeto Gender a convertir
     * @return el objeto GenderEntity resultante
     */
    GenderEntity toGenderEntity(Gender gender);
}
