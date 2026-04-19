package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordInformation;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedDetailsQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatteryManagementRecordQueryServiceTest {

    @Mock
    private BatteryManagementRecordQueryRepository recordQueryRepository;

    @Mock
    private PersonEvaluatedDetailsQueryRepository detailsQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatterOutputPort;

    @InjectMocks
    private BatteryManagementRecordQueryService batteryManagementRecordQueryService;

    private static final String CLOSED_STATUS = BatteryManagementRecordStatusCode.CLOSED.getDescription();
    private static final String IN_PROCESSING_STATUS = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();

    private BatteryManagementRecord buildBatteryManagementRecord() {
        return BatteryManagementRecord.builder()
                .id(1L)
                .personEvaluated(PersonEvaluated.builder().id(1L).firstName("JUAN").build())
                .status(BatteryManagementRecordStatus.builder()
                        .id(2L).name(IN_PROCESSING_STATUS).build())
                .build();
    }

    // ==================================================================================
    // listPaginatedRecords
    // ==================================================================================

    @Nested
    @DisplayName("listPaginatedRecords")
    class ListPaginatedRecords {

        @Test
        @DisplayName("Debe retornar página con registros excluyendo estado Cerrado")
        void should_returnPage_when_listPaginatedRecords() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            Page<BatteryManagementRecord> page = new PageImpl<>(List.of(record));
            when(recordQueryRepository.listPagedExcludingStatus(
                    eq(CLOSED_STATUS), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);
            when(detailsQueryRepository.getWorkAreaNameByBatteryManagementRecordId(1L))
                    .thenReturn(Optional.of("Tecnología"));

            // Act
            Page<BatteryManagementRecordInformation> result =
                    batteryManagementRecordQueryService.listPaginatedRecords(0, 10);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getPersonEvaluatedDetails().getWorkAreaName())
                    .isEqualTo("Tecnología");
        }

        @Test
        @DisplayName("Debe retornar página vacía cuando no hay registros")
        void should_returnEmptyPage_when_noRecords() {
            // Arrange
            Page<BatteryManagementRecord> emptyPage = new PageImpl<>(List.of());
            when(recordQueryRepository.listPagedExcludingStatus(
                    eq(CLOSED_STATUS), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(emptyPage);

            // Act
            Page<BatteryManagementRecordInformation> result =
                    batteryManagementRecordQueryService.listPaginatedRecords(0, 10);

            // Assert
            assertThat(result.getContent()).isEmpty();
        }
    }

    // ==================================================================================
    // listPaginatedByIdentificationPrefix
    // ==================================================================================

    @Nested
    @DisplayName("listPaginatedByIdentificationPrefix")
    class ListPaginatedByIdentificationPrefix {

        @Test
        @DisplayName("Debe retornar página filtrando por prefijo de identificación (con trim)")
        void should_returnPage_when_listPaginatedByIdentificationPrefix() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            Page<BatteryManagementRecord> page = new PageImpl<>(List.of(record));
            when(recordQueryRepository.listPaginatedByIdentificationPrefix(
                    eq(CLOSED_STATUS), eq("106"), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);
            when(detailsQueryRepository.getWorkAreaNameByBatteryManagementRecordId(1L))
                    .thenReturn(Optional.of("Tecnología"));

            // Act
            Page<BatteryManagementRecordInformation> result =
                    batteryManagementRecordQueryService
                            .listPaginatedByIdentificationPrefix(0, 10, "  106  ");

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        @DisplayName("Debe normalizar término null a null")
        void should_passNull_when_termIsNull() {
            // Arrange
            Page<BatteryManagementRecord> page = new PageImpl<>(List.of());
            when(recordQueryRepository.listPaginatedByIdentificationPrefix(
                    eq(CLOSED_STATUS), isNull(), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);

            // Act
            Page<BatteryManagementRecordInformation> result =
                    batteryManagementRecordQueryService
                            .listPaginatedByIdentificationPrefix(0, 10, null);

            // Assert
            assertThat(result.getContent()).isEmpty();
        }

        @Test
        @DisplayName("Debe normalizar término en blanco a null")
        void should_passNull_when_termIsBlank() {
            // Arrange
            Page<BatteryManagementRecord> page = new PageImpl<>(List.of());
            when(recordQueryRepository.listPaginatedByIdentificationPrefix(
                    eq(CLOSED_STATUS), isNull(), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);

            // Act
            Page<BatteryManagementRecordInformation> result =
                    batteryManagementRecordQueryService
                            .listPaginatedByIdentificationPrefix(0, 10, "   ");

            // Assert
            assertThat(result.getContent()).isEmpty();
        }
    }

    // ==================================================================================
    // listPaginatedWithSearchTerm
    // ==================================================================================

    @Nested
    @DisplayName("listPaginatedWithSearchTerm")
    class ListPaginatedWithSearchTerm {

        @Test
        @DisplayName("Debe retornar página filtrando por término de búsqueda (con trim)")
        void should_returnPage_when_listPaginatedWithSearchTerm() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            Page<BatteryManagementRecord> page = new PageImpl<>(List.of(record));
            when(recordQueryRepository.listPagedExcludingStatusWithSearchTerm(
                    eq(CLOSED_STATUS), eq("juan"), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);
            when(detailsQueryRepository.getWorkAreaNameByBatteryManagementRecordId(1L))
                    .thenReturn(Optional.of("Tecnología"));

            // Act
            Page<BatteryManagementRecordInformation> result =
                    batteryManagementRecordQueryService
                            .listPaginatedWithSearchTerm(0, 10, "  juan  ");

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
        }
    }

    // ==================================================================================
    // listPaginatedClosedRecords
    // ==================================================================================

    @Nested
    @DisplayName("listPaginatedClosedRecords")
    class ListPaginatedClosedRecords {

        @Test
        @DisplayName("Debe retornar página con registros en estado Cerrado")
        void should_returnPage_when_listPaginatedClosedRecords() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            Page<BatteryManagementRecord> page = new PageImpl<>(List.of(record));
            when(recordQueryRepository.listPagedByStatus(
                    eq(CLOSED_STATUS), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);
            when(detailsQueryRepository.getWorkAreaNameByBatteryManagementRecordId(1L))
                    .thenReturn(Optional.of("Tecnología"));

            // Act
            Page<BatteryManagementRecordInformation> result =
                    batteryManagementRecordQueryService.listPaginatedClosedRecords(0, 10);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
        }
    }

    // ==================================================================================
    // listPaginatedClosedRecordsWithSearchTerm
    // ==================================================================================

    @Nested
    @DisplayName("listPaginatedClosedRecordsWithSearchTerm")
    class ListPaginatedClosedRecordsWithSearchTerm {

        @Test
        @DisplayName("Debe retornar página Cerrados filtrando por término de búsqueda")
        void should_returnPage_when_listPaginatedClosedRecordsWithSearchTerm() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            Page<BatteryManagementRecord> page = new PageImpl<>(List.of(record));
            when(recordQueryRepository.listPagedByStatusWithSearchTerm(
                    eq(CLOSED_STATUS), eq("juan"), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);
            when(detailsQueryRepository.getWorkAreaNameByBatteryManagementRecordId(1L))
                    .thenReturn(Optional.of("Tecnología"));

            // Act
            Page<BatteryManagementRecordInformation> result =
                    batteryManagementRecordQueryService
                            .listPaginatedClosedRecordsWithSearchTerm(0, 10, "  juan  ");

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
        }
    }

    // ==================================================================================
    // listClosedWithMultipleFilters
    // ==================================================================================

    @Nested
    @DisplayName("listClosedWithMultipleFilters")
    class ListClosedWithMultipleFilters {

        @Test
        @DisplayName("Debe retornar página Cerrados aplicando todos los filtros")
        void should_returnPage_when_allFiltersProvided() {
            // Arrange
            LocalDateTime from = LocalDateTime.of(2025, 1, 1, 0, 0);
            LocalDateTime to = LocalDateTime.of(2025, 12, 31, 23, 59);
            BatteryManagementRecord record = buildBatteryManagementRecord();
            Page<BatteryManagementRecord> page = new PageImpl<>(List.of(record));
            when(recordQueryRepository.listClosedWithMultipleFilters(
                    eq("106"), eq("Tecnología"),
                    eq(from), eq(to),
                    eq(1L), eq(2L),
                    eq("A"),
                    eq(0), eq(10)))
                    .thenReturn(page);
            when(detailsQueryRepository.getWorkAreaNameByBatteryManagementRecordId(1L))
                    .thenReturn(Optional.of("Tecnología"));

            // Act
            Page<BatteryManagementRecordInformation> result =
                    batteryManagementRecordQueryService.listClosedWithMultipleFilters(
                            "106", "Tecnología", from, to, 1L, 2L, "A", 0, 10);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getPersonEvaluatedDetails().getWorkAreaName())
                    .isEqualTo("Tecnología");
        }

        @Test
        @DisplayName("Debe retornar página vacía cuando todos los filtros son null")
        void should_returnEmptyPage_when_allFiltersAreNull() {
            // Arrange
            Page<BatteryManagementRecord> emptyPage = new PageImpl<>(List.of());
            when(recordQueryRepository.listClosedWithMultipleFilters(
                    isNull(), isNull(), isNull(), isNull(),
                    isNull(), isNull(), isNull(),
                    eq(0), eq(10)))
                    .thenReturn(emptyPage);

            // Act
            Page<BatteryManagementRecordInformation> result =
                    batteryManagementRecordQueryService.listClosedWithMultipleFilters(
                            null, null, null, null, null, null, null, 0, 10);

            // Assert
            assertThat(result.getContent()).isEmpty();
        }
    }

    // ==================================================================================
    // listByAnalysisSpaceWithMultipleFilters
    // ==================================================================================

    @Nested
    @DisplayName("listByAnalysisSpaceWithMultipleFilters")
    class ListByAnalysisSpaceWithMultipleFilters {

        @Test
        @DisplayName("Debe retornar página de baterías del espacio de análisis con filtros")
        void should_returnPage_when_filteredBySpace() {
            // Arrange
            Long spaceId = 5L;
            LocalDateTime from = LocalDateTime.of(2025, 1, 1, 0, 0);
            LocalDateTime to = LocalDateTime.of(2025, 12, 31, 23, 59);
            BatteryManagementRecord record = buildBatteryManagementRecord();
            Page<BatteryManagementRecord> page = new PageImpl<>(List.of(record));
            when(recordQueryRepository.listByAnalysisSpaceWithMultipleFilters(
                    eq(spaceId),
                    eq("106"), eq("Tecnología"),
                    eq(from), eq(to),
                    eq(1L), eq(2L),
                    eq("B"),
                    eq(0), eq(10)))
                    .thenReturn(page);
            when(detailsQueryRepository.getWorkAreaNameByBatteryManagementRecordId(1L))
                    .thenReturn(Optional.of("Tecnología"));

            // Act
            Page<BatteryManagementRecordInformation> result =
                    batteryManagementRecordQueryService.listByAnalysisSpaceWithMultipleFilters(
                            spaceId, "106", "Tecnología", from, to, 1L, 2L, "B", 0, 10);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        @DisplayName("Debe retornar página vacía cuando el espacio no tiene baterías")
        void should_returnEmptyPage_when_noBatteriesInSpace() {
            // Arrange
            Long spaceId = 5L;
            Page<BatteryManagementRecord> emptyPage = new PageImpl<>(List.of());
            when(recordQueryRepository.listByAnalysisSpaceWithMultipleFilters(
                    eq(spaceId),
                    isNull(), isNull(), isNull(), isNull(),
                    isNull(), isNull(), isNull(),
                    eq(0), eq(10)))
                    .thenReturn(emptyPage);

            // Act
            Page<BatteryManagementRecordInformation> result =
                    batteryManagementRecordQueryService.listByAnalysisSpaceWithMultipleFilters(
                            spaceId, null, null, null, null, null, null, null, 0, 10);

            // Assert
            assertThat(result.getContent()).isEmpty();
        }
    }

    // ==================================================================================
    // getRecordInformationById
    // ==================================================================================

    @Nested
    @DisplayName("getRecordInformationById")
    class GetRecordInformationById {

        @Test
        @DisplayName("Debe retornar información del registro con área de trabajo")
        void should_returnRecordInformation_when_foundById() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            when(recordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(detailsQueryRepository.getWorkAreaNameByBatteryManagementRecordId(1L))
                    .thenReturn(Optional.of("Tecnología"));

            // Act
            BatteryManagementRecordInformation result =
                    batteryManagementRecordQueryService.getRecordInformationById(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getPersonEvaluated().getFirstName()).isEqualTo("JUAN");
            assertThat(result.getPersonEvaluatedDetails().getWorkAreaName())
                    .isEqualTo("Tecnología");
        }

        @Test
        @DisplayName("Debe retornar información con workArea null cuando no hay detalles")
        void should_returnRecordInformation_when_workAreaNotFound() {
            // Arrange
            BatteryManagementRecord record = buildBatteryManagementRecord();
            when(recordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(record));
            when(detailsQueryRepository.getWorkAreaNameByBatteryManagementRecordId(1L))
                    .thenReturn(Optional.empty());

            // Act
            BatteryManagementRecordInformation result =
                    batteryManagementRecordQueryService.getRecordInformationById(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getPersonEvaluatedDetails().getWorkAreaName()).isNull();
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el registro no existe")
        void should_throwEntityNotFound_when_recordNotFoundById() {
            // Arrange
            when(recordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getMessageKey(),
                    "user.battery.query_not_found",
                    new Object[]{1L}))
                .when(resultFormatterOutputPort).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> batteryManagementRecordQueryService
                    .getRecordInformationById(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatterOutputPort).throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.battery.query_not_found",
                    1L);
        }
    }
}
