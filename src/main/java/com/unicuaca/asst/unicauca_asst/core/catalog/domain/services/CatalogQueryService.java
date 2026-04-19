package com.unicuaca.asst.unicauca_asst.core.catalog.domain.services;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.*;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.ports.input.CatalogQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.exceptions.CatalogEmptyException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio que implementa los casos de uso de consulta de catálogos transversales.
 *
 * <p>Centraliza la lectura de catálogos maestros. Garantiza que los catálogos
 * consultados no estén vacíos y que las búsquedas puntuales retornen un resultado existente,
 * propagando excepciones tipadas vía {@link ResultFormatterOutputPort} cuando no se cumplen
 * estas condiciones.</p>
 */
@RequiredArgsConstructor
public class CatalogQueryService implements CatalogQueryCUInputPort {

    private final CatalogQueryRepository catalogQueryRepository;
    private final ResultFormatterOutputPort resultFormatterOutputPort;

    /**
     * Obtiene una lista de todos los tipos de identificación.
     *
     * @return una lista de objetos IdentificationType
     * @throws CatalogEmptyException si el catálogo está vacío o no se puede recuperar
     */
    @Override
    public List<IdentificationType> getIdTypes() {
        List<IdentificationType> list = catalogQueryRepository.getIdTypes();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY,
                "user.catalog.empty",
                "tipos de identificación"
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los estados civiles.
     *
     * @return una lista de objetos CivilStatus
     * @throws CatalogEmptyException si el catálogo está vacío o no se puede recuperar
     */
    @Override
    public List<CivilStatus> getCivilStatuses() {
        List<CivilStatus> list = catalogQueryRepository.getCivilStatuses();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY,
                "user.catalog.empty",
                "estados civiles"
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los niveles educativos.
     *
     * @return una lista de objetos EducationLevel
     * @throws CatalogEmptyException si el catálogo está vacío o no se puede recuperar
     */
    @Override
    public List<EducationLevel> getEducationLevels() {
        List<EducationLevel> list = catalogQueryRepository.getEducationLevels();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY,
                "user.catalog.empty",
                "niveles educativos"
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los tipos de vivienda.
     *
     * @return una lista de objetos HousingType
     * @throws CatalogEmptyException si el catálogo está vacío o no se puede recuperar
     */
    @Override
    public List<HousingType> getHousingTypes() {
        List<HousingType> list = catalogQueryRepository.getHousingTypes();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY,
                "user.catalog.empty",
                "tipos de vivienda"
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los niveles socioeconómicos.
     *
     * @return una lista de objetos SocioeconomicLevel
     * @throws CatalogEmptyException si el catálogo está vacío o no se puede recuperar
     */
    @Override
    public List<SocioeconomicLevel> getSocioeconomicLevels() {
        List<SocioeconomicLevel> list = catalogQueryRepository.getSocioeconomicLevels();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY,
                "user.catalog.empty",
                "niveles socioeconómicos"
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los tipos de cargo.
     *
     * @return una lista de objetos JobPositionType
     * @throws CatalogEmptyException si el catálogo está vacío o no se puede recuperar
     */
    @Override
    public List<JobPositionType> getJobPositionTypes() {
        List<JobPositionType> list = catalogQueryRepository.getJobPositionTypes();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY,
                "user.catalog.empty",
                "tipos de cargo"
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los tipos de contrato.
     *
     * @return una lista de objetos ContractType
     * @throws CatalogEmptyException si el catálogo está vacío o no se puede recuperar
     */
    @Override
    public List<ContractType> getContractTypes() {
        List<ContractType> list = catalogQueryRepository.getContractTypes();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY,
                "user.catalog.empty",
                "tipos de contrato"
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los tipos de salario.
     *
     * @return una lista de objetos SalaryType
     * @throws CatalogEmptyException si el catálogo está vacío o no se puede recuperar
     */
    @Override
    public List<SalaryType> getSalaryTypes() {
        List<SalaryType> list = catalogQueryRepository.getSalaryTypes();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY,
                "user.catalog.empty",
                "tipos de salario"
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los géneros.
     *
     * @return una lista de objetos Gender
     * @throws CatalogEmptyException si el catálogo está vacío o no se puede recuperar
     */
    @Override
    public List<Gender> getGenders() {
        List<Gender> list = catalogQueryRepository.getGenders();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY,
                "user.catalog.empty",
                "géneros"
            );
        }
        return list;
    }

    /**
     * Obtiene una ciudad por su código incluyendo su departamento.
     *
     * @param code código de la ciudad
     * @return la ciudad encontrada con su departamento
     * @throws EntityNotFoundPersException si no existe una ciudad con el código indicado
     */
    @Override
    public City getCityByCodeWithDepartment(String code) {
        Optional<City> cityOpt = catalogQueryRepository.getCityByCodeWithDepartment(code);
        if (cityOpt.isEmpty()) {
            resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.ENTITY_NOT_FOUND,
                "user.catalog.city_not_found",
                "código " + code
            );
        }
        return cityOpt.get();
    }

    /**
     * Obtiene una ciudad por su nombre incluyendo su departamento.
     *
     * @param name nombre de la ciudad
     * @return la ciudad encontrada con su departamento
     * @throws EntityNotFoundPersException si no existe una ciudad con el nombre indicado
     */
    @Override
    public City getCityByNameWithDepartment(String name) {
        Optional<City> cityOpt = catalogQueryRepository.getCityByNameWithDepartment(name);
        if (cityOpt.isEmpty()) {
            resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.ENTITY_NOT_FOUND,
                "user.catalog.city_not_found",
                "nombre " + name
            );
        }
        return cityOpt.get();
    }

    /**
     * Obtiene un departamento por su código incluyendo sus ciudades.
     *
     * @param code código del departamento
     * @return el departamento encontrado con sus ciudades
     * @throws EntityNotFoundPersException si no existe un departamento con el código indicado
     */
    @Override
    public Department getDepartmentByCodeWithCities(String code) {
        Optional<Department> deptOpt = catalogQueryRepository.getDepartmentByCodeWithCities(code);
        if (deptOpt.isEmpty()) {
            resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.ENTITY_NOT_FOUND,
                "user.catalog.department_not_found",
                "código " + code
            );
        }
        return deptOpt.get();
    }

    /**
     * Obtiene un departamento por su nombre incluyendo sus ciudades.
     *
     * @param name nombre del departamento
     * @return el departamento encontrado con sus ciudades
     * @throws EntityNotFoundPersException si no existe un departamento con el nombre indicado
     */
    @Override
    public Department getDepartmentByNameWithCities(String name) {
        Optional<Department> deptOpt = catalogQueryRepository.getDepartmentByNameWithCities(name);
        if (deptOpt.isEmpty()) {
            resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.ENTITY_NOT_FOUND,
                "user.catalog.department_not_found",
                "nombre " + name
            );
        }
        return deptOpt.get();
    }

    /**
     * Lista todos los departamentos (sin incluir sus ciudades).
     *
     * @return lista de departamentos.
     * @throws CatalogEmptyException si el catálogo está vacío o no se puede recuperar
     */
    @Override
    public List<Department> getAllDepartments() {
        List<Department> list = catalogQueryRepository.getAllDepartments();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY,
                "user.catalog.empty",
                "departamentos"
            );
        }
        return list;
    }

