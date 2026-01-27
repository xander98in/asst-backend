package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordInformationResponseDTO;
import org.springframework.data.domain.Page;

/**
 * Manejador de consultas para registros de gestión de baterías.
 */
public interface BatteryManagementRecordQueryHandler {

    /**
     * Lista registros de gestión de baterías de forma paginada. (Excluye registros con estado "Cerrado")
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @return una página de {@link BatteryManagementRecordInformationResponseDTO}
     */
    Page<BatteryManagementRecordInformationResponseDTO> listPaginatedRecords(Integer page, Integer size);

    /**
     * Lista registros de gestión de baterías de forma paginada, filtrando por prefijo de identificación.
     * (Excluye registros con estado "Cerrado")
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param term prefijo del número de identificación para filtrar (opcional)
     * @return una página de {@link BatteryManagementRecordInformationResponseDTO}
     */
    Page<BatteryManagementRecordInformationResponseDTO> listPaginatedByIdentificationPrefix(Integer page, Integer size, String term);

    /**
     * Lista registros de gestión de baterías de forma paginada, filtrando por término de búsqueda.
     * (Excluye registros con estado "Cerrado")
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param term término de búsqueda para filtrar (opcional)
     * @return una página de {@link BatteryManagementRecordInformationResponseDTO}
     */
    Page<BatteryManagementRecordInformationResponseDTO> listPagedWithSearchTerm(Integer page, Integer size, String term);

    /**
     * Obtiene la información detallada de un registro de gestión de baterías por su ID.
     *
     * @param id ID del registro de gestión de baterías
     * @return DTO con la información detallada del registro
     */
    BatteryManagementRecordInformationResponseDTO getRecordInformationById(Long id);
}
