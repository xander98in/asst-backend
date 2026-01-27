package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordInformation;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.BatteryManagementRecordQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedDetailsQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Servicio de consulta para registros de gestión de baterías.
 * Implementa las operaciones definidas en el puerto de entrada {@link BatteryManagementRecordQueryCUInputPort}.
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
            "Cerrado", page, size, Sort.by(Sort.Direction.DESC, "createdAt")
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
            "Cerrado",
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
            "Cerrado",
            normalizedTerm,
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return mapToInformationPage(recordPage);
    }

    /**
     * Obtiene la información detallada de un registro de gestión de baterías por su ID.
     *
     * @param id ID del registro de gestión de baterías
     * @return DTO con la información detallada del registro
     */
    @Override
    public BatteryManagementRecordInformation getRecordInformationById(Long id) {
        BatteryManagementRecord record = recordQueryRepository.getBatteryManagementRecordById(id)
            .orElseGet(() -> {
                resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "El registro de gestión de baterías con ID " + id + " no fue encontrado.")
                );
                return null;
            });

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
     * Centraliza el mapeo: Page<BatteryManagementRecord> -> Page<BatteryManagementRecordInformation>
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

        return new PageImpl<>(
            content,
            recordPage.getPageable(),
            recordPage.getTotalElements()
        );
    }

    /**
     * Normaliza el término de búsqueda: trim + convierte vacío a null.
     */
    private String normalizeTerm(String term) {
        if (term == null) return null;
        String normalized = term.trim();
        return normalized.isBlank() ? null : normalized;
    }
}
