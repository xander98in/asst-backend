package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

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
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.exceptions.BusinessRuleViolationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityAlreadyExistsException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityCreationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.StatusPersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.StatusPersonEvaluatedEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonEvaluatedCommandServiceTest {

    @Mock
    private PersonEvaluatedCommandRepository personEvaluatedCommandRepository;

    @Mock
    private PersonEvaluatedQueryRepository personEvaluatedQueryRepository;

    @Mock
    private BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;

    @Mock
    private CatalogQueryRepository catalogQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private PersonEvaluatedCommandService personEvaluatedCommandService;

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
                .firstName("juan carlos")
                .lastName("garcia lopez")
                .birthYear(1990)
                .email("juan.garcia@test.com")
                .status(null)
                .build();
    }

    private StatusPersonEvaluated buildDefaultStatus() {
        return StatusPersonEvaluated.builder()
                .id(1L)
                .name(StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription())
                .build();
    }

    // ==================================================================================
    // createPersonEvaluated
    // ==================================================================================

    @Nested
    @DisplayName("createPersonEvaluated")
    class CreatePersonEvaluated {

        @Test
        @DisplayName("Debe crear persona cuando todas las validaciones pasan")
        void should_createPersonEvaluated_when_allValidationsPass() {
            // Arrange
            PersonEvaluated person = buildPersonEvaluated();
            IdentificationType identificationType = person.getIdentificationType();
            StatusPersonEvaluated status = buildDefaultStatus();

            PersonEvaluated savedPerson = buildPersonEvaluated();
            savedPerson.setFirstName("JUAN CARLOS");
            savedPerson.setLastName("GARCIA LOPEZ");
            savedPerson.setIdentificationType(identificationType);
            savedPerson.setStatus(status);

            when(catalogQueryRepository.getIdTypeByAbbreviation("CC"))
                    .thenReturn(Optional.of(identificationType));
            when(personEvaluatedQueryRepository.existsByIdentification(1L, "1061234567"))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.existsByEmail("juan.garcia@test.com"))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(
                    StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription()))
                    .thenReturn(Optional.of(status));
            when(personEvaluatedCommandRepository.savePersonEvaluated(any(PersonEvaluated.class)))
                    .thenReturn(Optional.of(savedPerson));

            // Act
            PersonEvaluated result = personEvaluatedCommandService.createPersonEvaluated(person);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getFirstName()).isEqualTo("JUAN CARLOS");
            assertThat(result.getLastName()).isEqualTo("GARCIA LOPEZ");
            assertThat(result.getStatus().getName())
                    .isEqualTo(StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription());

            verify(personEvaluatedCommandRepository).savePersonEvaluated(personCaptor.capture());
            PersonEvaluated captured = personCaptor.getValue();
            assertThat(captured.getFirstName()).isEqualTo("JUAN CARLOS");
            assertThat(captured.getLastName()).isEqualTo("GARCIA LOPEZ");
            assertThat(captured.getIdentificationType().getId()).isEqualTo(1L);
            assertThat(captured.getStatus().getName())
                    .isEqualTo(StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el tipo de identificación no existe en catálogo")
        void should_throwEntityNotFound_when_identificationTypeNotFoundInCatalog() {
            // Arrange
            PersonEvaluated person = buildPersonEvaluated();

            when(catalogQueryRepository.getIdTypeByAbbreviation("CC"))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_ID_TYPE_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_ID_TYPE_NOT_FOUND.getMessageKey(),
                    "user.person.id_type_invalid",
                    new Object[]{"CC"}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedCommandService.createPersonEvaluated(person))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.PERSON_ID_TYPE_NOT_FOUND,
                    "user.person.id_type_invalid",
                    "CC");
            verify(personEvaluatedCommandRepository, never()).savePersonEvaluated(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityAlreadyExists cuando la identificación ya existe")
        void should_throwEntityAlreadyExists_when_identificationAlreadyExists() {
            // Arrange
            PersonEvaluated person = buildPersonEvaluated();
            IdentificationType identificationType = person.getIdentificationType();

            when(catalogQueryRepository.getIdTypeByAbbreviation("CC"))
                    .thenReturn(Optional.of(identificationType));
            when(personEvaluatedQueryRepository.existsByIdentification(1L, "1061234567"))
                    .thenReturn(true);
            doThrow(new EntityAlreadyExistsException(
                    ErrorCode.PERSON_IDENTIFICATION_EXISTS.getCode(),
                    ErrorCode.PERSON_IDENTIFICATION_EXISTS.getMessageKey(),
                    "user.person.identification_exists",
                    new Object[]{"1061234567"}))
                .when(resultFormatter).throwEntityAlreadyExists(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedCommandService.createPersonEvaluated(person))
                    .isInstanceOf(EntityAlreadyExistsException.class);

            verify(resultFormatter).throwEntityAlreadyExists(
                    ErrorCode.PERSON_IDENTIFICATION_EXISTS,
                    "user.person.identification_exists",
                    "1061234567");
            verify(personEvaluatedQueryRepository, never()).existsByEmail(anyString());
            verify(personEvaluatedCommandRepository, never()).savePersonEvaluated(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityAlreadyExists cuando el email ya existe")
        void should_throwEntityAlreadyExists_when_emailAlreadyExists() {
            // Arrange
            PersonEvaluated person = buildPersonEvaluated();
            IdentificationType identificationType = person.getIdentificationType();

            when(catalogQueryRepository.getIdTypeByAbbreviation("CC"))
                    .thenReturn(Optional.of(identificationType));
            when(personEvaluatedQueryRepository.existsByIdentification(1L, "1061234567"))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.existsByEmail("juan.garcia@test.com"))
                    .thenReturn(true);
            doThrow(new EntityAlreadyExistsException(
                    ErrorCode.PERSON_EMAIL_EXISTS.getCode(),
                    ErrorCode.PERSON_EMAIL_EXISTS.getMessageKey(),
                    "user.person.email_exists",
                    new Object[]{"juan.garcia@test.com"}))
                .when(resultFormatter).throwEntityAlreadyExists(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedCommandService.createPersonEvaluated(person))
                    .isInstanceOf(EntityAlreadyExistsException.class);

            verify(resultFormatter).throwEntityAlreadyExists(
                    ErrorCode.PERSON_EMAIL_EXISTS,
                    "user.person.email_exists",
                    "juan.garcia@test.com");
            verify(personEvaluatedCommandRepository, never()).savePersonEvaluated(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado por defecto no existe")
        void should_throwEntityNotFound_when_statusNotFound() {
            // Arrange
            PersonEvaluated person = buildPersonEvaluated();
            IdentificationType identificationType = person.getIdentificationType();

            when(catalogQueryRepository.getIdTypeByAbbreviation("CC"))
                    .thenReturn(Optional.of(identificationType));
            when(personEvaluatedQueryRepository.existsByIdentification(1L, "1061234567"))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.existsByEmail("juan.garcia@test.com"))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(
                    StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription()))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_STATUS_NOT_FOUND.getMessageKey(),
                    "user.person.status_not_found",
                    new Object[]{StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription()}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedCommandService.createPersonEvaluated(person))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.PERSON_STATUS_NOT_FOUND,
                    "user.person.status_not_found",
                    StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription());
            verify(personEvaluatedCommandRepository, never()).savePersonEvaluated(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityCreationException cuando falla el guardado")
        void should_throwEntityCreationFailed_when_saveFails() {
            // Arrange
            PersonEvaluated person = buildPersonEvaluated();
            IdentificationType identificationType = person.getIdentificationType();
            StatusPersonEvaluated status = buildDefaultStatus();

            when(catalogQueryRepository.getIdTypeByAbbreviation("CC"))
                    .thenReturn(Optional.of(identificationType));
            when(personEvaluatedQueryRepository.existsByIdentification(1L, "1061234567"))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.existsByEmail("juan.garcia@test.com"))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(
                    StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription()))
                    .thenReturn(Optional.of(status));
            when(personEvaluatedCommandRepository.savePersonEvaluated(any(PersonEvaluated.class)))
                    .thenReturn(Optional.empty());
            doThrow(new EntityCreationException(
                    ErrorCode.ENTITY_CREATION_ERROR.getCode(),
                    ErrorCode.ENTITY_CREATION_ERROR.getMessageKey(),
                    "user.person.creation_failed",
                    new Object[]{"1061234567"}))
                .when(resultFormatter).throwEntityCreationFailed(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedCommandService.createPersonEvaluated(person))
                    .isInstanceOf(EntityCreationException.class);

            verify(resultFormatter).throwEntityCreationFailed(
                    ErrorCode.ENTITY_CREATION_ERROR,
                    "user.person.creation_failed",
                    "1061234567");
        }
    }

    // ==================================================================================
    // updatePersonEvaluated
    // ==================================================================================

    @Nested
    @DisplayName("updatePersonEvaluated")
    class UpdatePersonEvaluated {

        @Test
        @DisplayName("Debe actualizar persona cuando todas las validaciones pasan")
        void should_updatePersonEvaluated_when_allValidationsPass() {
            // Arrange
            PersonEvaluated person = buildPersonEvaluated();
            IdentificationType identificationType = person.getIdentificationType();

            PersonEvaluated updatedPerson = buildPersonEvaluated();
            updatedPerson.setFirstName("JUAN CARLOS");
            updatedPerson.setLastName("GARCIA LOPEZ");
            updatedPerson.setIdentificationType(identificationType);

            when(personEvaluatedQueryRepository.existsById(1L))
                    .thenReturn(true);
            when(catalogQueryRepository.getIdTypeByAbbreviation("CC"))
                    .thenReturn(Optional.of(identificationType));
            when(personEvaluatedQueryRepository.isIdentificationAssignedToDifferentPerson(1L, "1061234567", 1L))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.isEmailAssignedToDifferentPerson("juan.garcia@test.com", 1L))
                    .thenReturn(false);
            when(personEvaluatedCommandRepository.updatePersonEvaluated(any(PersonEvaluated.class)))
                    .thenReturn(Optional.of(updatedPerson));

            // Act
            PersonEvaluated result = personEvaluatedCommandService.updatePersonEvaluated(person);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getFirstName()).isEqualTo("JUAN CARLOS");
            assertThat(result.getLastName()).isEqualTo("GARCIA LOPEZ");

            verify(personEvaluatedCommandRepository).updatePersonEvaluated(personCaptor.capture());
            PersonEvaluated captured = personCaptor.getValue();
            assertThat(captured.getFirstName()).isEqualTo("JUAN CARLOS");
            assertThat(captured.getLastName()).isEqualTo("GARCIA LOPEZ");
            assertThat(captured.getIdentificationType().getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la persona no existe")
        void should_throwEntityNotFound_when_personDoesNotExist() {
            // Arrange
            PersonEvaluated person = buildPersonEvaluated();

            when(personEvaluatedQueryRepository.existsById(1L))
                    .thenReturn(false);
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_NOT_FOUND.getMessageKey(),
                    "user.person.not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedCommandService.updatePersonEvaluated(person))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.PERSON_NOT_FOUND,
                    "user.person.not_found",
                    1L);
            verify(catalogQueryRepository, never()).getIdTypeByAbbreviation(anyString());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el tipo de identificación no existe en catálogo")
        void should_throwEntityNotFound_when_identificationTypeNotFoundOnUpdate() {
            // Arrange
            PersonEvaluated person = buildPersonEvaluated();

            when(personEvaluatedQueryRepository.existsById(1L))
                    .thenReturn(true);
            when(catalogQueryRepository.getIdTypeByAbbreviation("CC"))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_ID_TYPE_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_ID_TYPE_NOT_FOUND.getMessageKey(),
                    "user.person.id_type_invalid",
                    new Object[]{"CC"}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedCommandService.updatePersonEvaluated(person))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.PERSON_ID_TYPE_NOT_FOUND,
                    "user.person.id_type_invalid",
                    "CC");
            verify(personEvaluatedCommandRepository, never()).updatePersonEvaluated(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityAlreadyExists cuando la identificación pertenece a otra persona")
        void should_throwEntityAlreadyExists_when_identificationAssignedToDifferentPerson() {
            // Arrange
            PersonEvaluated person = buildPersonEvaluated();
            IdentificationType identificationType = person.getIdentificationType();

            when(personEvaluatedQueryRepository.existsById(1L))
                    .thenReturn(true);
            when(catalogQueryRepository.getIdTypeByAbbreviation("CC"))
                    .thenReturn(Optional.of(identificationType));
            when(personEvaluatedQueryRepository.isIdentificationAssignedToDifferentPerson(1L, "1061234567", 1L))
                    .thenReturn(true);
            doThrow(new EntityAlreadyExistsException(
                    ErrorCode.PERSON_IDENTIFICATION_EXISTS.getCode(),
                    ErrorCode.PERSON_IDENTIFICATION_EXISTS.getMessageKey(),
                    "user.person.update_identification_exists",
                    new Object[]{"1061234567"}))
                .when(resultFormatter).throwEntityAlreadyExists(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedCommandService.updatePersonEvaluated(person))
                    .isInstanceOf(EntityAlreadyExistsException.class);

            verify(resultFormatter).throwEntityAlreadyExists(
                    ErrorCode.PERSON_IDENTIFICATION_EXISTS,
                    "user.person.update_identification_exists",
                    "1061234567");
            verify(personEvaluatedQueryRepository, never()).isEmailAssignedToDifferentPerson(anyString(), anyLong());
        }

        @Test
        @DisplayName("Debe lanzar EntityAlreadyExists cuando el email pertenece a otra persona")
        void should_throwEntityAlreadyExists_when_emailAssignedToDifferentPerson() {
            // Arrange
            PersonEvaluated person = buildPersonEvaluated();
            IdentificationType identificationType = person.getIdentificationType();

            when(personEvaluatedQueryRepository.existsById(1L))
                    .thenReturn(true);
            when(catalogQueryRepository.getIdTypeByAbbreviation("CC"))
                    .thenReturn(Optional.of(identificationType));
            when(personEvaluatedQueryRepository.isIdentificationAssignedToDifferentPerson(1L, "1061234567", 1L))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.isEmailAssignedToDifferentPerson("juan.garcia@test.com", 1L))
                    .thenReturn(true);
            doThrow(new EntityAlreadyExistsException(
                    ErrorCode.PERSON_EMAIL_EXISTS.getCode(),
                    ErrorCode.PERSON_EMAIL_EXISTS.getMessageKey(),
                    "user.person.update_email_exists",
                    new Object[]{"juan.garcia@test.com"}))
                .when(resultFormatter).throwEntityAlreadyExists(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedCommandService.updatePersonEvaluated(person))
                    .isInstanceOf(EntityAlreadyExistsException.class);

            verify(resultFormatter).throwEntityAlreadyExists(
                    ErrorCode.PERSON_EMAIL_EXISTS,
                    "user.person.update_email_exists",
                    "juan.garcia@test.com");
            verify(personEvaluatedCommandRepository, never()).updatePersonEvaluated(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityCreationException cuando falla la actualización")
        void should_throwEntityCreationFailed_when_updateFails() {
            // Arrange
            PersonEvaluated person = buildPersonEvaluated();
            IdentificationType identificationType = person.getIdentificationType();

            when(personEvaluatedQueryRepository.existsById(1L))
                    .thenReturn(true);
            when(catalogQueryRepository.getIdTypeByAbbreviation("CC"))
                    .thenReturn(Optional.of(identificationType));
            when(personEvaluatedQueryRepository.isIdentificationAssignedToDifferentPerson(1L, "1061234567", 1L))
                    .thenReturn(false);
            when(personEvaluatedQueryRepository.isEmailAssignedToDifferentPerson("juan.garcia@test.com", 1L))
                    .thenReturn(false);
            when(personEvaluatedCommandRepository.updatePersonEvaluated(any(PersonEvaluated.class)))
                    .thenReturn(Optional.empty());
            doThrow(new EntityCreationException(
                    ErrorCode.ENTITY_UPDATE_ERROR.getCode(),
                    ErrorCode.ENTITY_UPDATE_ERROR.getMessageKey(),
                    "user.person.update_failed",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityCreationFailed(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedCommandService.updatePersonEvaluated(person))
                    .isInstanceOf(EntityCreationException.class);

            verify(resultFormatter).throwEntityCreationFailed(
                    ErrorCode.ENTITY_UPDATE_ERROR,
                    "user.person.update_failed",
                    1L);
        }
    }

    // ==================================================================================
    // deletePersonEvaluated
    // ==================================================================================

    @Nested
    @DisplayName("deletePersonEvaluated")
    class DeletePersonEvaluated {

        @Test
        @DisplayName("Debe eliminar persona cuando existe y no tiene registros de batería")
        void should_deletePersonEvaluated_when_personExistsAndHasNoRecords() {
            // Arrange
            when(personEvaluatedQueryRepository.existsById(1L))
                    .thenReturn(true);
            when(batteryManagementRecordQueryRepository.existsByPersonEvaluatedId(1L))
                    .thenReturn(false);

            // Act
            personEvaluatedCommandService.deletePersonEvaluated(1L);

            // Assert
            verify(personEvaluatedCommandRepository).deletePersonEvaluatedById(1L);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la persona no existe")
        void should_throwEntityNotFound_when_personDoesNotExistOnDelete() {
            // Arrange
            when(personEvaluatedQueryRepository.existsById(1L))
                    .thenReturn(false);
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_NOT_FOUND.getMessageKey(),
                    "user.person.not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedCommandService.deletePersonEvaluated(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.PERSON_NOT_FOUND,
                    "user.person.not_found",
                    1L);
            verify(batteryManagementRecordQueryRepository, never()).existsByPersonEvaluatedId(anyLong());
            verify(personEvaluatedCommandRepository, never()).deletePersonEvaluatedById(anyLong());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando la persona tiene registros de batería")
        void should_throwBusinessRuleViolation_when_personHasBatteryRecords() {
            // Arrange
            when(personEvaluatedQueryRepository.existsById(1L))
                    .thenReturn(true);
            when(batteryManagementRecordQueryRepository.existsByPersonEvaluatedId(1L))
                    .thenReturn(true);
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.PERSON_WITH_BATTERY_RECORD.getCode(),
                    ErrorCode.PERSON_WITH_BATTERY_RECORD.getMessageKey(),
                    "user.person.has_records",
                    new Object[]{1L}))
                .when(resultFormatter).throwBusinessRuleViolation(any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedCommandService.deletePersonEvaluated(1L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.PERSON_WITH_BATTERY_RECORD,
                    "user.person.has_records",
                    1L);
            verify(personEvaluatedCommandRepository, never()).deletePersonEvaluatedById(anyLong());
        }
    }
}
