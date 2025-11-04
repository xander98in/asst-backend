package com.unicuaca.asst.unicauca_asst.common.domain.services;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.domain.models.*;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.input.CatalogQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.exceptions.CatalogEmptyException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio de consulta de catálogos.
 *
 * Se encarga de manejar las solicitudes de consulta relacionadas con catálogos
 */
@RequiredArgsConstructor
public class CatalogQueryService implements CatalogQueryCUInputPort {

    private final CatalogQueryRepository catalogQueryRepository;
    private final ResultFormatterOutputPort resultFormatterOutputPort;

    /**
     * Obtiene una lista de todos los tipos de identificación.
     *
     * @return una lista de objetos IdentificationType
     */
    @Override
    public List<IdentificationType> getIdTypes() {
        List<IdentificationType> list = catalogQueryRepository.getIdTypes();
        if (list == null || list.isEmpty()) {
            this.resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY.getCode(),
                String.format(ErrorCode.CATALOG_EMPTY.getMessageKey(), "tipos de identificación")
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los estados civiles.
     *
     * @return una lista de objetos CivilStatus
     */
    @Override
    public List<CivilStatus> getCivilStatuses() {
        List<CivilStatus> list = catalogQueryRepository.getCivilStatuses();
        if (list == null || list.isEmpty()) {
            this.resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY.getCode(),
                String.format(ErrorCode.CATALOG_EMPTY.getMessageKey(), "estados civiles")
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los niveles educativos.
     *
     * @return una lista de objetos EducationLevel
     */
    @Override
    public List<EducationLevel> getEducationLevels() {
        List<EducationLevel> list = catalogQueryRepository.getEducationLevels();
        if (list == null || list.isEmpty()) {
            this.resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY.getCode(),
                String.format(ErrorCode.CATALOG_EMPTY.getMessageKey(), "niveles educativos")
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los tipos de vivienda.
     *
     * @return una lista de objetos HousingType
     */
    @Override
    public List<HousingType> getHousingTypes() {
        List<HousingType> list = catalogQueryRepository.getHousingTypes();
        if (list == null || list.isEmpty()) {
            this.resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY.getCode(),
                String.format(ErrorCode.CATALOG_EMPTY.getMessageKey(), "tipos de vivienda")
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los niveles socioeconómicos.
     *
     * @return una lista de objetos SocioeconomicLevel
     */
    @Override
    public List<SocioeconomicLevel> getSocioeconomicLevels() {
        List<SocioeconomicLevel> list = catalogQueryRepository.getSocioeconomicLevels();
        if (list == null || list.isEmpty()) {
            this.resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY.getCode(),
                String.format(ErrorCode.CATALOG_EMPTY.getMessageKey(), "niveles socioeconómicos")
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los tipos de cargo.
     *
     * @return una lista de objetos JobPositionType
     */
    @Override
    public List<JobPositionType> getJobPositionTypes() {
        List<JobPositionType> list = catalogQueryRepository.getJobPositionTypes();
        if (list == null || list.isEmpty()) {
            this.resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY.getCode(),
                String.format(ErrorCode.CATALOG_EMPTY.getMessageKey(), "tipos de cargo")
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los tipos de contrato.
     *
     * @return una lista de objetos ContractType
     */
    @Override
    public List<ContractType> getContractTypes() {
        List<ContractType> list = catalogQueryRepository.getContractTypes();
        if (list == null || list.isEmpty()) {
            this.resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY.getCode(),
                String.format(ErrorCode.CATALOG_EMPTY.getMessageKey(), "tipos de contrato")
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los tipos de salario.
     *
     * @return una lista de objetos SalaryType
     */
    @Override
    public List<SalaryType> getSalaryTypes() {
        List<SalaryType> list = catalogQueryRepository.getSalaryTypes();
        if (list == null || list.isEmpty()) {
            this.resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY.getCode(),
                String.format(ErrorCode.CATALOG_EMPTY.getMessageKey(), "tipos de salario")
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los géneros.
     *
     * @return una lista de objetos Gender
     */
    @Override
    public List<Gender> getGenders() {
        List<Gender> list = catalogQueryRepository.getGenders();
        if (list == null || list.isEmpty()) {
            this.resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY.getCode(),
                String.format(ErrorCode.CATALOG_EMPTY.getMessageKey(), "géneros")
            );
        }
        return list;
    }

    /**
     * Obtiene una ciudad por su código incluyendo su departamento.
     *
     * @param code código de la ciudad.
     * @return la entidad City encontrada.
     */
    @Override
    public City getCityByCodeWithDepartment(String code) {
        return this.catalogQueryRepository.getCityByCodeWithDepartment(code)
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "Ciudad con código: '" + code + "'")
                );
                return null;
            });
    }

    /**
     * Obtiene una ciudad por su nombre incluyendo su departamento.
     *
     * @param name nombre de la ciudad.
     * @return la entidad City encontrada.
     */
    @Override
    public City getCityByNameWithDepartment(String name) {
        return catalogQueryRepository.getCityByNameWithDepartment(name)
            .orElseGet(() -> {
                resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "Ciudad con nombre '" + name + "'")
                );
                return null;
            });
    }

    /**
     * Obtiene un departamento por su código incluyendo sus ciudades.
     *
     * @param code código del departamento.
     * @return la entidad Department encontrada.
     */
    @Override
    public Department getDepartmentByCodeWithCities(String code) {
        return catalogQueryRepository.getDepartmentByCodeWithCities(code)
            .orElseGet(() -> {
                resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "Departamento con código '" + code + "'")
                );
                return null;
            });
    }

    /**
     * Obtiene un departamento por su nombre incluyendo sus ciudades.
     *
     * @param name nombre del departamento.
     * @return la entidad Department encontrada.
     */
    @Override
    public Department getDepartmentByNameWithCities(String name) {
        return catalogQueryRepository.getDepartmentByNameWithCities(name)
            .orElseGet(() -> {
                resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "Departamento con nombre '" + name + "'")
                );
                return null;
            });
    }

    /**
     * Lista todos los departamentos (sin incluir sus ciudades).
     *
     * @return lista de departamentos.
     */
    @Override
    public List<Department> getAllDepartments() {
        List<Department> list = catalogQueryRepository.getAllDepartments();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY.getCode(),
                String.format(ErrorCode.CATALOG_EMPTY.getMessageKey(), "departamentos")
            );
        }
        return list;
    }

    /**
     * Lista todos los departamentos incluyendo sus ciudades
     * (cada City sin su Department para evitar ciclos).
     *
     * @return lista de departamentos con sus ciudades.
     */
    @Override
    public List<Department> getAllDepartmentsWithCities() {
        List<Department> list = catalogQueryRepository.getAllDepartmentsWithCities();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY.getCode(),
                String.format(ErrorCode.CATALOG_EMPTY.getMessageKey(), "departamentos")
            );
        }
        return list;
    }

    /**
     * Lista todas las ciudades (sin incluir su Department).
     *
     * @return lista de ciudades.
     */
    @Override
    public List<City> getAllCities() {
        List<City> list = catalogQueryRepository.getAllCities();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY.getCode(),
                String.format(ErrorCode.CATALOG_EMPTY.getMessageKey(), "ciudades")
            );
        }
        return list;
    }

    /**
     * Lista todas las ciudades INCLUYENDO su departamento.
     * (El Department no incluye sus cities para evitar ciclos).
     *
     * @return lista de ciudades con su departamento.
     */
    @Override
    public List<City> getAllCitiesWithDepartment() {
        List<City> list = catalogQueryRepository.getAllCitiesWithDepartment();
        if (list == null || list.isEmpty()) {
            resultFormatterOutputPort.throwCatalogEmptyException(
                ErrorCode.CATALOG_EMPTY.getCode(),
                String.format(ErrorCode.CATALOG_EMPTY.getMessageKey(), "ciudades")
            );
        }
        return list;
    }
}