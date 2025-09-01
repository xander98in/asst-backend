package com.unicuaca.asst.unicauca_asst.common.application.mappers;

import org.mapstruct.Mapper;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.CivilStatusResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.ContractTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.EducationLevelResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.GenderResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.HousingTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.IdentificationTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.JobPositionTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.SalaryTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.SocioeconomicLevelResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.domain.models.CivilStatus;
import com.unicuaca.asst.unicauca_asst.common.domain.models.ContractType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.EducationLevel;
import com.unicuaca.asst.unicauca_asst.common.domain.models.Gender;
import com.unicuaca.asst.unicauca_asst.common.domain.models.HousingType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.JobPositionType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.SalaryType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.SocioeconomicLevel;

/**
 * Mapper para convertir entre objetos de dominio y DTOs.
 */
@Mapper(componentModel = "spring")
public interface CatalogMapper {

    /**
     * Convierte un objeto IdentificationType en un objeto IdentificationTypeResponseDTO.
     *
     * @param identificationType el objeto IdentificationType a convertir
     * @return el objeto IdentificationTypeResponseDTO resultante
     */
    IdentificationTypeResponseDTO toIdentificationTypeResponseDTO(IdentificationType identificationType);

    /**
     * Convierte un objeto CivilStatus en un objeto CivilStatusResponseDTO.
     *
     * @param civilStatus el objeto CivilStatus a convertir
     * @return el objeto CivilStatusResponseDTO resultante
     */
    CivilStatusResponseDTO toCivilStatusResponseDTO(CivilStatus civilStatus);

    /**
     * Convierte un objeto EducationLevel en un objeto EducationLevelResponseDTO.
     *
     * @param educationLevel el objeto EducationLevel a convertir
     * @return el objeto EducationLevelResponseDTO resultante
     */
    EducationLevelResponseDTO toEducationLevelResponseDTO(EducationLevel educationLevel);

    /**
     * Convierte un objeto HousingType en un objeto HousingTypeResponseDTO.
     *
     * @param housingType el objeto HousingType a convertir
     * @return el objeto HousingTypeResponseDTO resultante
     */
    HousingTypeResponseDTO toHousingTypeResponseDTO(HousingType housingType);

    /**
     * Convierte un objeto SocioeconomicLevel en un objeto SocioeconomicLevelResponseDTO.
     *
     * @param socioeconomicLevel el objeto SocioeconomicLevel a convertir
     * @return el objeto SocioeconomicLevelResponseDTO resultante
     */
    SocioeconomicLevelResponseDTO toSocioeconomicLevelResponseDTO(SocioeconomicLevel socioeconomicLevel);

    /**
     * Convierte un objeto JobPositionType en un objeto JobPositionTypeResponseDTO.
     *
     * @param jobPositionType el objeto JobPositionType a convertir
     * @return el objeto JobPositionTypeResponseDTO resultante
     */
    JobPositionTypeResponseDTO toJobPositionTypeResponseDTO(JobPositionType jobPositionType);

    /**
     * Convierte un objeto ContractType en un objeto ContractTypeResponseDTO.
     *
     * @param contractType el objeto ContractType a convertir
     * @return el objeto ContractTypeResponseDTO resultante
     */
    ContractTypeResponseDTO toContractTypeResponseDTO(ContractType contractType);

    /**
     * Convierte un objeto SalaryType en un objeto SalaryTypeResponseDTO.
     *
     * @param salaryType el objeto SalaryType a convertir
     * @return el objeto SalaryTypeResponseDTO resultante
     */
    SalaryTypeResponseDTO toSalaryTypeResponseDTO(SalaryType salaryType);

    /**
     * Convierte un objeto Gender en un objeto GenderResponseDTO.
     *
     * @param gender el objeto Gender a convertir
     * @return el objeto GenderResponseDTO resultante
     */
    GenderResponseDTO toGenderResponseDTO(Gender gender);
}
