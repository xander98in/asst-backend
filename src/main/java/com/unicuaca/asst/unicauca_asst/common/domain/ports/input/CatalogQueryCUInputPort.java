package com.unicuaca.asst.unicauca_asst.common.domain.ports.input;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.domain.models.*;

public interface CatalogQueryCUInputPort {

    /**
     * Obtiene una lista de todos los tipos de identificación.
     *
     * @return una lista de objetos IdentificationType
     */
    List<IdentificationType> getIdTypes();

    /**
     * Obtiene una lista de todos los estados civiles.
     *
     * @return una lista de objetos CivilStatus
     */
    List<CivilStatus> getCivilStatuses();

    /**
     * Obtiene una lista de todos los niveles educativos.
     *
     * @return una lista de objetos EducationLevel
     */
    List<EducationLevel> getEducationLevels();

    /**
     * Obtiene una lista de todos los tipos de vivienda.
     *
     * @return una lista de objetos HousingType
     */
    List<HousingType> getHousingTypes();

    /**
     * Obtiene una lista de todos los niveles socioeconómicos.
     *
     * @return una lista de objetos SocioeconomicLevel
     */
    List<SocioeconomicLevel> getSocioeconomicLevels();

    /**
     * Obtiene una lista de todos los tipos de cargo.
     *
     * @return una lista de objetos JobPositionType
     */
    List<JobPositionType> getJobPositionTypes();

    /**
     * Obtiene una lista de todos los tipos de contrato.
     *
     * @return una lista de objetos ContractType
     */
    List<ContractType> getContractTypes();

    /**
     * Obtiene una lista de todos los tipos de salario.
     *
     * @return una lista de objetos SalaryType
     */
    List<SalaryType> getSalaryTypes();

    /**
     * Obtiene una lista de todos los géneros.
     *
     * @return una lista de objetos Gender
     */
    List<Gender> getGenders();

    /**
     * Obtiene una ciudad por su código incluyendo su departamento.
     * Lanza excepción si no existe.
     *
     * @param code código de la ciudad.
     * @return la ciudad con su departamento.
     */
    City getCityByCodeWithDepartment(String code);

    /**
     * Obtiene una ciudad por su nombre incluyendo su departamento.
     * Lanza excepción si no existe.
     *
     * @param name nombre de la ciudad.
     * @return la ciudad con su departamento.
     */
    City getCityByNameWithDepartment(String name);

    /**
     * Obtiene un departamento por su código incluyendo sus ciudades. (cada City no contiene department).
     * Lanza excepción si no existe.
     *
     * @param code código del departamento.
     * @return el departamento con sus ciudades.
     */
    Department getDepartmentByCodeWithCities(String code);

    /**
     * Obtiene un departamento por su nombre incluyendo sus ciudades. (cada City no contiene department).
     * Lanza excepción si no existe.
     *
     * @param name nombre del departamento.
     * @return el departamento con sus ciudades.
     */
    Department getDepartmentByNameWithCities(String name);

    /**
     * Lista todos los departamentos (sin incluir sus ciudades).
     *
     * @return lista de departamentos.
     */
    List<Department> getAllDepartments();

    /**
     * Lista todos los departamentos incluyendo sus ciudades
     * (cada City sin su Department para evitar ciclos).
     *
     * @return lista de departamentos con sus ciudades.
     */
    List<Department> getAllDepartmentsWithCities();

    /**
     * Lista todas las ciudades (sin incluir su Department).
     *
     * @return lista de ciudades.
     */
    List<City> getAllCities();

    /**
     * Lista todas las ciudades incluyendo su Department
     * (el Department no incluye sus cities para evitar ciclos).
     *
     * @return lista de ciudades con su departamento.
     */
    List<City> getAllCitiesWithDepartment();
}
