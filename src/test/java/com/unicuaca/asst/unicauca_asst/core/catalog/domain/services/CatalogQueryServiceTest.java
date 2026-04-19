package com.unicuaca.asst.unicauca_asst.core.catalog.domain.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.City;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.CivilStatus;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.ContractType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.Department;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.EducationLevel;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.Gender;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.HousingType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.JobPositionType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.SalaryType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.SocioeconomicLevel;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.exceptions.CatalogEmptyException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogQueryServiceTest {

    @Mock
    private CatalogQueryRepository catalogQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatterOutputPort;

    @InjectMocks
    private CatalogQueryService catalogQueryService;

    // ==================================================================================
    // getIdTypes
    // ==================================================================================

    @Nested
    @DisplayName("getIdTypes")
    class GetIdTypes {

        @Test
        @DisplayName("Debe retornar lista de tipos de identificación")
        void should_returnIdTypes() {
            List<IdentificationType> list = List.of(IdentificationType.builder().id(1L).name("CC").build());
            when(catalogQueryRepository.getIdTypes()).thenReturn(list);

            List<IdentificationType> result = catalogQueryService.getIdTypes();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es vacía")
        void should_throwCatalogEmpty_when_listIsEmpty() {
            when(catalogQueryRepository.getIdTypes()).thenReturn(Collections.emptyList());
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getIdTypes())
                    .isInstanceOf(CatalogEmptyException.class);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es null")
        void should_throwCatalogEmpty_when_listIsNull() {
            when(catalogQueryRepository.getIdTypes()).thenReturn(null);
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getIdTypes())
                    .isInstanceOf(CatalogEmptyException.class);
        }
    }

    // ==================================================================================
    // getCivilStatuses
    // ==================================================================================

    @Nested
    @DisplayName("getCivilStatuses")
    class GetCivilStatuses {

        @Test
        @DisplayName("Debe retornar lista de estados civiles")
        void should_returnCivilStatuses() {
            List<CivilStatus> list = List.of(CivilStatus.builder().id(1L).name("Soltero").build());
            when(catalogQueryRepository.getCivilStatuses()).thenReturn(list);

            List<CivilStatus> result = catalogQueryService.getCivilStatuses();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es vacía")
        void should_throwCatalogEmpty_when_listIsEmpty() {
            when(catalogQueryRepository.getCivilStatuses()).thenReturn(Collections.emptyList());
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getCivilStatuses())
                    .isInstanceOf(CatalogEmptyException.class);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es null")
        void should_throwCatalogEmpty_when_listIsNull() {
            when(catalogQueryRepository.getCivilStatuses()).thenReturn(null);
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getCivilStatuses())
                    .isInstanceOf(CatalogEmptyException.class);
        }
    }

    // ==================================================================================
    // getEducationLevels
    // ==================================================================================

    @Nested
    @DisplayName("getEducationLevels")
    class GetEducationLevels {

        @Test
        @DisplayName("Debe retornar lista de niveles educativos")
        void should_returnEducationLevels() {
            List<EducationLevel> list = List.of(EducationLevel.builder().id(1L).name("Universitario").build());
            when(catalogQueryRepository.getEducationLevels()).thenReturn(list);

            List<EducationLevel> result = catalogQueryService.getEducationLevels();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es vacía")
        void should_throwCatalogEmpty_when_listIsEmpty() {
            when(catalogQueryRepository.getEducationLevels()).thenReturn(Collections.emptyList());
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getEducationLevels())
                    .isInstanceOf(CatalogEmptyException.class);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es null")
        void should_throwCatalogEmpty_when_listIsNull() {
            when(catalogQueryRepository.getEducationLevels()).thenReturn(null);
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getEducationLevels())
                    .isInstanceOf(CatalogEmptyException.class);
        }
    }

    // ==================================================================================
    // getHousingTypes
    // ==================================================================================

    @Nested
    @DisplayName("getHousingTypes")
    class GetHousingTypes {

        @Test
        @DisplayName("Debe retornar lista de tipos de vivienda")
        void should_returnHousingTypes() {
            List<HousingType> list = List.of(HousingType.builder().id(1L).name("Propia").build());
            when(catalogQueryRepository.getHousingTypes()).thenReturn(list);

            List<HousingType> result = catalogQueryService.getHousingTypes();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es vacía")
        void should_throwCatalogEmpty_when_listIsEmpty() {
            when(catalogQueryRepository.getHousingTypes()).thenReturn(Collections.emptyList());
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getHousingTypes())
                    .isInstanceOf(CatalogEmptyException.class);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es null")
        void should_throwCatalogEmpty_when_listIsNull() {
            when(catalogQueryRepository.getHousingTypes()).thenReturn(null);
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getHousingTypes())
                    .isInstanceOf(CatalogEmptyException.class);
        }
    }

    // ==================================================================================
    // getSocioeconomicLevels
    // ==================================================================================

    @Nested
    @DisplayName("getSocioeconomicLevels")
    class GetSocioeconomicLevels {

        @Test
        @DisplayName("Debe retornar lista de niveles socioeconómicos")
        void should_returnSocioeconomicLevels() {
            List<SocioeconomicLevel> list = List.of(SocioeconomicLevel.builder().id(1L).name("Estrato 1").build());
            when(catalogQueryRepository.getSocioeconomicLevels()).thenReturn(list);

            List<SocioeconomicLevel> result = catalogQueryService.getSocioeconomicLevels();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es vacía")
        void should_throwCatalogEmpty_when_listIsEmpty() {
            when(catalogQueryRepository.getSocioeconomicLevels()).thenReturn(Collections.emptyList());
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getSocioeconomicLevels())
                    .isInstanceOf(CatalogEmptyException.class);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es null")
        void should_throwCatalogEmpty_when_listIsNull() {
            when(catalogQueryRepository.getSocioeconomicLevels()).thenReturn(null);
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getSocioeconomicLevels())
                    .isInstanceOf(CatalogEmptyException.class);
        }
    }

    // ==================================================================================
    // getJobPositionTypes
    // ==================================================================================

    @Nested
    @DisplayName("getJobPositionTypes")
    class GetJobPositionTypes {

        @Test
        @DisplayName("Debe retornar lista de tipos de cargo")
        void should_returnJobPositionTypes() {
            List<JobPositionType> list = List.of(JobPositionType.builder().id(1L).name("Profesional").build());
            when(catalogQueryRepository.getJobPositionTypes()).thenReturn(list);

            List<JobPositionType> result = catalogQueryService.getJobPositionTypes();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es vacía")
        void should_throwCatalogEmpty_when_listIsEmpty() {
            when(catalogQueryRepository.getJobPositionTypes()).thenReturn(Collections.emptyList());
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getJobPositionTypes())
                    .isInstanceOf(CatalogEmptyException.class);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es null")
        void should_throwCatalogEmpty_when_listIsNull() {
            when(catalogQueryRepository.getJobPositionTypes()).thenReturn(null);
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getJobPositionTypes())
                    .isInstanceOf(CatalogEmptyException.class);
        }
    }

    // ==================================================================================
    // getContractTypes
    // ==================================================================================

    @Nested
    @DisplayName("getContractTypes")
    class GetContractTypes {

        @Test
        @DisplayName("Debe retornar lista de tipos de contrato")
        void should_returnContractTypes() {
            List<ContractType> list = List.of(ContractType.builder().id(1L).name("Término fijo").build());
            when(catalogQueryRepository.getContractTypes()).thenReturn(list);

            List<ContractType> result = catalogQueryService.getContractTypes();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es vacía")
        void should_throwCatalogEmpty_when_listIsEmpty() {
            when(catalogQueryRepository.getContractTypes()).thenReturn(Collections.emptyList());
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getContractTypes())
                    .isInstanceOf(CatalogEmptyException.class);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es null")
        void should_throwCatalogEmpty_when_listIsNull() {
            when(catalogQueryRepository.getContractTypes()).thenReturn(null);
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getContractTypes())
                    .isInstanceOf(CatalogEmptyException.class);
        }
    }

    // ==================================================================================
    // getSalaryTypes
    // ==================================================================================

    @Nested
    @DisplayName("getSalaryTypes")
    class GetSalaryTypes {

        @Test
        @DisplayName("Debe retornar lista de tipos de salario")
        void should_returnSalaryTypes() {
            List<SalaryType> list = List.of(SalaryType.builder().id(1L).name("Mensual").build());
            when(catalogQueryRepository.getSalaryTypes()).thenReturn(list);

            List<SalaryType> result = catalogQueryService.getSalaryTypes();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es vacía")
        void should_throwCatalogEmpty_when_listIsEmpty() {
            when(catalogQueryRepository.getSalaryTypes()).thenReturn(Collections.emptyList());
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getSalaryTypes())
                    .isInstanceOf(CatalogEmptyException.class);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es null")
        void should_throwCatalogEmpty_when_listIsNull() {
            when(catalogQueryRepository.getSalaryTypes()).thenReturn(null);
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getSalaryTypes())
                    .isInstanceOf(CatalogEmptyException.class);
        }
    }

    // ==================================================================================
    // getGenders
    // ==================================================================================

    @Nested
    @DisplayName("getGenders")
    class GetGenders {

        @Test
        @DisplayName("Debe retornar lista de géneros")
        void should_returnGenders() {
            List<Gender> list = List.of(Gender.builder().id(1L).name("Masculino").build());
            when(catalogQueryRepository.getGenders()).thenReturn(list);

            List<Gender> result = catalogQueryService.getGenders();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es vacía")
        void should_throwCatalogEmpty_when_listIsEmpty() {
            when(catalogQueryRepository.getGenders()).thenReturn(Collections.emptyList());
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getGenders())
                    .isInstanceOf(CatalogEmptyException.class);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es null")
        void should_throwCatalogEmpty_when_listIsNull() {
            when(catalogQueryRepository.getGenders()).thenReturn(null);
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getGenders())
                    .isInstanceOf(CatalogEmptyException.class);
        }
    }

    // ==================================================================================
    // getAllDepartments
    // ==================================================================================

    @Nested
    @DisplayName("getAllDepartments")
    class GetAllDepartments {

        @Test
        @DisplayName("Debe retornar lista de departamentos")
        void should_returnDepartments() {
            List<Department> list = List.of(Department.builder().id(1L).name("Cauca").build());
            when(catalogQueryRepository.getAllDepartments()).thenReturn(list);

            List<Department> result = catalogQueryService.getAllDepartments();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es vacía")
        void should_throwCatalogEmpty_when_listIsEmpty() {
            when(catalogQueryRepository.getAllDepartments()).thenReturn(Collections.emptyList());
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getAllDepartments())
                    .isInstanceOf(CatalogEmptyException.class);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es null")
        void should_throwCatalogEmpty_when_listIsNull() {
            when(catalogQueryRepository.getAllDepartments()).thenReturn(null);
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getAllDepartments())
                    .isInstanceOf(CatalogEmptyException.class);
        }
    }

    // ==================================================================================
    // getAllDepartmentsWithCities
    // ==================================================================================

    @Nested
    @DisplayName("getAllDepartmentsWithCities")
    class GetAllDepartmentsWithCities {

        @Test
        @DisplayName("Debe retornar lista de departamentos con ciudades")
        void should_returnDepartmentsWithCities() {
            List<Department> list = List.of(Department.builder().id(1L).name("Cauca").build());
            when(catalogQueryRepository.getAllDepartmentsWithCities()).thenReturn(list);

            List<Department> result = catalogQueryService.getAllDepartmentsWithCities();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es vacía")
        void should_throwCatalogEmpty_when_listIsEmpty() {
            when(catalogQueryRepository.getAllDepartmentsWithCities()).thenReturn(Collections.emptyList());
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getAllDepartmentsWithCities())
                    .isInstanceOf(CatalogEmptyException.class);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es null")
        void should_throwCatalogEmpty_when_listIsNull() {
            when(catalogQueryRepository.getAllDepartmentsWithCities()).thenReturn(null);
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getAllDepartmentsWithCities())
                    .isInstanceOf(CatalogEmptyException.class);
        }
    }

    // ==================================================================================
    // getAllCities
    // ==================================================================================

    @Nested
    @DisplayName("getAllCities")
    class GetAllCities {

        @Test
        @DisplayName("Debe retornar lista de ciudades")
        void should_returnCities() {
            List<City> list = List.of(City.builder().id(1L).name("Popayán").build());
            when(catalogQueryRepository.getAllCities()).thenReturn(list);

            List<City> result = catalogQueryService.getAllCities();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es vacía")
        void should_throwCatalogEmpty_when_listIsEmpty() {
            when(catalogQueryRepository.getAllCities()).thenReturn(Collections.emptyList());
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getAllCities())
                    .isInstanceOf(CatalogEmptyException.class);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es null")
        void should_throwCatalogEmpty_when_listIsNull() {
            when(catalogQueryRepository.getAllCities()).thenReturn(null);
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getAllCities())
                    .isInstanceOf(CatalogEmptyException.class);
        }
    }

    // ==================================================================================
    // getAllCitiesWithDepartment
    // ==================================================================================

    @Nested
    @DisplayName("getAllCitiesWithDepartment")
    class GetAllCitiesWithDepartment {

        @Test
        @DisplayName("Debe retornar lista de ciudades con departamento")
        void should_returnCitiesWithDepartment() {
            List<City> list = List.of(City.builder().id(1L).name("Popayán").build());
            when(catalogQueryRepository.getAllCitiesWithDepartment()).thenReturn(list);

            List<City> result = catalogQueryService.getAllCitiesWithDepartment();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es vacía")
        void should_throwCatalogEmpty_when_listIsEmpty() {
            when(catalogQueryRepository.getAllCitiesWithDepartment()).thenReturn(Collections.emptyList());
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getAllCitiesWithDepartment())
                    .isInstanceOf(CatalogEmptyException.class);
        }

        @Test
        @DisplayName("Debe lanzar CatalogEmpty cuando la lista es null")
        void should_throwCatalogEmpty_when_listIsNull() {
            when(catalogQueryRepository.getAllCitiesWithDepartment()).thenReturn(null);
            doThrow(new CatalogEmptyException(ErrorCode.CATALOG_EMPTY.getCode(), "empty"))
                .when(resultFormatterOutputPort).throwCatalogEmptyException(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getAllCitiesWithDepartment())
                    .isInstanceOf(CatalogEmptyException.class);
        }
    }

    // ==================================================================================
    // getCityByCodeWithDepartment
    // ==================================================================================

    @Nested
    @DisplayName("getCityByCodeWithDepartment")
    class GetCityByCodeWithDepartment {

        @Test
        @DisplayName("Debe retornar ciudad cuando existe por código")
        void should_returnCity_when_foundByCode() {
            City city = City.builder().id(1L).code("19001").name("Popayán").build();
            when(catalogQueryRepository.getCityByCodeWithDepartment("19001"))
                    .thenReturn(Optional.of(city));

            City result = catalogQueryService.getCityByCodeWithDepartment("19001");

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo("19001");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la ciudad no existe por código")
        void should_throwEntityNotFound_when_cityNotFoundByCode() {
            when(catalogQueryRepository.getCityByCodeWithDepartment("99999"))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    ErrorCode.ENTITY_NOT_FOUND.getMessageKey()))
                .when(resultFormatterOutputPort).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getCityByCodeWithDepartment("99999"))
                    .isInstanceOf(EntityNotFoundPersException.class);
        }
    }

    // ==================================================================================
    // getCityByNameWithDepartment
    // ==================================================================================

    @Nested
    @DisplayName("getCityByNameWithDepartment")
    class GetCityByNameWithDepartment {

        @Test
        @DisplayName("Debe retornar ciudad cuando existe por nombre")
        void should_returnCity_when_foundByName() {
            City city = City.builder().id(1L).name("Popayán").build();
            when(catalogQueryRepository.getCityByNameWithDepartment("Popayán"))
                    .thenReturn(Optional.of(city));

            City result = catalogQueryService.getCityByNameWithDepartment("Popayán");

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Popayán");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la ciudad no existe por nombre")
        void should_throwEntityNotFound_when_cityNotFoundByName() {
            when(catalogQueryRepository.getCityByNameWithDepartment("Inexistente"))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    ErrorCode.ENTITY_NOT_FOUND.getMessageKey()))
                .when(resultFormatterOutputPort).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getCityByNameWithDepartment("Inexistente"))
                    .isInstanceOf(EntityNotFoundPersException.class);
        }
    }

    // ==================================================================================
    // getDepartmentByCodeWithCities
    // ==================================================================================

    @Nested
    @DisplayName("getDepartmentByCodeWithCities")
    class GetDepartmentByCodeWithCities {

        @Test
        @DisplayName("Debe retornar departamento cuando existe por código")
        void should_returnDepartment_when_foundByCode() {
            Department dept = Department.builder().id(1L).code("19").name("Cauca").build();
            when(catalogQueryRepository.getDepartmentByCodeWithCities("19"))
                    .thenReturn(Optional.of(dept));

            Department result = catalogQueryService.getDepartmentByCodeWithCities("19");

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo("19");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el departamento no existe por código")
        void should_throwEntityNotFound_when_departmentNotFoundByCode() {
            when(catalogQueryRepository.getDepartmentByCodeWithCities("99"))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    ErrorCode.ENTITY_NOT_FOUND.getMessageKey()))
                .when(resultFormatterOutputPort).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getDepartmentByCodeWithCities("99"))
                    .isInstanceOf(EntityNotFoundPersException.class);
        }
    }

    // ==================================================================================
    // getDepartmentByNameWithCities
    // ==================================================================================

    @Nested
    @DisplayName("getDepartmentByNameWithCities")
    class GetDepartmentByNameWithCities {

        @Test
        @DisplayName("Debe retornar departamento cuando existe por nombre")
        void should_returnDepartment_when_foundByName() {
            Department dept = Department.builder().id(1L).name("Cauca").build();
            when(catalogQueryRepository.getDepartmentByNameWithCities("Cauca"))
                    .thenReturn(Optional.of(dept));

            Department result = catalogQueryService.getDepartmentByNameWithCities("Cauca");

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Cauca");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el departamento no existe por nombre")
        void should_throwEntityNotFound_when_departmentNotFoundByName() {
            when(catalogQueryRepository.getDepartmentByNameWithCities("Inexistente"))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    ErrorCode.ENTITY_NOT_FOUND.getMessageKey()))
                .when(resultFormatterOutputPort).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            assertThatThrownBy(() -> catalogQueryService.getDepartmentByNameWithCities("Inexistente"))
                    .isInstanceOf(EntityNotFoundPersException.class);
        }
    }
}
