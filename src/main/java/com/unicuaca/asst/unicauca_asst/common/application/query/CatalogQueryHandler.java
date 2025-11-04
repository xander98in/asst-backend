package com.unicuaca.asst.unicauca_asst.common.application.query;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.*;

public interface CatalogQueryHandler {

    /**
     * Consulta los tipos de identificación disponibles en el sistema.
     *
     * @return una lista de {@link IdentificationTypeResponseDTO} representando los tipos de identificación.
     */
    List<IdentificationTypeResponseDTO> getIdTypes();

    /**
     * Consulta los estados civiles disponibles en el sistema.
     *
     * @return una lista de {@link CivilStatusResponseDTO} representando los estados civiles.
     */
    List<CivilStatusResponseDTO> getCivilStatuses();

    /**
     * Consulta los niveles de educación disponibles en el sistema.
     *
     * @return una lista de {@link EducationLevelResponseDTO} representando los niveles de educación.
     */
    List<EducationLevelResponseDTO> getEducationLevels();

    /**
     * Consulta los tipos de vivienda disponibles en el sistema.
     *
     * @return una lista de {@link HousingTypeResponseDTO} representando los tipos de vivienda.
     */
    List<HousingTypeResponseDTO> getHousingTypes();

    /**
     * Consulta los niveles socioeconómicos disponibles en el sistema.
     *
     * @return una lista de {@link SocioeconomicLevelResponseDTO} representando los niveles socioeconómicos.
     */
    List<SocioeconomicLevelResponseDTO> getSocioeconomicLevels();

    /**
     * Consulta los tipos de cargo disponibles en el sistema.
     *
     * @return una lista de {@link JobPositionTypeResponseDTO} representando los tipos de cargo.
     */
    List<JobPositionTypeResponseDTO> getJobPositionTypes();

    /**
     * Consulta los tipos de contrato disponibles en el sistema.
     *
     * @return una lista de {@link ContractTypeResponseDTO} representando los tipos de contrato.
     */
    List<ContractTypeResponseDTO> getContractTypes();

    /**
     * Consulta los tipos de salario disponibles en el sistema.
     *
     * @return una lista de {@link SalaryTypeResponseDTO} representando los tipos de salario.
     */
    List<SalaryTypeResponseDTO> getSalaryTypes();

    /**
     * Consulta los géneros disponibles en el sistema.
     *
     * @return una lista de {@link GenderResponseDTO} representando los géneros.
     */
    List<GenderResponseDTO> getGenders();

    /**
     * Devuelve la ciudad (por código) con su departamento (sin ciudades).
     *
     * @param code código de la ciudad
     * @return la ciudad con su departamento
     */
    CityResponseDTO getCityByCodeWithDepartment(String code);

    /**
     * Devuelve la ciudad (por nombre) con su departamento (sin ciudades).
     *
     * @param name nombre de la ciudad
     * @return la ciudad con su departamento
     */
    CityResponseDTO getCityByNameWithDepartment(String name);

    /**
     * Devuelve el departamento (por código) con sus ciudades (cada ciudad sin department).
     *
     * @param code código del departamento
     * @return el departamento con sus ciudades
     */
    DepartmentResponseDTO getDepartmentByCodeWithCities(String code);

    /**
     * Devuelve el departamento (por nombre) con sus ciudades (cada ciudad sin department).
     *
     * @param name nombre del departamento
     * @return el departamento con sus ciudades
     */
    DepartmentResponseDTO getDepartmentByNameWithCities(String name);

    /**
     * Lista todos los departamentos (sin incluir sus ciudades).
     *
     * @return lista de departamentos
     */
    List<DepartmentResponseDTO> getAllDepartments();

    /**
     * Lista todos los departamentos con sus ciudades
     * (cada City sin su Department).
     *
     * @return lista de departamentos con ciudades
     */
    List<DepartmentResponseDTO> getAllDepartmentsWithCities();

    /**
     * Lista todas las ciudades (sin incluir su Department).
     * Devuelve el DTO resumen de ciudad.
     *
     * @return lista de ciudades
     */
    List<CitySummaryResponseDTO> getAllCities();

    /**
     * Lista todas las ciudades con su Department
     * (Department en versión resumen, sin cities).
     *
     * @return lista de ciudades con departamento
     */
    List<CityResponseDTO> getAllCitiesWithDepartment();

}
