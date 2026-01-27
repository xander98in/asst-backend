package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordInformationResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.BatteryManagementRecordMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordInformation;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.BatteryManagementRecordQueryCUInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Implementación del manejador de consultas para registros de gestión de baterías.
 */
@RequiredArgsConstructor
@Component
public class BatteryManagementRecordQueryHandlerImpl implements BatteryManagementRecordQueryHandler{

    private final BatteryManagementRecordQueryCUInputPort batteryManagementRecordQueryCUInputPort;
    private final BatteryManagementRecordMapper batteryManagementRecordMapper;

    /**
     * Lista registros de gestión de baterías de forma paginada. (Excluye registros con estado "Cerrado")
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @return una página de {@link BatteryManagementRecordInformationResponseDTO}
     */
    @Override
    public Page<BatteryManagementRecordInformationResponseDTO> listPaginatedRecords(Integer page, Integer size) {
        Page<BatteryManagementRecordInformation> infoPage = batteryManagementRecordQueryCUInputPort.listPaginatedRecords(page, size);
        var dtoList = infoPage.getContent().stream()
            .map(batteryManagementRecordMapper::toInformationResponseDTO)
            .toList();

        return new PageImpl<>(
            dtoList,
            infoPage.getPageable(),
            infoPage.getTotalElements()
        );
    }

    /**
     * Lista registros de gestión de baterías de forma paginada, filtrando por prefijo de identificación.
     * (Excluye registros con estado "Cerrado")
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param term prefijo del número de identificación para filtrar (opcional)
     * @return una página de {@link BatteryManagementRecordInformationResponseDTO}
     */
    @Override
    public Page<BatteryManagementRecordInformationResponseDTO> listPaginatedByIdentificationPrefix(Integer page, Integer size, String term) {
        Page<BatteryManagementRecordInformation> infoPage =
            batteryManagementRecordQueryCUInputPort.listPaginatedByIdentificationPrefix(page, size, term);

        var dtoList = infoPage.getContent().stream()
            .map(batteryManagementRecordMapper::toInformationResponseDTO)
            .toList();

        return new PageImpl<>(dtoList, infoPage.getPageable(), infoPage.getTotalElements());
    }

    /**
     * Lista registros de gestión de baterías de forma paginada, filtrando por término de búsqueda.
     * (Excluye registros con estado "Cerrado")
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param term término de búsqueda para filtrar por número de identificación o nombre del área de trabajo (opcional)
     * @return una página de {@link BatteryManagementRecordInformationResponseDTO}
     */
    @Override
    public Page<BatteryManagementRecordInformationResponseDTO> listPagedWithSearchTerm(Integer page, Integer size, String term) {
        Page<BatteryManagementRecordInformation> infoPage =
            batteryManagementRecordQueryCUInputPort.listPaginatedWithSearchTerm(page, size, term);

        var dtoList = infoPage.getContent().stream()
            .map(batteryManagementRecordMapper::toInformationResponseDTO)
            .toList();

        return new PageImpl<>(dtoList, infoPage.getPageable(), infoPage.getTotalElements());
    }

    /**
     * Obtiene la información detallada de un registro de gestión de baterías por su ID.
     *
     * @param id ID del registro de gestión de baterías
     * @return DTO con la información detallada del registro
     */
    @Override
    public BatteryManagementRecordInformationResponseDTO getRecordInformationById(Long id) {
        BatteryManagementRecordInformation info = batteryManagementRecordQueryCUInputPort.getRecordInformationById(id);
        return batteryManagementRecordMapper.toInformationResponseDTO(info);
    }
}
