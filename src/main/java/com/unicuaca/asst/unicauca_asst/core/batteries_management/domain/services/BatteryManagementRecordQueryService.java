package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordInformation;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.BatteryManagementRecordQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedDetailsQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de dominio para la consulta de registros de gestión de baterías.
 * 
 * <p>Implementa la lógica para listar y obtener información detallada de los procesos de evaluación.
 * Utiliza i18n para los mensajes de error técnicos y de usuario, garantizando trazabilidad y claridad.</p>
 */
@RequiredArgsConstructor
public class BatteryManagementRecordQueryService implements BatteryManagementRecordQueryCUInputPort {

    private final BatteryManagementRecordQueryRepository recordQueryRepository;
    private final PersonEvaluatedDetailsQueryRepository detailsQueryRepository;
    private final ResultFormatterOutputPort resultFormatterOutputPort;

    /**
     * Lista registros de gestión de baterías de forma paginada. (Excluye registros con estado "Cerrado").
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @return una página de {@link BatteryManagementRecordInformation}
     */
    @Override
    public Page<BatteryManagementRecordInformation> listPaginatedRecords(Integer page, Integer size) {
        Page<BatteryManagementRecord> recordPage = recordQueryRepository.listPagedExcludingStatus(
            BatteryManagementRecordStatusCode.CLOSED.getDescription(), page, size, Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return mapToInformationPage(recordPage);
    }

    /**
     * Lista registros de gestión de baterías de forma paginada, filtrando por prefijo de identificación.
     * (Excluye registros con estado "Cerrado").
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param term prefijo del número de identificación para filtrar (opcional)
     * @return una página de {@link BatteryManagementRecordInformation}
     */
    @Override
    public Page<BatteryManagementRecordInformation> listPaginatedByIdentificationPrefix(Integer page, Integer size, String term) {
        String normalizedTerm = normalizeTerm(term);
        Page<BatteryManagementRecord> recordPage = recordQueryRepository.listPaginatedByIdentificationPrefix(
            BatteryManagementRecordStatusCode.CLOSED.getDescription(),
            normalizedTerm,
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return mapToInformationPage(recordPage);
    }

    /**
     * Lista registros de gestión de baterías de forma paginada, filtrando por término de búsqueda
     * en número de identificación o nombre del área de trabajo.
     * (Excluye registros con estado "Cerrado").
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param searchTerm término de búsqueda para filtrar (opcional)
     * @return una página de {@link BatteryManagementRecordInformation}
     */
    @Override
    public Page<BatteryManagementRecordInformation> listPaginatedWithSearchTerm(Integer page, Integer size, String searchTerm) {
        String normalizedTerm = normalizeTerm(searchTerm);
        Page<BatteryManagementRecord> recordPage = recordQueryRepository.listPagedExcludingStatusWithSearchTerm(
            BatteryManagementRecordStatusCode.CLOSED.getDescription(),
            normalizedTerm,
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return mapToInformationPage(recordPage);
    }

    /**
     * Lista registros de gestión de baterías de forma paginada que tienen el estado "Cerrado".
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @return una página de {@link BatteryManagementRecordInformation}
     */
    @Override
    public Page<BatteryManagementRecordInformation> listPaginatedClosedRecords(Integer page, Integer size) {
        Page<BatteryManagementRecord> recordPage = recordQueryRepository.listPagedByStatus(
            BatteryManagementRecordStatusCode.CLOSED.getDescription(),
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return mapToInformationPage(recordPage);
    }

    /**
     * Lista registros de gestión de baterías de forma paginada que tienen el estado "Cerrado",
     * filtrando por término de búsqueda en número de identificación o nombre del área de trabajo.
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param searchTerm término de búsqueda para filtrar (opcional)
     * @return una página de {@link BatteryManagementRecordInformation}
     */
    @Override
    public Page<BatteryManagementRecordInformation> listPaginatedClosedRecordsWithSearchTerm(Integer page, Integer size, String searchTerm) {
        String normalizedTerm = normalizeTerm(searchTerm);
        Page<BatteryManagementRecord> recordPage = recordQueryRepository.listPagedByStatusWithSearchTerm(
            BatteryManagementRecordStatusCode.CLOSED.getDescription(),
            normalizedTerm,
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return mapToInformationPage(recordPage);
    }

    /**
     * Lista baterías cerradas paginadas con filtros opcionales múltiples.
     *
     * @param identificationNumber número de identificación (prefijo, puede ser null)
     * @param workAreaName         área de trabajo (contenido parcial, puede ser null)
     * @param dateFrom             fecha inicial del rango (puede ser null)
     * @param dateTo               fecha final del rango (puede ser null)
     * @param identificationTypeId ID del tipo de identificación (puede ser null)
     * @param jobPositionTypeId    ID del tipo de cargo (puede ser null)
     * @param intralaboralForm     forma intralaboral: "A" (cargos 1-2) o "B" (cargos 3-4), puede ser null
     * @param page                 número de página
     * @param size                 tamaño de página
     * @return una página de {@link BatteryManagementRecordInformation}
     */
    @Override
    public Page<BatteryManagementRecordInformation> listClosedWithMultipleFilters(
        String identificationNumber, String workAreaName,
        LocalDateTime dateFrom, LocalDateTime dateTo,
        Long identificationTypeId, Long jobPositionTypeId,
        String intralaboralForm,
        Integer page, Integer size
    ) {
        Page<BatteryManagementRecord> recordPage = recordQueryRepository.listClosedWithMultipleFilters(
            identificationNumber, workAreaName, dateFrom, dateTo,
            identificationTypeId, jobPositionTypeId, intralaboralForm, page, size
        );
        return mapToInformationPage(recordPage);
    }

    /**
     * Obtiene la información enriquecida de un proceso de evaluación por su ID.
     *
     * @param id identificador único del registro de batería
     * @return información detallada que incluye datos de la persona y su área de trabajo
     */
    @Override
    public BatteryManagementRecordInformation getRecordInformationById(Long id) {
        Optional<BatteryManagementRecord> recordOpt = recordQueryRepository.getBatteryManagementRecordById(id);
        if (recordOpt.isEmpty()) {
            resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.BATTERY_RECORD_NOT_FOUND,
                "user.battery.query_not_found",
                id
            );
        }
        BatteryManagementRecord record = recordOpt.get();

        String workArea = detailsQueryRepository
            .getWorkAreaNameByBatteryManagementRecordId(record.getId())
            .orElse(null);

        PersonEvaluatedDetails details = PersonEvaluatedDetails.builder()
            .workAreaName(workArea)
            .build();

        return BatteryManagementRecordInformation.builder()
            .id(record.getId())
            .createdAt(record.getCreatedAt())
            .updatedAt(record.getUpdatedAt())
            .status(record.getStatus())
            .personEvaluated(record.getPersonEvaluated())
            .personEvaluatedDetails(details)
            .build();
    }

    /**
     * Mapea y enriquece una página de registros con información sociodemográfica adicional.
     * @param recordPage página de registros de gestión de baterías obtenida del repositorio
     * @return página de información enriquecida para cada registro, lista para ser presentada al usuario
     */
    private Page<BatteryManagementRecordInformation> mapToInformationPage(Page<BatteryManagementRecord> recordPage) {
        List<BatteryManagementRecordInformation> content =
            recordPage.getContent().stream()
                .map(record -> {
                    String workArea = detailsQueryRepository
                        .getWorkAreaNameByBatteryManagementRecordId(record.getId())
                        .orElse(null);

                    PersonEvaluatedDetails details = PersonEvaluatedDetails.builder()
                        .workAreaName(workArea)
                        .build();

                    return BatteryManagementRecordInformation.builder()
                        .id(record.getId())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .status(record.getStatus())
                        .personEvaluated(record.getPersonEvaluated())
                        .personEvaluatedDetails(details)
                        .build();
                })
                .toList();

        return new PageImpl<>(content, recordPage.getPageable(), recordPage.getTotalElements());
    }

    /**
     * Limpia y normaliza el término de búsqueda.
     * @param term término de búsqueda ingresado por el usuario, que puede ser nulo o contener solo espacios
     * @return el término normalizado (sin espacios al inicio o al final), o null si el término es nulo o solo espacios
     */
    private String normalizeTerm(String term) {
        if (term == null) return null;
        String normalized = term.trim();
        return normalized.isBlank() ? null : normalized;
    }
}
