package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.common.exceptions.BusinessRuleViolationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityAlreadyExistsException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityCreationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.StatusPersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireManagementRecordStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.StatusPersonEvaluatedEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordStatusQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
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
class BatteryManagementRecordCommandServiceTest {

    @Mock
    private BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository;

    @Mock
    private BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;

    @Mock
    private PersonEvaluatedQueryRepository personEvaluatedQueryRepository;

    @Mock
    private PersonEvaluatedCommandRepository personEvaluatedCommandRepository;

    @Mock
    private QuestionnaireManagementRecordCommandRepository questionnaireManagementRecordCommandRepository;

    @Mock
    private QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;

    @Mock
    private QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatterOutputPort;

    @InjectMocks
    private BatteryManagementRecordCommandService batteryManagementRecordCommandService;

    @Captor
    private ArgumentCaptor<BatteryManagementRecord> recordCaptor;

    @Captor
    private ArgumentCaptor<PersonEvaluated> personCaptor;

    private PersonEvaluated buildPersonEvaluated() {
        return PersonEvaluated.builder()
                .id(1L)
                .identificationType(IdentificationType.builder()
                        .id(1L)
                        .name("Cédula de Ciudadanía")
                        .abbreviation("CC")
                        .build())
                .identificationNumber("1061234567")
                .firstName("JUAN CARLOS")
                .lastName("GARCIA LOPEZ")
                .birthYear(1990)
                .email("juan.garcia@test.com")
                .status(StatusPersonEvaluated.builder()
                        .id(1L)
                        .name(StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription())
                        .build())
                .build();
    }

    private BatteryManagementRecord buildBatteryManagementRecord() {
        return BatteryManagementRecord.builder()
                .id(1L)
                .personEvaluated(buildPersonEvaluated())
                .status(BatteryManagementRecordStatus.builder()
                        .id(1L)
                        .name(BatteryManagementRecordStatusCode.CREATED.getDescription())
                        .build())
                .build();
    }

    private BatteryManagementRecordStatus buildStatus(Long id, BatteryManagementRecordStatusCode code) {
        return BatteryManagementRecordStatus.builder()
                .id(id)
                .name(code.getDescription())
                .build();
    }

    private StatusPersonEvaluated buildPersonStatus(Long id, StatusPersonEvaluatedEnum statusEnum) {
        return StatusPersonEvaluated.builder()
                .id(id)
                .name(statusEnum.getDescription())
                .build();
    }

    // ==================================================================================
    // createBatteryManagementRecord
    // ==================================================================================

    @Nested
    @DisplayName("createBatteryManagementRecord")
    class CreateBatteryManagementRecord {

        @Test
        @DisplayName("Debe crear registro cuando todas las validaciones pasan")
        void should_createRecord_when_allValidationsPass() {
            // Arrange
            Long personEvaluatedId = 1L;
            BatteryManagementRecordStatus initialStatus = buildStatus(1L, BatteryManagementRecordStatusCode.CREATED);
            PersonEvaluated person = buildPersonEvaluated();
            StatusPersonEvaluated withRecordStatus = buildPersonStatus(2L, StatusPersonEvaluatedEnum.WITH_RECORD);

            PersonEvaluated updatedPerson = buildPersonEvaluated();
            updatedPerson.setStatus(withRecordStatus);

            BatteryManagementRecord savedRecord = buildBatteryManagementRecord();
            savedRecord.setStatus(initialStatus);
            savedRecord.setPersonEvaluated(updatedPerson);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordStatusByName(
                    BatteryManagementRecordStatusCode.CREATED.getDescription()))
                    .thenReturn(Optional.of(initialStatus));
            when(personEvaluatedQueryRepository.getPersonEvaluatedById(personEvaluatedId))
                    .thenReturn(Optional.of(person));
            when(batteryManagementRecordQueryRepository.existsByPersonEvaluatedIdAndStatusNameIn(
                    eq(personEvaluatedId), anyList()))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(
                    StatusPersonEvaluatedEnum.WITH_RECORD.getDescription()))
                    .thenReturn(Optional.of(withRecordStatus));
            when(personEvaluatedCommandRepository.updatePersonEvaluated(any(PersonEvaluated.class)))
                    .thenReturn(Optional.of(updatedPerson));
            when(batteryManagementRecordCommandRepository.saveBatteryManagementRecord(any(BatteryManagementRecord.class)))
                    .thenReturn(Optional.of(savedRecord));

