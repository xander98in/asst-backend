package com.unicuaca.asst.unicauca_asst.common.application.mappers;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.*;
import com.unicuaca.asst.unicauca_asst.common.domain.models.*;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

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

    /**
     * Convierte un objeto City en un objeto CitySummaryResponseDTO.
     *
     * @param city el objeto City a convertir
     * @return el objeto CitySummaryResponseDTO resultante
     */
    @Named("toCitySummaryDTO")
    CitySummaryResponseDTO toCitySummaryDTO(City city);

    /**
     * Convierte un objeto Department en un objeto DepartmentSummaryResponseDTO.
     *
     * @param department el objeto Department a convertir
     * @return el objeto DepartmentSummaryResponseDTO resultante
     */
    @Named("toDepartmentSummaryDTO")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    DepartmentSummaryResponseDTO toDepartmentSummaryDTO(Department department);

    /**
     * Convierte un objeto City en un objeto CityResponseDTO.
     *
     * @param city el objeto City a convertir
     * @return el objeto CityResponseDTO resultante
     */
    @Mapping(target = "department", source = "department", qualifiedByName = "toDepartmentSummaryDTO")
    CityResponseDTO toCityResponseDTO(City city);

    /**
     * Convierte un objeto Department en un objeto DepartmentResponseDTO.
     * @param department el objeto Department a convertir
     * @return el objeto DepartmentResponseDTO resultante
     */
    @Mapping(target = "cities", source = "cities", qualifiedByName = "toCitySummaryList")
    DepartmentResponseDTO toDepartmentResponseDTO(Department department);

    /**
     * Convierte un conjunto de objetos City en una lista de CitySummaryResponseDTO.
     *
     * @param cities el conjunto de objetos City a convertir
     * @return la lista de CitySummaryResponseDTO resultante
     */
    @Named("toCitySummaryList")
    default List<CitySummaryResponseDTO> toCitySummaryList(Set<City> cities) {
        return cities == null ? List.of() : cities.stream()
            .map(this::toCitySummaryDTO)
            .toList();
    }
}
