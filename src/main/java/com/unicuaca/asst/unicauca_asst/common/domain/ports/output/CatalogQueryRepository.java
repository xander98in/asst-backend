package com.unicuaca.asst.unicauca_asst.common.domain.ports.output;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.common.domain.models.*;

/**
 * Puerto de salida para las consultas de catálogos.
 *
 * Define las firmas de los métodos que deben implementar los adaptadores de infraestructura
 * encargados de recuperar la información desde fuentes externas, como bases de datos relacionales.
 */
public interface CatalogQueryRepository {

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
     * Obtiene un estado civil por su ID.
     *
     * @param id el ID del estado civil
     * @return un {@link Optional} con el estado civil correspondiente, o vacío si no se encuentra
     */
    Optional<CivilStatus> getCivilStatusById(Long id);

    /**
     * Obtiene una lista de todos los niveles educativos.
     *
     * @return una lista de objetos EducationLevel
     */
    List<EducationLevel> getEducationLevels();

    /**
     * Obtiene un nivel educativo por su ID.
     *
     * @param id el ID del nivel educativo
     * @return un {@link Optional} con el nivel educativo correspondiente, o vacío si no se encuentra
     */
    Optional<EducationLevel> getEducationLevelById(Long id);

    /**
     * Obtiene una lista de todos los tipos de vivienda.
     *
     * @return una lista de objetos HousingType
     */
    List<HousingType> getHousingTypes();

    /**
     * Obtiene un tipo de vivienda por su ID.
     *
     * @param id el ID del tipo de vivienda
     * @return un {@link Optional} con el tipo de vivienda correspondiente, o vacío si no se encuentra
     */
    Optional<HousingType> getHousingTypeById(Long id);

    /**
     * Obtiene una lista de todos los niveles socioeconómicos.
     *
     * @return una lista de objetos SocioeconomicLevel
     */
    List<SocioeconomicLevel> getSocioeconomicLevels();

    /**
     * Obtiene un nivel socioeconómico por su ID.
     *
     * @param id el ID del nivel socioeconómico
     * @return un {@link Optional} con el nivel socioeconómico correspondiente, o vacío si no se encuentra
     */
    Optional<SocioeconomicLevel> getSocioeconomicLevelById(Long id);

    /**
     * Obtiene una lista de todos los tipos de cargo.
     *
     * @return una lista de objetos JobPositionType
     */
    List<JobPositionType> getJobPositionTypes();

    /**
     * Obtiene un tipo de cargo por su ID.
     *
     * @param id el ID del tipo de cargo
     * @return un {@link Optional} con el tipo de cargo correspondiente, o vacío si no se encuentra
     */
    Optional<JobPositionType> getJobPositionTypeById(Long id);

    /**
     * Obtiene una lista de todos los tipos de contrato.
     *
     * @return una lista de objetos ContractType
     */
    List<ContractType> getContractTypes();

    /**
     * Obtiene un tipo de contrato por su ID.
     *
     * @param id el ID del tipo de contrato
     * @return un {@link Optional} con el tipo de contrato correspondiente, o vacío si no se encuentra
     */
    Optional<ContractType> getContractTypeById(Long id);

    /**
     * Obtiene una lista de todos los tipos de salario.
     *
     * @return una lista de objetos SalaryType
     */
    List<SalaryType> getSalaryTypes();

    /**
     * Obtiene un tipo de salario por su ID.
     *
     * @param id el ID del tipo de salario
     * @return un {@link Optional} con el tipo de salario correspondiente, o vacío si no se encuentra
     */
    Optional<SalaryType> getSalaryTypeById(Long id);

    /**
     * Obtiene una lista de todos los géneros.
     *
     * @return una lista de objetos Gender
     */
    List<Gender> getGenders();

    /**
     * Obtiene un género por su ID.
     *
     * @param id el ID del género
     * @return un {@link Optional} con el género correspondiente, o vacío si no se encuentra
     */
    Optional<Gender> getGenderById(Long id);

    /**
     * Obtiene un tipo de identificación mediante su abreviatura.
     *
     * @param abbreviation la abreviatura del tipo de identificación
     * @return un {@link Optional} con el tipo de identificación correspondiente, o vacío si no se encuentra
     */ 
    Optional<IdentificationType> getIdTypeByAbbreviation(String abbreviation);

    /**
     * Obtiene una ciudad por su código, incluyendo la información del departamento.
     *
     * @param code código de la ciudad
     * @return Optional con la ciudad (con department), o vacío si no existe
     */
    Optional<City> getCityByCodeWithDepartment(String code);

    /**
     * Obtiene una ciudad por su nombre, incluyendo la información del departamento.
     *
     * @param name nombre de la ciudad
     * @return Optional con la ciudad (con department), o vacío si no existe
     */
    Optional<City> getCityByNameWithDepartment(String name);

    /**
     * Obtiene una ciudad por su ID.
     *
     * @param id ID de la ciudad
     * @return Optional con la ciudad, o vacío si no existe
     */
    Optional<City> getCityById(Long id);

    /**
     * Obtiene un departamento por su código, incluyendo sus ciudades.
     * (Cada City no contiene department para evitar ciclos.)
     *
     * @param code código del departamento
     * @return Optional con el departamento (con cities), o vacío si no existe
     */
    Optional<Department> getDepartmentByCodeWithCities(String code);

    /**
     * Obtiene un departamento por su nombre, incluyendo sus ciudades.
     * (Cada City no contiene department para evitar ciclos.)
     *
     * @param name nombre del departamento
     * @return Optional con el departamento (con cities), o vacío si no existe
     */
    Optional<Department> getDepartmentByNameWithCities(String name);

    /**
     * Lista todos los departamentos SIN incluir sus ciudades.
     *
     * @return lista de departamentos
     */
    List<Department> getAllDepartments();

    /**
     * Lista todos los departamentos INCLUYENDO sus ciudades.
     * (Cada City no contiene su Department para evitar ciclos).
     *
     * @return lista de departamentos con sus ciudades
     */
    List<Department> getAllDepartmentsWithCities();

    /**
     * Lista todas las ciudades SIN incluir el departamento.
     *
     * @return lista de ciudades
     */
    List<City> getAllCities();

    /**
     * Lista todas las ciudades INCLUYENDO su departamento.
     * (El Department no incluye sus cities para evitar ciclos).
     *
     * @return lista de ciudades con su departamento
     */
    List<City> getAllCitiesWithDepartment();
}