    /**
     * Lista todos los departamentos incluyendo sus ciudades
     * (cada City sin su Department para evitar ciclos).
     *
     * @return lista de departamentos con sus ciudades.
     * @throws CatalogEmptyException si el catálogo está vacío o no se puede recuperar
     */
    @Override
    public List<Department> getAllDepartmentsWithCities() {
        List<Department> list = catalogQueryRepository.getAllDepartmentsWithCities();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY,
                "user.catalog.empty",
                "departamentos"
            );
        }
        return list;
    }

    /**
     * Lista todas las ciudades (sin incluir su Department).
     *
     * @return lista de ciudades.
     * @throws CatalogEmptyException si el catálogo está vacío o no se puede recuperar
     */
    @Override
    public List<City> getAllCities() {
        List<City> list = catalogQueryRepository.getAllCities();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY,
                "user.catalog.empty",
                "ciudades"
            );
        }
        return list;
    }

    /**
     * Lista todas las ciudades INCLUYENDO su departamento.
     * (El Department no incluye sus cities para evitar ciclos).
     *
     * @return lista de ciudades con su departamento.
     * @throws CatalogEmptyException si el catálogo está vacío o no se puede recuperar
     */
    @Override
    public List<City> getAllCitiesWithDepartment() {
        List<City> list = catalogQueryRepository.getAllCitiesWithDepartment();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY,
                "user.catalog.empty",
                "ciudades"
            );
        }
        return list;
    }
}
