package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.query;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.common.domain.models.*;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.CivilStatusEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.ContractTypeEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.EducationLevelEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.GenderEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.HousingTypeEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.IdentificationTypeEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.JobPositionTypeEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.SalaryTypeEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.SocioeconomicLevelEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.mappers.CatalogPersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida para las consultas de catálogos.
 *
 * Se encarga de recuperar la información desde la base de datos utilizando JPA.
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CatalogQueryRepositoryImpl implements CatalogQueryRepository {

    private final IdentificationTypeSpringJpaRepository identificationTypeSpringJpaRepository;
    private final CivilStatusSpringJpaRepository civilStatusSpringJpaRepository;
    private final EducationLevelSpringJpaRepository educationLevelSpringJpaRepository;
    private final HousingTypeSpringJpaRepository housingTypeSpringJpaRepository;
    private final SocioeconomicLevelSpringJpaRepository socioeconomicLevelSpringJpaRepository;
    private final JobPositionTypeSpringJpaRepository jobPositionTypeSpringJpaRepository;
    private final ContractTypeSpringJpaRepository contractTypeSpringJpaRepository;
    private final SalaryTypeSpringJpaRepository salaryTypeSpringJpaRepository;
    private final GenderSpringJpaRepository genderSpringJpaRepository;
    private final CitySpringJpaRepository citySpringJpaRepository;
    private final DepartmentSpringJpaRepository departmentSpringJpaRepository;

    private final CatalogPersistenceMapper catalogPersistenceMapper;

    /**
     * Obtiene una lista de todos los tipos de identificación.
     *
     * @return una lista de objetos IdentificationType
     */
    @Override
    public List<IdentificationType> getIdTypes() {
        List<IdentificationTypeEntity> entities = identificationTypeSpringJpaRepository.findAllByOrderByNameAsc();
        return entities.stream()
                .map(catalogPersistenceMapper::toIdentificationTypeDomain)
                .toList();
    }

    /**
     * Obtiene una lista de todos los estados civiles.
     *
     * @return una lista de objetos CivilStatus
     */
    @Override
    public List<CivilStatus> getCivilStatuses() {
        List<CivilStatusEntity> entities = civilStatusSpringJpaRepository.findAllByOrderByNameAsc();
        return entities.stream()
                .map(catalogPersistenceMapper::toCivilStatusDomain)
                .toList();
    }

    /**
     * Obtiene una lista de todos los niveles educativos.
     *
     * @return una lista de objetos EducationLevel
     */
    @Override
    public List<EducationLevel> getEducationLevels() {
        List<EducationLevelEntity> entities = educationLevelSpringJpaRepository.findAllByOrderByNameAsc();
        return entities.stream()
                .map(catalogPersistenceMapper::toEducationLevelDomain)
                .toList();
    }

    /**
     * Obtiene una lista de todos los tipos de vivienda.
     *
     * @return una lista de objetos HousingType
     */
    @Override
    public List<HousingType> getHousingTypes() {
        List<HousingTypeEntity> entities = housingTypeSpringJpaRepository.findAllByOrderByNameAsc();
        return entities.stream()
                .map(catalogPersistenceMapper::toHousingTypeDomain)
                .toList();
    }

    /**
     * Obtiene una lista de todos los niveles socioeconómicos.
     *
     * @return una lista de objetos SocioeconomicLevel
     */
    @Override
    public List<SocioeconomicLevel> getSocioeconomicLevels() {
        List<SocioeconomicLevelEntity> entities = socioeconomicLevelSpringJpaRepository.findAllByOrderByNameAsc();
        return entities.stream()
                .map(catalogPersistenceMapper::toSocioeconomicLevelDomain)
                .toList();
    }

    /**
     * Obtiene una lista de todos los tipos de cargo.
     *
     * @return una lista de objetos JobPositionType
     */
    @Override
    public List<JobPositionType> getJobPositionTypes() {
        List<JobPositionTypeEntity> entities = jobPositionTypeSpringJpaRepository.findAllByOrderByNameAsc();
        return entities.stream()
                .map(catalogPersistenceMapper::toJobPositionTypeDomain)
                .toList();
    }

    /**
     * Obtiene una lista de todos los tipos de contrato.
     *
     * @return una lista de objetos ContractType
     */
    @Override
    public List<ContractType> getContractTypes() {
        List<ContractTypeEntity> entities = contractTypeSpringJpaRepository.findAllByOrderByNameAsc();
        return entities.stream()
                .map(catalogPersistenceMapper::toContractTypeDomain)
                .toList();
    }

    /**
     * Obtiene una lista de todos los tipos de salario.
     *
     * @return una lista de objetos SalaryType
     */
    @Override
    public List<SalaryType> getSalaryTypes() {
        List<SalaryTypeEntity> entities = salaryTypeSpringJpaRepository.findAllByOrderByNameAsc();
        return entities.stream()
                .map(catalogPersistenceMapper::toSalaryTypeDomain)
                .toList();
    }

    /**
     * Obtiene una lista de todos los géneros.
     *
     * @return una lista de objetos Gender
     */
    @Override
    public List<Gender> getGenders() {
        List<GenderEntity> entities = genderSpringJpaRepository.findAllByOrderByNameAsc();
        return entities.stream()
                .map(catalogPersistenceMapper::toGenderDomain)
                .toList();
    }

    /**
     * Obtiene un tipo de identificación mediante su abreviatura.
     *
     * @param abbreviation la abreviatura del tipo de identificación
     * @return un {@link Optional} con el tipo de identificación correspondiente, o vacío si no se encuentra
     */
    @Override
    public Optional<IdentificationType> getIdTypeByAbbreviation(String abbreviation) {
        return identificationTypeSpringJpaRepository.findByAbbreviation(abbreviation)
                .map(catalogPersistenceMapper::toIdentificationTypeDomain);
    }

    /**
     * Obtiene una ciudad por su código incluyendo su departamento.
     *
     * @param code código de la ciudad.
     * @return un {@link Optional} con la ciudad encontrada, o vacío si no existe.
     */
    @Override
    public Optional<City> getCityByCodeWithDepartment(String code) {
        return this.citySpringJpaRepository
            .findByCodeWithDepartment(code)
            .map(this.catalogPersistenceMapper::toCityDomainWithDepartment);
    }

    /**
     * Obtiene una ciudad por su nombre incluyendo su departamento.
     *
     * @param name nombre de la ciudad.
     * @return un {@link Optional} con la ciudad encontrada, o vacío si no existe.
     */
    @Override
    public Optional<City> getCityByNameWithDepartment(String name) {
        return this.citySpringJpaRepository
            .findByNameWithDepartment(name)
            .map(this.catalogPersistenceMapper::toCityDomainWithDepartment);
    }

    /**
     * Obtiene un departamento por su código incluyendo sus ciudades.
     *
     * @param code código del departamento.
     * @return un {@link Optional} con el departamento encontrado, o vacío si no existe.
     */
    @Override
    public Optional<Department> getDepartmentByCodeWithCities(String code) {
        return this.departmentSpringJpaRepository
            .findByCodeWithCities(code)
            .map(this.catalogPersistenceMapper::toDepartmentDomainWithCities);
    }

    /**
     * Obtiene un departamento por su nombre incluyendo sus ciudades.
     *
     * @param name nombre del departamento.
     * @return un {@link Optional} con el departamento encontrado, o vacío si no existe.
     */
    @Override
    public Optional<Department> getDepartmentByNameWithCities(String name) {
        return this.departmentSpringJpaRepository
            .findByNameWithCities(name)
            .map(this.catalogPersistenceMapper::toDepartmentDomainWithCities);
    }

    /**
     * Obtiene todos los departamentos. (Sin incluir ciudades)
     *
     * @return una lista de todos los departamentos.
     */
    @Override
    public List<Department> getAllDepartments() {
        return this.departmentSpringJpaRepository.findAll()
            .stream()
            .map(catalogPersistenceMapper::toDepartmentDomain)
            .toList();
    }

    /**
     * Obtiene todos los departamentos incluyendo sus ciudades.
     *
     * @return una lista de todos los departamentos con sus ciudades.
     */
    @Override
    public List<Department> getAllDepartmentsWithCities() {
        return this.departmentSpringJpaRepository.findAllWithCitiesJoinFetch()
            .stream()
            .map(catalogPersistenceMapper::toDepartmentDomainWithCities)
            .toList();
    }

    /**
     * Obtiene todas las ciudades. (Sin incluir departamentos)
     *
     * @return una lista de todas las ciudades.
     */
    @Override
    public List<City> getAllCities() {
        return citySpringJpaRepository.findAll()
            .stream()
            .map(catalogPersistenceMapper::toCityDomain)
            .toList();
    }

    /**
     * Obtiene todas las ciudades incluyendo sus departamentos.
     *
     * @return una lista de todas las ciudades con sus departamentos.
     */
    @Override
    public List<City> getAllCitiesWithDepartment() {
        return citySpringJpaRepository.findAllWithDepartmentJoinFetch()
            .stream()
            .map(catalogPersistenceMapper::toCityDomainWithDepartment)
            .toList();
    }
}