            // Act
            BatteryManagementRecord result = batteryManagementRecordCommandService.createBatteryManagementRecord(personEvaluatedId);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getStatus().getName())
                    .isEqualTo(BatteryManagementRecordStatusCode.CREATED.getDescription());
            assertThat(result.getPersonEvaluated()).isNotNull();
            assertThat(result.getPersonEvaluated().getStatus().getName())
                    .isEqualTo(StatusPersonEvaluatedEnum.WITH_RECORD.getDescription());

            verify(batteryManagementRecordCommandRepository).saveBatteryManagementRecord(recordCaptor.capture());
            BatteryManagementRecord captured = recordCaptor.getValue();
            assertThat(captured.getStatus().getName())
                    .isEqualTo(BatteryManagementRecordStatusCode.CREATED.getDescription());
            assertThat(captured.getPersonEvaluated()).isNotNull();

            verify(personEvaluatedCommandRepository).updatePersonEvaluated(personCaptor.capture());
            PersonEvaluated capturedPerson = personCaptor.getValue();
            assertThat(capturedPerson.getStatus().getName())
                    .isEqualTo(StatusPersonEvaluatedEnum.WITH_RECORD.getDescription());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado inicial no existe")
        void should_throwEntityNotFound_when_initialStatusNotFound() {
            // Arrange
            Long personEvaluatedId = 1L;

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordStatusByName(
                    BatteryManagementRecordStatusCode.CREATED.getDescription()))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_STATUS_NOT_FOUND.getMessageKey(),
                    "user.battery.config_error",
                    new Object[]{BatteryManagementRecordStatusCode.CREATED.getDescription()}))
                .when(resultFormatterOutputPort).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.createBatteryManagementRecord(personEvaluatedId))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatterOutputPort).throwEntityNotFound(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND,
                    "user.battery.config_error",
                    BatteryManagementRecordStatusCode.CREATED.getDescription());
            verify(personEvaluatedQueryRepository, never()).getPersonEvaluatedById(anyLong());
            verify(batteryManagementRecordCommandRepository, never()).saveBatteryManagementRecord(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la persona no existe")
        void should_throwEntityNotFound_when_personNotFound() {
            // Arrange
            Long personEvaluatedId = 1L;
            BatteryManagementRecordStatus initialStatus = buildStatus(1L, BatteryManagementRecordStatusCode.CREATED);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordStatusByName(
                    BatteryManagementRecordStatusCode.CREATED.getDescription()))
                    .thenReturn(Optional.of(initialStatus));
            when(personEvaluatedQueryRepository.getPersonEvaluatedById(personEvaluatedId))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_NOT_FOUND.getMessageKey(),
                    "user.battery.person_not_found",
                    new Object[]{personEvaluatedId}))
                .when(resultFormatterOutputPort).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.createBatteryManagementRecord(personEvaluatedId))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatterOutputPort).throwEntityNotFound(
                    ErrorCode.PERSON_NOT_FOUND,
                    "user.battery.person_not_found",
                    personEvaluatedId);
            verify(batteryManagementRecordQueryRepository, never()).existsByPersonEvaluatedIdAndStatusNameIn(anyLong(), anyList());
            verify(batteryManagementRecordCommandRepository, never()).saveBatteryManagementRecord(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado 'Con registro' no existe")
        void should_throwEntityNotFound_when_withRecordStatusNotFound() {
            // Arrange
            Long personEvaluatedId = 1L;
            BatteryManagementRecordStatus initialStatus = buildStatus(1L, BatteryManagementRecordStatusCode.CREATED);
            PersonEvaluated person = buildPersonEvaluated();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordStatusByName(
                    BatteryManagementRecordStatusCode.CREATED.getDescription()))
                    .thenReturn(Optional.of(initialStatus));
            when(personEvaluatedQueryRepository.getPersonEvaluatedById(personEvaluatedId))
                    .thenReturn(Optional.of(person));
            when(batteryManagementRecordQueryRepository.existsByPersonEvaluatedIdAndStatusNameIn(
                    eq(personEvaluatedId), anyList()))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(
                    StatusPersonEvaluatedEnum.WITH_RECORD.getDescription()))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_STATUS_NOT_FOUND.getMessageKey(),
                    "user.battery.sync_status_not_found",
                    new Object[]{StatusPersonEvaluatedEnum.WITH_RECORD.getDescription()}))
                .when(resultFormatterOutputPort).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.createBatteryManagementRecord(personEvaluatedId))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatterOutputPort).throwEntityNotFound(
                    ErrorCode.PERSON_STATUS_NOT_FOUND,
                    "user.battery.sync_status_not_found",
                    StatusPersonEvaluatedEnum.WITH_RECORD.getDescription());
            verify(personEvaluatedCommandRepository, never()).updatePersonEvaluated(any());
            verify(batteryManagementRecordCommandRepository, never()).saveBatteryManagementRecord(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityCreationException cuando falla la actualización de estado de persona")
        void should_throwEntityCreationFailed_when_personStatusUpdateFails() {
            // Arrange
            Long personEvaluatedId = 1L;
            BatteryManagementRecordStatus initialStatus = buildStatus(1L, BatteryManagementRecordStatusCode.CREATED);
            PersonEvaluated person = buildPersonEvaluated();
            StatusPersonEvaluated withRecordStatus = buildPersonStatus(2L, StatusPersonEvaluatedEnum.WITH_RECORD);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordStatusByName(
                    BatteryManagementRecordStatusCode.CREATED.getDescription()))
                    .thenReturn(Optional.of(initialStatus));
            when(personEvaluatedQueryRepository.getPersonEvaluatedById(personEvaluatedId))
                    .thenReturn(Optional.of(person));
            when(batteryManagementRecordQueryRepository.existsByPersonEvaluatedIdAndStatusNameIn(
                    eq(personEvaluatedId), anyList()))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(
                    StatusPersonEvaluatedEnum.WITH_RECORD.getDescription()))
                    .thenReturn(Optional.of(withRecordStatus));
            when(personEvaluatedCommandRepository.updatePersonEvaluated(any(PersonEvaluated.class)))
                    .thenReturn(Optional.empty());
            doThrow(new EntityCreationException(
                    ErrorCode.ENTITY_UPDATE_ERROR.getCode(),
                    ErrorCode.ENTITY_UPDATE_ERROR.getMessageKey(),
                    "user.battery.status_update_failed",
                    new Object[]{personEvaluatedId}))
                .when(resultFormatterOutputPort).throwEntityCreationFailed(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.createBatteryManagementRecord(personEvaluatedId))
                    .isInstanceOf(EntityCreationException.class);

            verify(resultFormatterOutputPort).throwEntityCreationFailed(
                    ErrorCode.ENTITY_UPDATE_ERROR,
                    "user.battery.status_update_failed",
                    personEvaluatedId);
            verify(batteryManagementRecordCommandRepository, never()).saveBatteryManagementRecord(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityAlreadyExists cuando la persona ya tiene un registro activo")
        void should_throwEntityAlreadyExists_when_personAlreadyHasActiveRecord() {
            // Arrange
            Long personEvaluatedId = 1L;
            BatteryManagementRecordStatus initialStatus = buildStatus(1L, BatteryManagementRecordStatusCode.CREATED);
            PersonEvaluated person = buildPersonEvaluated();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordStatusByName(
                    BatteryManagementRecordStatusCode.CREATED.getDescription()))
                    .thenReturn(Optional.of(initialStatus));
            when(personEvaluatedQueryRepository.getPersonEvaluatedById(personEvaluatedId))
                    .thenReturn(Optional.of(person));
            when(batteryManagementRecordQueryRepository.existsByPersonEvaluatedIdAndStatusNameIn(
                    eq(personEvaluatedId), anyList()))
                    .thenReturn(true);
            doThrow(new EntityAlreadyExistsException(
                    ErrorCode.BATTERY_RECORD_ALREADY_EXISTS.getCode(),
                    ErrorCode.BATTERY_RECORD_ALREADY_EXISTS.getMessageKey(),
                    "user.battery.already_exists",
                    new Object[]{personEvaluatedId}))
                .when(resultFormatterOutputPort).throwEntityAlreadyExists(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.createBatteryManagementRecord(personEvaluatedId))
                    .isInstanceOf(EntityAlreadyExistsException.class);

            verify(resultFormatterOutputPort).throwEntityAlreadyExists(
                    ErrorCode.BATTERY_RECORD_ALREADY_EXISTS,
                    "user.battery.already_exists",
                    personEvaluatedId);
            verify(personEvaluatedQueryRepository, never()).getStatusPersonEvaluatedByName(anyString());
            verify(personEvaluatedCommandRepository, never()).updatePersonEvaluated(any());
            verify(batteryManagementRecordCommandRepository, never()).saveBatteryManagementRecord(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityCreationException cuando falla el guardado del registro")
        void should_throwEntityCreationFailed_when_saveFails() {
            // Arrange
            Long personEvaluatedId = 1L;
            BatteryManagementRecordStatus initialStatus = buildStatus(1L, BatteryManagementRecordStatusCode.CREATED);
            PersonEvaluated person = buildPersonEvaluated();
            StatusPersonEvaluated withRecordStatus = buildPersonStatus(2L, StatusPersonEvaluatedEnum.WITH_RECORD);
            PersonEvaluated updatedPerson = buildPersonEvaluated();
            updatedPerson.setStatus(withRecordStatus);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordStatusByName(
                    BatteryManagementRecordStatusCode.CREATED.getDescription()))
                    .thenReturn(Optional.of(initialStatus));
            when(personEvaluatedQueryRepository.getPersonEvaluatedById(personEvaluatedId))
                    .thenReturn(Optional.of(person));
            when(batteryManagementRecordQueryRepository.existsByPersonEvaluatedIdAndStatusNameIn(
                    eq(personEvaluatedId), anyList()))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(
                    StatusPersonEvaluatedEnum.WITH_RECORD.getDescription()))
                    .thenReturn(Optional.of(withRecordStatus));
            when(personEvaluatedCommandRepository.updatePersonEvaluated(any(PersonEvaluated.class)))
                    .thenReturn(Optional.of(updatedPerson));
            when(batteryManagementRecordCommandRepository.saveBatteryManagementRecord(any(BatteryManagementRecord.class)))
                    .thenReturn(Optional.empty());
            doThrow(new EntityCreationException(
                    ErrorCode.ENTITY_CREATION_ERROR.getCode(),
                    ErrorCode.ENTITY_CREATION_ERROR.getMessageKey(),
                    "user.battery.creation_failed",
                    new Object[]{personEvaluatedId}))
                .when(resultFormatterOutputPort).throwEntityCreationFailed(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.createBatteryManagementRecord(personEvaluatedId))
                    .isInstanceOf(EntityCreationException.class);

            verify(resultFormatterOutputPort).throwEntityCreationFailed(
                    ErrorCode.ENTITY_CREATION_ERROR,
                    "user.battery.creation_failed",
                    personEvaluatedId);
        }
    }

    // ==================================================================================
    // deleteBatteryManagementRecord
    // ==================================================================================

    @Nested
    @DisplayName("deleteBatteryManagementRecord")
    class DeleteBatteryManagementRecord {

        @Test
        @DisplayName("Debe eliminar registro y sincronizar estado de persona cuando no tiene otros registros activos")
        void should_deleteRecord_when_statusIsCreatedAndNoOtherActiveRecords() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            PersonEvaluated person = record.getPersonEvaluated();
            person.setStatus(buildPersonStatus(2L, StatusPersonEvaluatedEnum.WITH_RECORD));
            StatusPersonEvaluated withoutRecordStatus = buildPersonStatus(1L, StatusPersonEvaluatedEnum.WITHOUT_RECORD);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(batteryManagementRecordQueryRepository.existsByPersonEvaluatedIdAndStatusNameIn(
                    eq(1L), anyList()))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(
                    StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription()))
                    .thenReturn(Optional.of(withoutRecordStatus));
            when(personEvaluatedCommandRepository.updatePersonEvaluated(any(PersonEvaluated.class)))
                    .thenReturn(Optional.of(person));

            // Act
            batteryManagementRecordCommandService.deleteBatteryManagementRecord(1L);

            // Assert
            verify(batteryManagementRecordCommandRepository).deleteBatteryManagementRecordById(1L);
            verify(personEvaluatedCommandRepository).updatePersonEvaluated(personCaptor.capture());
            assertThat(personCaptor.getValue().getStatus().getName())
                    .isEqualTo(StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription());
        }

        @Test
        @DisplayName("Debe eliminar registro y mantener estado de persona cuando tiene otros registros activos")
        void should_deleteRecordAndKeepPersonStatus_when_personHasOtherActiveRecords() {
            // Arrange
            PersonEvaluated person = buildPersonEvaluated();
            person.setStatus(buildPersonStatus(2L, StatusPersonEvaluatedEnum.WITH_RECORD));

            BatteryManagementRecord record = buildBatteryManagementRecord();
            record.setPersonEvaluated(person);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(batteryManagementRecordQueryRepository.existsByPersonEvaluatedIdAndStatusNameIn(
                    eq(1L), anyList()))
                    .thenReturn(true);

            // Act
            batteryManagementRecordCommandService.deleteBatteryManagementRecord(1L);

            // Assert
            verify(batteryManagementRecordCommandRepository).deleteBatteryManagementRecordById(1L);
            verify(personEvaluatedCommandRepository, never()).updatePersonEvaluated(any());
        }

        @Test
        @DisplayName("Debe eliminar registro sin sincronizar cuando la persona es null")
        void should_deleteRecord_when_statusIsCreatedAndPersonIsNull() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            record.setPersonEvaluated(null);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));

            // Act
            batteryManagementRecordCommandService.deleteBatteryManagementRecord(1L);

            // Assert
            verify(batteryManagementRecordCommandRepository).deleteBatteryManagementRecordById(1L);
            verify(batteryManagementRecordQueryRepository, never()).existsByPersonEvaluatedIdAndStatusNameIn(anyLong(), anyList());
            verify(personEvaluatedCommandRepository, never()).updatePersonEvaluated(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el registro no existe")
        void should_throwEntityNotFound_when_recordDoesNotExist() {
            // Arrange
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getMessageKey(),
                    "user.battery.delete_not_found",
                    new Object[]{1L}))
                .when(resultFormatterOutputPort).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.deleteBatteryManagementRecord(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatterOutputPort).throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.battery.delete_not_found",
                    1L);
            verify(batteryManagementRecordCommandRepository, never()).deleteBatteryManagementRecordById(anyLong());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el estado no es 'Creado'")
        void should_throwBusinessRuleViolation_when_statusIsNotCreated() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            record.setStatus(buildStatus(2L, BatteryManagementRecordStatusCode.IN_PROCESSING));

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.DELETE_BATTERY_MANAGEMENT_RECORD.getCode(),
                    ErrorCode.DELETE_BATTERY_MANAGEMENT_RECORD.getMessageKey(),
                    "user.battery.delete_not_allowed",
                    new Object[]{BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription(), 1L}))
                .when(resultFormatterOutputPort).throwBusinessRuleViolation(any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.deleteBatteryManagementRecord(1L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatterOutputPort).throwBusinessRuleViolation(
                    ErrorCode.DELETE_BATTERY_MANAGEMENT_RECORD,
                    "user.battery.delete_not_allowed",
                    BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription(), 1L);
            verify(batteryManagementRecordCommandRepository, never()).deleteBatteryManagementRecordById(anyLong());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado de sincronización no existe después de eliminar")
        void should_throwEntityNotFound_when_syncStatusNotFoundAfterDelete() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            record.getPersonEvaluated().setStatus(buildPersonStatus(2L, StatusPersonEvaluatedEnum.WITH_RECORD));

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(batteryManagementRecordQueryRepository.existsByPersonEvaluatedIdAndStatusNameIn(
                    eq(1L), anyList()))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(
                    StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription()))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_STATUS_NOT_FOUND.getMessageKey(),
                    "user.battery.sync_delete_error",
                    new Object[]{StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription()}))
                .when(resultFormatterOutputPort).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.deleteBatteryManagementRecord(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatterOutputPort).throwEntityNotFound(
                    ErrorCode.PERSON_STATUS_NOT_FOUND,
                    "user.battery.sync_delete_error",
                    StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription());
            verify(batteryManagementRecordCommandRepository).deleteBatteryManagementRecordById(1L);
            verify(personEvaluatedCommandRepository, never()).updatePersonEvaluated(any());
        }
    }

    // ==================================================================================
    // closeBatteryManagementRecord
    // ==================================================================================

    @Nested
    @DisplayName("closeBatteryManagementRecord")
    class CloseBatteryManagementRecord {

        @Test
        @DisplayName("Debe cerrar registro con cierre en cascada de cuestionarios y sincronizar persona")
        void should_closeRecord_when_statusIsCompleted() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            record.setStatus(buildStatus(3L, BatteryManagementRecordStatusCode.COMPLETED));
            record.getPersonEvaluated().setStatus(buildPersonStatus(2L, StatusPersonEvaluatedEnum.WITH_RECORD));

            QuestionnaireManagementRecordStatus closedQStatus = QuestionnaireManagementRecordStatus.builder()
                    .id(4L).name(QuestionnaireManagementRecordStatusEnum.CERRADO.getName()).build();
            BatteryManagementRecordStatus closedBatteryStatus = buildStatus(4L, BatteryManagementRecordStatusCode.CLOSED);
            StatusPersonEvaluated withoutRecordStatus = buildPersonStatus(1L, StatusPersonEvaluatedEnum.WITHOUT_RECORD);

            QuestionnaireManagementRecord q1 = QuestionnaireManagementRecord.builder()
                    .id(10L).batteryManagementRecord(record).status(QuestionnaireManagementRecordStatus.builder()
                            .id(3L).name(QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName()).build()).build();
            QuestionnaireManagementRecord q2 = QuestionnaireManagementRecord.builder()
                    .id(11L).batteryManagementRecord(record).status(QuestionnaireManagementRecordStatus.builder()
                            .id(3L).name(QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName()).build()).build();

            BatteryManagementRecord closedRecord = buildBatteryManagementRecord();
            closedRecord.setStatus(closedBatteryStatus);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordStatusQueryRepository.getQuestionnaireManagementRecordStatusByName(
                    QuestionnaireManagementRecordStatusEnum.CERRADO.getName()))
                    .thenReturn(Optional.of(closedQStatus));
            when(questionnaireManagementRecordQueryRepository.findAllByBatteryManagementRecordId(1L))
                    .thenReturn(List.of(q1, q2));
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordStatusByName(
                    BatteryManagementRecordStatusCode.CLOSED.getDescription()))
                    .thenReturn(Optional.of(closedBatteryStatus));
            when(batteryManagementRecordCommandRepository.saveBatteryManagementRecord(any(BatteryManagementRecord.class)))
                    .thenReturn(Optional.of(closedRecord));
            when(batteryManagementRecordQueryRepository.existsByPersonEvaluatedIdAndStatusNameIn(
                    eq(1L), anyList()))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(
                    StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription()))
                    .thenReturn(Optional.of(withoutRecordStatus));
            when(personEvaluatedCommandRepository.updatePersonEvaluated(any(PersonEvaluated.class)))
                    .thenReturn(Optional.of(record.getPersonEvaluated()));

            // Act
            BatteryManagementRecord result = batteryManagementRecordCommandService.closeBatteryManagementRecord(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getStatus().getName())
                    .isEqualTo(BatteryManagementRecordStatusCode.CLOSED.getDescription());

            verify(questionnaireManagementRecordCommandRepository).save(eq(q1));
            verify(questionnaireManagementRecordCommandRepository).save(eq(q2));
            assertThat(q1.getStatus().getName()).isEqualTo(QuestionnaireManagementRecordStatusEnum.CERRADO.getName());
            assertThat(q2.getStatus().getName()).isEqualTo(QuestionnaireManagementRecordStatusEnum.CERRADO.getName());

            verify(batteryManagementRecordCommandRepository).saveBatteryManagementRecord(recordCaptor.capture());
            assertThat(recordCaptor.getValue().getStatus().getName())
                    .isEqualTo(BatteryManagementRecordStatusCode.CLOSED.getDescription());

            verify(personEvaluatedCommandRepository).updatePersonEvaluated(personCaptor.capture());
            assertThat(personCaptor.getValue().getStatus().getName())
                    .isEqualTo(StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el registro no existe al cerrar")
        void should_throwEntityNotFound_when_recordNotFoundOnClose() {
            // Arrange
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getMessageKey(),
                    "user.battery.close_not_found",
                    new Object[]{1L}))
                .when(resultFormatterOutputPort).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.closeBatteryManagementRecord(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatterOutputPort).throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.battery.close_not_found",
                    1L);
            verify(batteryManagementRecordCommandRepository, never()).saveBatteryManagementRecord(any());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el estado no es 'Diligenciado'")
        void should_throwBusinessRuleViolation_when_statusIsNotCompleted() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            record.setStatus(buildStatus(2L, BatteryManagementRecordStatusCode.IN_PROCESSING));

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.CLOSE_BATTERY_MANAGEMENT_RECORD.getCode(),
                    ErrorCode.CLOSE_BATTERY_MANAGEMENT_RECORD.getMessageKey(),
                    "user.battery.close_not_allowed",
                    new Object[]{BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription(), 1L}))
                .when(resultFormatterOutputPort).throwBusinessRuleViolation(any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.closeBatteryManagementRecord(1L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatterOutputPort).throwBusinessRuleViolation(
                    ErrorCode.CLOSE_BATTERY_MANAGEMENT_RECORD,
                    "user.battery.close_not_allowed",
                    BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription(), 1L);
            verify(questionnaireManagementRecordCommandRepository, never()).save(any());
            verify(batteryManagementRecordCommandRepository, never()).saveBatteryManagementRecord(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado 'Cerrado' de cuestionario no existe")
        void should_throwEntityNotFound_when_closedQuestionnaireStatusNotFound() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            record.setStatus(buildStatus(3L, BatteryManagementRecordStatusCode.COMPLETED));

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordStatusQueryRepository.getQuestionnaireManagementRecordStatusByName(
                    QuestionnaireManagementRecordStatusEnum.CERRADO.getName()))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND.getMessageKey(),
                    "user.battery.close_config_error",
                    new Object[]{QuestionnaireManagementRecordStatusEnum.CERRADO.getName()}))
                .when(resultFormatterOutputPort).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.closeBatteryManagementRecord(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatterOutputPort).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND,
                    "user.battery.close_config_error",
                    QuestionnaireManagementRecordStatusEnum.CERRADO.getName());
            verify(questionnaireManagementRecordCommandRepository, never()).save(any());
            verify(batteryManagementRecordCommandRepository, never()).saveBatteryManagementRecord(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado 'Cerrado' de batería no existe")
        void should_throwEntityNotFound_when_closedBatteryStatusNotFound() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            record.setStatus(buildStatus(3L, BatteryManagementRecordStatusCode.COMPLETED));

            QuestionnaireManagementRecordStatus closedQStatus = QuestionnaireManagementRecordStatus.builder()
                    .id(4L).name(QuestionnaireManagementRecordStatusEnum.CERRADO.getName()).build();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordStatusQueryRepository.getQuestionnaireManagementRecordStatusByName(
                    QuestionnaireManagementRecordStatusEnum.CERRADO.getName()))
                    .thenReturn(Optional.of(closedQStatus));
            when(questionnaireManagementRecordQueryRepository.findAllByBatteryManagementRecordId(1L))
                    .thenReturn(List.of());
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordStatusByName(
                    BatteryManagementRecordStatusCode.CLOSED.getDescription()))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_STATUS_NOT_FOUND.getMessageKey(),
                    "user.battery.close_status_failed",
                    new Object[]{BatteryManagementRecordStatusCode.CLOSED.getDescription()}))
                .when(resultFormatterOutputPort).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.closeBatteryManagementRecord(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatterOutputPort).throwEntityNotFound(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND,
                    "user.battery.close_status_failed",
                    BatteryManagementRecordStatusCode.CLOSED.getDescription());
            verify(batteryManagementRecordCommandRepository, never()).saveBatteryManagementRecord(any());
        }

        @Test
        @DisplayName("Debe cerrar registro y mantener estado de persona cuando tiene otros registros activos")
        void should_closeRecord_and_keepPersonStatus_when_personHasOtherActiveRecords() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            record.setStatus(buildStatus(3L, BatteryManagementRecordStatusCode.COMPLETED));
            record.getPersonEvaluated().setStatus(buildPersonStatus(2L, StatusPersonEvaluatedEnum.WITH_RECORD));

            BatteryManagementRecordStatus closedBatteryStatus = buildStatus(4L, BatteryManagementRecordStatusCode.CLOSED);
            QuestionnaireManagementRecordStatus closedQStatus = QuestionnaireManagementRecordStatus.builder()
                    .id(4L).name(QuestionnaireManagementRecordStatusEnum.CERRADO.getName()).build();

            BatteryManagementRecord closedRecord = buildBatteryManagementRecord();
            closedRecord.setStatus(closedBatteryStatus);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordStatusQueryRepository.getQuestionnaireManagementRecordStatusByName(
                    QuestionnaireManagementRecordStatusEnum.CERRADO.getName()))
                    .thenReturn(Optional.of(closedQStatus));
            when(questionnaireManagementRecordQueryRepository.findAllByBatteryManagementRecordId(1L))
                    .thenReturn(List.of());
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordStatusByName(
                    BatteryManagementRecordStatusCode.CLOSED.getDescription()))
                    .thenReturn(Optional.of(closedBatteryStatus));
            when(batteryManagementRecordCommandRepository.saveBatteryManagementRecord(any(BatteryManagementRecord.class)))
                    .thenReturn(Optional.of(closedRecord));
            when(batteryManagementRecordQueryRepository.existsByPersonEvaluatedIdAndStatusNameIn(
                    eq(1L), anyList()))
                    .thenReturn(true);

            // Act
            BatteryManagementRecord result = batteryManagementRecordCommandService.closeBatteryManagementRecord(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getStatus().getName())
                    .isEqualTo(BatteryManagementRecordStatusCode.CLOSED.getDescription());
            verify(personEvaluatedCommandRepository, never()).updatePersonEvaluated(any());
        }

        @Test
        @DisplayName("Debe cerrar registro sin sincronizar cuando la persona es null")
        void should_closeRecord_without_sync_when_personIsNull() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            record.setStatus(buildStatus(3L, BatteryManagementRecordStatusCode.COMPLETED));
            record.setPersonEvaluated(null);

            BatteryManagementRecordStatus closedBatteryStatus = buildStatus(4L, BatteryManagementRecordStatusCode.CLOSED);
            QuestionnaireManagementRecordStatus closedQStatus = QuestionnaireManagementRecordStatus.builder()
                    .id(4L).name(QuestionnaireManagementRecordStatusEnum.CERRADO.getName()).build();

            BatteryManagementRecord closedRecord = buildBatteryManagementRecord();
            closedRecord.setStatus(closedBatteryStatus);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordStatusQueryRepository.getQuestionnaireManagementRecordStatusByName(
                    QuestionnaireManagementRecordStatusEnum.CERRADO.getName()))
                    .thenReturn(Optional.of(closedQStatus));
            when(questionnaireManagementRecordQueryRepository.findAllByBatteryManagementRecordId(1L))
                    .thenReturn(List.of());
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordStatusByName(
                    BatteryManagementRecordStatusCode.CLOSED.getDescription()))
                    .thenReturn(Optional.of(closedBatteryStatus));
            when(batteryManagementRecordCommandRepository.saveBatteryManagementRecord(any(BatteryManagementRecord.class)))
                    .thenReturn(Optional.of(closedRecord));

            // Act
            BatteryManagementRecord result = batteryManagementRecordCommandService.closeBatteryManagementRecord(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getStatus().getName())
                    .isEqualTo(BatteryManagementRecordStatusCode.CLOSED.getDescription());
            verify(batteryManagementRecordQueryRepository, never()).existsByPersonEvaluatedIdAndStatusNameIn(anyLong(), anyList());
            verify(personEvaluatedCommandRepository, never()).updatePersonEvaluated(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado de sincronización no existe después de cerrar")
        void should_throwEntityNotFound_when_syncStatusNotFoundAfterClose() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            record.setStatus(buildStatus(3L, BatteryManagementRecordStatusCode.COMPLETED));
            record.getPersonEvaluated().setStatus(buildPersonStatus(2L, StatusPersonEvaluatedEnum.WITH_RECORD));

            BatteryManagementRecordStatus closedBatteryStatus = buildStatus(4L, BatteryManagementRecordStatusCode.CLOSED);
            QuestionnaireManagementRecordStatus closedQStatus = QuestionnaireManagementRecordStatus.builder()
                    .id(4L).name(QuestionnaireManagementRecordStatusEnum.CERRADO.getName()).build();

            BatteryManagementRecord closedRecord = buildBatteryManagementRecord();
            closedRecord.setStatus(closedBatteryStatus);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordStatusQueryRepository.getQuestionnaireManagementRecordStatusByName(
                    QuestionnaireManagementRecordStatusEnum.CERRADO.getName()))
                    .thenReturn(Optional.of(closedQStatus));
            when(questionnaireManagementRecordQueryRepository.findAllByBatteryManagementRecordId(1L))
                    .thenReturn(List.of());
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordStatusByName(
                    BatteryManagementRecordStatusCode.CLOSED.getDescription()))
                    .thenReturn(Optional.of(closedBatteryStatus));
            when(batteryManagementRecordCommandRepository.saveBatteryManagementRecord(any(BatteryManagementRecord.class)))
                    .thenReturn(Optional.of(closedRecord));
            when(batteryManagementRecordQueryRepository.existsByPersonEvaluatedIdAndStatusNameIn(
                    eq(1L), anyList()))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(
                    StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription()))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_STATUS_NOT_FOUND.getMessageKey(),
                    "user.battery.sync_delete_error",
                    new Object[]{StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription()}))
                .when(resultFormatterOutputPort).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.closeBatteryManagementRecord(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(batteryManagementRecordCommandRepository).saveBatteryManagementRecord(any());
            verify(resultFormatterOutputPort).throwEntityNotFound(
                    ErrorCode.PERSON_STATUS_NOT_FOUND,
                    "user.battery.sync_delete_error",
                    StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription());
            verify(personEvaluatedCommandRepository, never()).updatePersonEvaluated(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityCreationException cuando falla el guardado del cierre")
        void should_throwEntityCreationFailed_when_closeUpdateFails() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            record.setStatus(buildStatus(3L, BatteryManagementRecordStatusCode.COMPLETED));

            QuestionnaireManagementRecordStatus closedQStatus = QuestionnaireManagementRecordStatus.builder()
                    .id(4L).name(QuestionnaireManagementRecordStatusEnum.CERRADO.getName()).build();
            BatteryManagementRecordStatus closedBatteryStatus = buildStatus(4L, BatteryManagementRecordStatusCode.CLOSED);

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordStatusQueryRepository.getQuestionnaireManagementRecordStatusByName(
                    QuestionnaireManagementRecordStatusEnum.CERRADO.getName()))
                    .thenReturn(Optional.of(closedQStatus));
            when(questionnaireManagementRecordQueryRepository.findAllByBatteryManagementRecordId(1L))
                    .thenReturn(List.of());
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordStatusByName(
                    BatteryManagementRecordStatusCode.CLOSED.getDescription()))
                    .thenReturn(Optional.of(closedBatteryStatus));
            when(batteryManagementRecordCommandRepository.saveBatteryManagementRecord(any(BatteryManagementRecord.class)))
                    .thenReturn(Optional.empty());
            doThrow(new EntityCreationException(
                    ErrorCode.ENTITY_UPDATE_ERROR.getCode(),
                    ErrorCode.ENTITY_UPDATE_ERROR.getMessageKey(),
                    "user.battery.close_failed",
                    new Object[]{1L}))
                .when(resultFormatterOutputPort).throwEntityCreationFailed(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordCommandService.closeBatteryManagementRecord(1L))
                    .isInstanceOf(EntityCreationException.class);

            verify(resultFormatterOutputPort).throwEntityCreationFailed(
                    ErrorCode.ENTITY_UPDATE_ERROR,
                    "user.battery.close_failed",
                    1L);
        }
    }
}
