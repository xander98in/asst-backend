package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.CivilStatus;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.City;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.ContractType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.EducationLevel;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.Gender;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.HousingType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.JobPositionType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.SalaryType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.SocioeconomicLevel;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.exceptions.BusinessRuleViolationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityAlreadyExistsException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityCreationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordStatusQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedDetailsCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedDetailsQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonEvaluatedDetailsCommandServiceTest {

    @Mock
    private CatalogQueryRepository catalogQueryRepository;

    @Mock
    private BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;

    @Mock
    private PersonEvaluatedDetailsCommandRepository personEvaluatedDetailsCommandRepository;

    @Mock
    private PersonEvaluatedDetailsQueryRepository personEvaluatedDetailsQueryRepository;

    @Mock
    private QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;

    @Mock
    private BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository;

    @Mock
    private BatteryManagementRecordStatusQueryRepository batteryManagementRecordStatusQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private PersonEvaluatedDetailsCommandService personEvaluatedDetailsCommandService;

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private PersonEvaluatedDetails buildPersonEvaluatedDetails() {
        return PersonEvaluatedDetails.builder()
                .batteryManagementRecord(BatteryManagementRecord.builder()
                        .id(1L)
                        .status(BatteryManagementRecordStatus.builder()
                                .id(1L)
                                .name(BatteryManagementRecordStatusCode.CREATED.getDescription())
                                .build())
                        .build())
                .gender(Gender.builder().id(1L).name("Masculino").build())
                .civilStatus(CivilStatus.builder().id(1L).name("Soltero").build())
                .educationLevel(EducationLevel.builder().id(1L).name("Universitario").build())
                .profession("ingeniero de sistemas")
                .residenceCity(City.builder().id(1L).name("Popayan").build())
                .socioeconomicLevel(SocioeconomicLevel.builder().id(1L).name("Estrato 3").build())
                .housingType(HousingType.builder().id(1L).name("Propia").build())
                .dependentsCount(2)
                .workCity(City.builder().id(2L).name("Cali").build())
                .yearsAtCompany("5")
                .jobTitle("desarrollador senior")
                .jobPositionType(JobPositionType.builder().id(1L).name("Profesional").build())
                .yearsInPosition("3")
                .workAreaName("tecnologia")
                .contractType(ContractType.builder().id(1L).name("Termino indefinido").build())
                .dailyWorkHours(8)
                .salaryType(SalaryType.builder().id(1L).name("Salario fijo").build())
                .build();
    }

    private void mockAllCatalogs() {
        when(catalogQueryRepository.getGenderById(1L))
                .thenReturn(Optional.of(Gender.builder().id(1L).name("Masculino").build()));
        when(catalogQueryRepository.getCivilStatusById(1L))
                .thenReturn(Optional.of(CivilStatus.builder().id(1L).name("Soltero").build()));
        when(catalogQueryRepository.getEducationLevelById(1L))
                .thenReturn(Optional.of(EducationLevel.builder().id(1L).name("Universitario").build()));
        when(catalogQueryRepository.getCityById(1L))
                .thenReturn(Optional.of(City.builder().id(1L).name("Popayan").build()));
        when(catalogQueryRepository.getCityById(2L))
                .thenReturn(Optional.of(City.builder().id(2L).name("Cali").build()));
        when(catalogQueryRepository.getSocioeconomicLevelById(1L))
                .thenReturn(Optional.of(SocioeconomicLevel.builder().id(1L).name("Estrato 3").build()));
        when(catalogQueryRepository.getHousingTypeById(1L))
                .thenReturn(Optional.of(HousingType.builder().id(1L).name("Propia").build()));
        when(catalogQueryRepository.getJobPositionTypeById(1L))
                .thenReturn(Optional.of(JobPositionType.builder().id(1L).name("Profesional").build()));
        when(catalogQueryRepository.getContractTypeById(1L))
                .thenReturn(Optional.of(ContractType.builder().id(1L).name("Termino indefinido").build()));
        when(catalogQueryRepository.getSalaryTypeById(1L))
                .thenReturn(Optional.of(SalaryType.builder().id(1L).name("Salario fijo").build()));
    }

    private void mockCatalogsBeforeContractType() {
        when(catalogQueryRepository.getGenderById(1L))
                .thenReturn(Optional.of(Gender.builder().id(1L).name("Masculino").build()));
        when(catalogQueryRepository.getCivilStatusById(1L))
                .thenReturn(Optional.of(CivilStatus.builder().id(1L).name("Soltero").build()));
        when(catalogQueryRepository.getEducationLevelById(1L))
                .thenReturn(Optional.of(EducationLevel.builder().id(1L).name("Universitario").build()));
        when(catalogQueryRepository.getCityById(1L))
                .thenReturn(Optional.of(City.builder().id(1L).name("Popayan").build()));
        when(catalogQueryRepository.getCityById(2L))
                .thenReturn(Optional.of(City.builder().id(2L).name("Cali").build()));
        when(catalogQueryRepository.getSocioeconomicLevelById(1L))
                .thenReturn(Optional.of(SocioeconomicLevel.builder().id(1L).name("Estrato 3").build()));
        when(catalogQueryRepository.getHousingTypeById(1L))
                .thenReturn(Optional.of(HousingType.builder().id(1L).name("Propia").build()));
        when(catalogQueryRepository.getJobPositionTypeById(1L))
                .thenReturn(Optional.of(JobPositionType.builder().id(1L).name("Profesional").build()));
    }

    private BatteryManagementRecordStatus buildBatteryStatus(Long id, BatteryManagementRecordStatusCode code) {
        return BatteryManagementRecordStatus.builder()
                .id(id)
                .name(code.getDescription())
                .build();
    }

    // ==================================================================================
    // createPersonEvaluatedDetails
    // ==================================================================================

    @Nested
    @DisplayName("createPersonEvaluatedDetails")
    class CreatePersonEvaluatedDetails {

        @Test
        @DisplayName("Debe crear detalles cuando todas las validaciones pasan")
        void should_createDetails_when_allValidationsPass() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();
            BatteryManagementRecord record = details.getBatteryManagementRecord();
            BatteryManagementRecordStatus inProcessingStatus = buildBatteryStatus(2L, BatteryManagementRecordStatusCode.IN_PROCESSING);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(personEvaluatedDetailsQueryRepository.existsByBatteryManagementRecordId(1L))
                    .thenReturn(false);
            mockAllCatalogs();
            when(personEvaluatedDetailsCommandRepository.savePersonEvaluatedDetails(any(PersonEvaluatedDetails.class)))
                    .thenReturn(Optional.of(details));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(
                    BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription()))
                    .thenReturn(Optional.of(inProcessingStatus));

            // Act
            personEvaluatedDetailsCommandService.createPersonEvaluatedDetails(details);

            // Assert
            verify(personEvaluatedDetailsCommandRepository).savePersonEvaluatedDetails(any(PersonEvaluatedDetails.class));
            verify(batteryManagementRecordCommandRepository).updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el registro de batería no existe")
        void should_throwEntityNotFound_when_batteryRecordNotFound() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.CATALOG_RESOURCE_NOT_FOUND.getCode(),
                    ErrorCode.CATALOG_RESOURCE_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.catalog_invalid",
                    new Object[]{"registro de gestión de batería", 1L}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.createPersonEvaluatedDetails(details))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.CATALOG_RESOURCE_NOT_FOUND,
                    "user.person_evaluated_details.catalog_invalid",
                    "registro de gestión de batería", 1L);
            verify(personEvaluatedDetailsCommandRepository, never()).savePersonEvaluatedDetails(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityAlreadyExists cuando ya existen detalles para la batería")
        void should_throwEntityAlreadyExists_when_detailsAlreadyExist() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();
            BatteryManagementRecord record = details.getBatteryManagementRecord();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(personEvaluatedDetailsQueryRepository.existsByBatteryManagementRecordId(1L))
                    .thenReturn(true);
            doThrow(new EntityAlreadyExistsException(
                    ErrorCode.PERSON_DETAILS_ALREADY_EXISTS.getCode(),
                    ErrorCode.PERSON_DETAILS_ALREADY_EXISTS.getMessageKey(),
                    "user.person_evaluated_details.already_exists",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityAlreadyExists(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.createPersonEvaluatedDetails(details))
                    .isInstanceOf(EntityAlreadyExistsException.class);

            verify(resultFormatter).throwEntityAlreadyExists(
                    ErrorCode.PERSON_DETAILS_ALREADY_EXISTS,
                    "user.person_evaluated_details.already_exists",
                    1L);
            verify(personEvaluatedDetailsCommandRepository, never()).savePersonEvaluatedDetails(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando un catálogo no existe")
        void should_throwEntityNotFound_when_catalogNotFound() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();
            BatteryManagementRecord record = details.getBatteryManagementRecord();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(personEvaluatedDetailsQueryRepository.existsByBatteryManagementRecordId(1L))
                    .thenReturn(false);
            when(catalogQueryRepository.getGenderById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.CATALOG_RESOURCE_NOT_FOUND.getCode(),
                    ErrorCode.CATALOG_RESOURCE_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.catalog_invalid",
                    new Object[]{"género", 1L}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.createPersonEvaluatedDetails(details))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.CATALOG_RESOURCE_NOT_FOUND,
                    "user.person_evaluated_details.catalog_invalid",
                    "género", 1L);
            verify(personEvaluatedDetailsCommandRepository, never()).savePersonEvaluatedDetails(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityCreationException cuando falla el guardado")
        void should_throwEntityCreationFailed_when_saveFails() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();
            BatteryManagementRecord record = details.getBatteryManagementRecord();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(personEvaluatedDetailsQueryRepository.existsByBatteryManagementRecordId(1L))
                    .thenReturn(false);
            mockAllCatalogs();
            when(personEvaluatedDetailsCommandRepository.savePersonEvaluatedDetails(any(PersonEvaluatedDetails.class)))
                    .thenReturn(Optional.empty());
            doThrow(new EntityCreationException(
                    ErrorCode.ENTITY_CREATION_ERROR.getCode(),
                    ErrorCode.ENTITY_CREATION_ERROR.getMessageKey(),
                    "user.person_evaluated_details.creation_failed",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityCreationFailed(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.createPersonEvaluatedDetails(details))
                    .isInstanceOf(EntityCreationException.class);

            verify(resultFormatter).throwEntityCreationFailed(
                    ErrorCode.ENTITY_CREATION_ERROR,
                    "user.person_evaluated_details.creation_failed",
                    1L);
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el ID de un catálogo es null")
        void should_throwBusinessRuleViolation_when_catalogIdIsNull() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();
            BatteryManagementRecord record = details.getBatteryManagementRecord();
            details.setContractType(ContractType.builder().id(null).build());

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(personEvaluatedDetailsQueryRepository.existsByBatteryManagementRecordId(1L))
                    .thenReturn(false);
            mockCatalogsBeforeContractType();
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.BAD_REQUEST.getCode(),
                    ErrorCode.BAD_REQUEST.getMessageKey(),
                    "user.person_evaluated_details.required_fields_missing",
                    new Object[]{"tipo de contrato"}))
                .when(resultFormatter).throwBusinessRuleViolation(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.createPersonEvaluatedDetails(details))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.BAD_REQUEST,
                    "user.person_evaluated_details.required_fields_missing",
                    "tipo de contrato");
            verify(personEvaluatedDetailsCommandRepository, never()).savePersonEvaluatedDetails(any());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el ID del registro de batería es null")
        void should_throwBusinessRuleViolation_when_batteryRecordIdIsNull() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();
            details.setBatteryManagementRecord(BatteryManagementRecord.builder().id(null).build());

            doThrow(new BusinessRuleViolationException(
                    ErrorCode.BAD_REQUEST.getCode(),
                    ErrorCode.BAD_REQUEST.getMessageKey(),
                    "user.person_evaluated_details.required_fields_missing",
                    new Object[]{"registro de gestión de batería"}))
                .when(resultFormatter).throwBusinessRuleViolation(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.createPersonEvaluatedDetails(details))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.BAD_REQUEST,
                    "user.person_evaluated_details.required_fields_missing",
                    "registro de gestión de batería");
            verify(personEvaluatedDetailsCommandRepository, never()).savePersonEvaluatedDetails(any());
            verify(batteryManagementRecordQueryRepository, never()).getBatteryManagementRecordById(anyLong());
        }

        @Test
        @DisplayName("Debe manejar texto null en normalizeText")
        void should_handleNullTextInNormalizeText() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();
            BatteryManagementRecord record = details.getBatteryManagementRecord();
            details.setProfession(null);
            BatteryManagementRecordStatus inProcessingStatus = buildBatteryStatus(2L, BatteryManagementRecordStatusCode.IN_PROCESSING);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(personEvaluatedDetailsQueryRepository.existsByBatteryManagementRecordId(1L))
                    .thenReturn(false);
            mockAllCatalogs();
            when(personEvaluatedDetailsCommandRepository.savePersonEvaluatedDetails(any(PersonEvaluatedDetails.class)))
                    .thenReturn(Optional.of(details));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(
                    BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription()))
                    .thenReturn(Optional.of(inProcessingStatus));

            // Act
            personEvaluatedDetailsCommandService.createPersonEvaluatedDetails(details);

            // Assert
            verify(personEvaluatedDetailsCommandRepository).savePersonEvaluatedDetails(any(PersonEvaluatedDetails.class));
        }

        @Test
        @DisplayName("Debe manejar texto en blanco en normalizeText")
        void should_handleBlankTextInNormalizeText() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();
            BatteryManagementRecord record = details.getBatteryManagementRecord();
            details.setProfession("   ");
            BatteryManagementRecordStatus inProcessingStatus = buildBatteryStatus(2L, BatteryManagementRecordStatusCode.IN_PROCESSING);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(personEvaluatedDetailsQueryRepository.existsByBatteryManagementRecordId(1L))
                    .thenReturn(false);
            mockAllCatalogs();
            when(personEvaluatedDetailsCommandRepository.savePersonEvaluatedDetails(any(PersonEvaluatedDetails.class)))
                    .thenReturn(Optional.of(details));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(
                    BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription()))
                    .thenReturn(Optional.of(inProcessingStatus));

            // Act
            personEvaluatedDetailsCommandService.createPersonEvaluatedDetails(details);

            // Assert
            verify(personEvaluatedDetailsCommandRepository).savePersonEvaluatedDetails(any(PersonEvaluatedDetails.class));
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el registro de batería no existe durante sincronización")
        void should_throwEntityNotFound_when_batteryNotFoundDuringSync() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();
            BatteryManagementRecord record = details.getBatteryManagementRecord();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record))
                    .thenReturn(Optional.empty());
            when(personEvaluatedDetailsQueryRepository.existsByBatteryManagementRecordId(1L))
                    .thenReturn(false);
            mockAllCatalogs();
            when(personEvaluatedDetailsCommandRepository.savePersonEvaluatedDetails(any(PersonEvaluatedDetails.class)))
                    .thenReturn(Optional.of(details));
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.battery_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.createPersonEvaluatedDetails(details))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.person_evaluated_details.battery_not_found",
                    1L);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado 'En diligenciamiento' no existe durante sincronización")
        void should_throwEntityNotFound_when_inProcessingStatusNotFoundDuringSync() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();
            BatteryManagementRecord record = details.getBatteryManagementRecord();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(personEvaluatedDetailsQueryRepository.existsByBatteryManagementRecordId(1L))
                    .thenReturn(false);
            mockAllCatalogs();
            when(personEvaluatedDetailsCommandRepository.savePersonEvaluatedDetails(any(PersonEvaluatedDetails.class)))
                    .thenReturn(Optional.of(details));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(
                    BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription()))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_STATUS_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.sync_status_not_found",
                    new Object[]{BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription()}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.createPersonEvaluatedDetails(details))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND,
                    "user.person_evaluated_details.sync_status_not_found",
                    BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription());
            verify(batteryManagementRecordCommandRepository, never()).updateBatteryManagementRecord(any());
        }
    }

    // ==================================================================================
    // updatePersonEvaluatedDetails
    // ==================================================================================

    @Nested
    @DisplayName("updatePersonEvaluatedDetails")
    class UpdatePersonEvaluatedDetails {

        @Test
        @DisplayName("Debe actualizar detalles cuando todas las validaciones pasan")
        void should_updateDetails_when_allValidationsPass() {
            // Arrange
            PersonEvaluatedDetails existing = buildPersonEvaluatedDetails();
            existing.setId(1L);
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();

            when(personEvaluatedDetailsQueryRepository.getByIdWithAll(1L))
                    .thenReturn(Optional.of(existing));
            mockAllCatalogs();
            when(personEvaluatedDetailsCommandRepository.updatePersonEvaluatedDetails(eq(1L), any(PersonEvaluatedDetails.class)))
                    .thenReturn(Optional.of(details));

            // Act
            personEvaluatedDetailsCommandService.updatePersonEvaluatedDetails(1L, details);

            // Assert
            verify(personEvaluatedDetailsCommandRepository).updatePersonEvaluatedDetails(eq(1L), any(PersonEvaluatedDetails.class));
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando los detalles no existen")
        void should_throwEntityNotFound_when_detailsNotFound() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();

            when(personEvaluatedDetailsQueryRepository.getByIdWithAll(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_DETAILS_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.update_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.updatePersonEvaluatedDetails(1L, details))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND,
                    "user.person_evaluated_details.update_not_found",
                    1L);
            verify(personEvaluatedDetailsCommandRepository, never()).updatePersonEvaluatedDetails(anyLong(), any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando un catálogo no existe en actualización")
        void should_throwEntityNotFound_when_catalogNotFoundOnUpdate() {
            // Arrange
            PersonEvaluatedDetails existing = buildPersonEvaluatedDetails();
            existing.setId(1L);
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();

            when(personEvaluatedDetailsQueryRepository.getByIdWithAll(1L))
                    .thenReturn(Optional.of(existing));
            when(catalogQueryRepository.getGenderById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.CATALOG_RESOURCE_NOT_FOUND.getCode(),
                    ErrorCode.CATALOG_RESOURCE_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.catalog_invalid",
                    new Object[]{"género", 1L}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.updatePersonEvaluatedDetails(1L, details))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.CATALOG_RESOURCE_NOT_FOUND,
                    "user.person_evaluated_details.catalog_invalid",
                    "género", 1L);
            verify(personEvaluatedDetailsCommandRepository, never()).updatePersonEvaluatedDetails(anyLong(), any());
        }

        @Test
        @DisplayName("Debe lanzar EntityCreationException cuando falla la actualización")
        void should_throwEntityCreationFailed_when_updateFails() {
            // Arrange
            PersonEvaluatedDetails existing = buildPersonEvaluatedDetails();
            existing.setId(1L);
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();

            when(personEvaluatedDetailsQueryRepository.getByIdWithAll(1L))
                    .thenReturn(Optional.of(existing));
            mockAllCatalogs();
            when(personEvaluatedDetailsCommandRepository.updatePersonEvaluatedDetails(eq(1L), any(PersonEvaluatedDetails.class)))
                    .thenReturn(Optional.empty());
            doThrow(new EntityCreationException(
                    ErrorCode.ENTITY_UPDATE_ERROR.getCode(),
                    ErrorCode.ENTITY_UPDATE_ERROR.getMessageKey(),
                    "user.person_evaluated_details.update_failed",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityCreationFailed(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.updatePersonEvaluatedDetails(1L, details))
                    .isInstanceOf(EntityCreationException.class);

            verify(resultFormatter).throwEntityCreationFailed(
                    ErrorCode.ENTITY_UPDATE_ERROR,
                    "user.person_evaluated_details.update_failed",
                    1L);
        }
    }

    // ==================================================================================
    // deletePersonEvaluatedDetails
    // ==================================================================================

    @Nested
    @DisplayName("deletePersonEvaluatedDetails")
    class DeletePersonEvaluatedDetails {

        @Test
        @DisplayName("Debe eliminar detalles y sincronizar batería a 'Creado' cuando no hay cuestionarios")
        void should_deleteAndSyncToCreated_when_noQuestionnaires() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();
            BatteryManagementRecord record = details.getBatteryManagementRecord();
            BatteryManagementRecordStatus createdStatus = buildBatteryStatus(1L, BatteryManagementRecordStatusCode.CREATED);

            when(personEvaluatedDetailsQueryRepository.getByIdWithAll(1L))
                    .thenReturn(Optional.of(details));
            when(questionnaireManagementRecordQueryRepository.existsByBatteryManagementRecordIdAndQuestionnaireAbbreviationIn(
                    eq(1L), anyList()))
                    .thenReturn(false);
            when(questionnaireManagementRecordQueryRepository.existsByBatteryManagementRecordId(1L))
                    .thenReturn(false);
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(
                    BatteryManagementRecordStatusCode.CREATED.getDescription()))
                    .thenReturn(Optional.of(createdStatus));

            // Act
            personEvaluatedDetailsCommandService.deletePersonEvaluatedDetails(1L);

            // Assert
            verify(personEvaluatedDetailsCommandRepository).deleteById(1L);
            verify(batteryManagementRecordCommandRepository).updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe eliminar detalles sin sincronizar batería cuando hay cuestionarios")
        void should_deleteWithoutSync_when_hasOtherQuestionnaires() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();

            when(personEvaluatedDetailsQueryRepository.getByIdWithAll(1L))
                    .thenReturn(Optional.of(details));
            when(questionnaireManagementRecordQueryRepository.existsByBatteryManagementRecordIdAndQuestionnaireAbbreviationIn(
                    eq(1L), anyList()))
                    .thenReturn(false);
            when(questionnaireManagementRecordQueryRepository.existsByBatteryManagementRecordId(1L))
                    .thenReturn(true);

            // Act
            personEvaluatedDetailsCommandService.deletePersonEvaluatedDetails(1L);

            // Assert
            verify(personEvaluatedDetailsCommandRepository).deleteById(1L);
            verify(batteryManagementRecordCommandRepository, never()).updateBatteryManagementRecord(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando los detalles no existen")
        void should_throwEntityNotFound_when_detailsNotFound() {
            // Arrange
            when(personEvaluatedDetailsQueryRepository.getByIdWithAll(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_DETAILS_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.delete_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.deletePersonEvaluatedDetails(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND,
                    "user.person_evaluated_details.delete_not_found",
                    1L);
            verify(personEvaluatedDetailsCommandRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando hay registros intralaborales")
        void should_throwBusinessRuleViolation_when_hasIntralaboralRecords() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();

            when(personEvaluatedDetailsQueryRepository.getByIdWithAll(1L))
                    .thenReturn(Optional.of(details));
            when(questionnaireManagementRecordQueryRepository.existsByBatteryManagementRecordIdAndQuestionnaireAbbreviationIn(
                    eq(1L), anyList()))
                    .thenReturn(true);
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.PERSON_EVALUATED_DETAILS_DELETE_NOT_ALLOWED.getCode(),
                    ErrorCode.PERSON_EVALUATED_DETAILS_DELETE_NOT_ALLOWED.getMessageKey(),
                    "user.person_evaluated_details.delete_not_allowed",
                    new Object[]{1L}))
                .when(resultFormatter).throwBusinessRuleViolation(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.deletePersonEvaluatedDetails(1L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.PERSON_EVALUATED_DETAILS_DELETE_NOT_ALLOWED,
                    "user.person_evaluated_details.delete_not_allowed",
                    1L);
            verify(personEvaluatedDetailsCommandRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la batería no existe durante sincronización post-eliminación")
        void should_throwEntityNotFound_when_batteryNotFoundDuringSyncAfterDelete() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();

            when(personEvaluatedDetailsQueryRepository.getByIdWithAll(1L))
                    .thenReturn(Optional.of(details));
            when(questionnaireManagementRecordQueryRepository.existsByBatteryManagementRecordIdAndQuestionnaireAbbreviationIn(
                    eq(1L), anyList()))
                    .thenReturn(false);
            when(questionnaireManagementRecordQueryRepository.existsByBatteryManagementRecordId(1L))
                    .thenReturn(false);
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.battery_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.deletePersonEvaluatedDetails(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.person_evaluated_details.battery_not_found",
                    1L);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado 'Creado' no existe durante sincronización post-eliminación")
        void should_throwEntityNotFound_when_createdStatusNotFoundDuringSyncAfterDelete() {
            // Arrange
            PersonEvaluatedDetails details = buildPersonEvaluatedDetails();
            BatteryManagementRecord record = details.getBatteryManagementRecord();

            when(personEvaluatedDetailsQueryRepository.getByIdWithAll(1L))
                    .thenReturn(Optional.of(details));
            when(questionnaireManagementRecordQueryRepository.existsByBatteryManagementRecordIdAndQuestionnaireAbbreviationIn(
                    eq(1L), anyList()))
                    .thenReturn(false);
            when(questionnaireManagementRecordQueryRepository.existsByBatteryManagementRecordId(1L))
                    .thenReturn(false);
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(
                    BatteryManagementRecordStatusCode.CREATED.getDescription()))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_STATUS_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.sync_status_failed",
                    new Object[]{BatteryManagementRecordStatusCode.CREATED.getDescription()}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsCommandService.deletePersonEvaluatedDetails(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND,
                    "user.person_evaluated_details.sync_status_failed",
                    BatteryManagementRecordStatusCode.CREATED.getDescription());
            verify(batteryManagementRecordCommandRepository, never()).updateBatteryManagementRecord(any());
        }
    }
}
