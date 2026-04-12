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
import com.unicuaca.asst.unicauca_asst.common.domain.models.JobPositionType;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedDetailsQueryRepository;

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
class PersonEvaluatedDetailsQueryServiceTest {

    @Mock
    private PersonEvaluatedDetailsQueryRepository personEvaluatedDetailsQueryRepository;

    @Mock
    private BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private PersonEvaluatedDetailsQueryService personEvaluatedDetailsQueryService;

    // ==================================================================================
    // getMetaByBatteryManagementRecordId
    // ==================================================================================

    @Nested
    @DisplayName("getMetaByBatteryManagementRecordId")
    class GetMetaByBatteryManagementRecordId {

        @Test
        @DisplayName("Debe retornar metadata cuando la batería y los detalles existen")
        void should_returnMeta_when_batteryAndDetailsExist() {
            // Arrange
            BatteryManagementRecord batteryRecord = BatteryManagementRecord.builder()
                    .id(1L).build();
            PersonEvaluatedDetails details = PersonEvaluatedDetails.builder()
                    .id(10L)
                    .jobPositionType(JobPositionType.builder().id(1L).name("Profesional").build())
                    .build();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(personEvaluatedDetailsQueryRepository.getByBatteryManagementRecordId(1L))
                    .thenReturn(Optional.of(details));

            // Act
            PersonEvaluatedDetails result = personEvaluatedDetailsQueryService
                    .getMetaByBatteryManagementRecordId(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(10L);
            assertThat(result.getBatteryManagementRecord()).isNotNull();
            assertThat(result.getJobPositionType()).isNotNull();
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la batería no existe")
        void should_throwEntityNotFound_when_batteryNotFound() {
            // Arrange
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.battery_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsQueryService
                    .getMetaByBatteryManagementRecordId(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.person_evaluated_details.battery_not_found",
                    1L);
            verify(personEvaluatedDetailsQueryRepository, never())
                    .getByBatteryManagementRecordId(anyLong());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando los detalles no existen para la batería")
        void should_throwEntityNotFound_when_detailsNotFound() {
            // Arrange
            BatteryManagementRecord batteryRecord = BatteryManagementRecord.builder()
                    .id(1L).build();
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(personEvaluatedDetailsQueryRepository.getByBatteryManagementRecordId(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_DETAILS_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.query_by_record_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsQueryService
                    .getMetaByBatteryManagementRecordId(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND,
                    "user.person_evaluated_details.query_by_record_not_found",
                    1L);
        }
    }

    // ==================================================================================
    // getPersonEvaluatedDetailsById
    // ==================================================================================

    @Nested
    @DisplayName("getPersonEvaluatedDetailsById")
    class GetPersonEvaluatedDetailsById {

        @Test
        @DisplayName("Debe retornar detalles completos cuando existen")
        void should_returnDetails_when_foundById() {
            // Arrange
            PersonEvaluatedDetails details = PersonEvaluatedDetails.builder()
                    .id(10L).build();
            when(personEvaluatedDetailsQueryRepository.getByIdWithAll(10L))
                    .thenReturn(Optional.of(details));

            // Act
            PersonEvaluatedDetails result = personEvaluatedDetailsQueryService
                    .getPersonEvaluatedDetailsById(10L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(10L);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando los detalles no existen por ID")
        void should_throwEntityNotFound_when_detailsNotFoundById() {
            // Arrange
            when(personEvaluatedDetailsQueryRepository.getByIdWithAll(10L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_DETAILS_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.query_not_found",
                    new Object[]{10L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedDetailsQueryService
                    .getPersonEvaluatedDetailsById(10L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND,
                    "user.person_evaluated_details.query_not_found",
                    10L);
        }
    }
}
