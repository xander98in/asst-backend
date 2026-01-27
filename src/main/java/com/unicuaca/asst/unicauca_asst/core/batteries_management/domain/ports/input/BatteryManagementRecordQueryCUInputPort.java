package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordInformation;
import org.springframework.data.domain.Page;

/**
 * Puerto de entrada para consultas de registros de gestión de baterías.
 */
public interface BatteryManagementRecordQueryCUInputPort {

    /**
     * Lista registros de gestión de baterías de forma paginada. (Excluye registros con estado "Cerrado").
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @return una página de {@link BatteryManagementRecordInformation}
     */
    Page<BatteryManagementRecordInformation> listPaginatedRecords(Integer page, Integer size);

    /**
     * Lista registros de gestión de baterías de forma paginada, filtrando por prefijo de identificación.
     * (Excluye registros con estado "Cerrado").
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param term prefijo del número de identificación para filtrar (opcional)
     * @return una página de {@link BatteryManagementRecordInformation}
     */
    Page<BatteryManagementRecordInformation> listPaginatedByIdentificationPrefix(Integer page, Integer size, String term);

    /**
     * Lista registros de gestión de baterías paginados, filtrando por término de búsqueda en número de identificación
     * o nombre del área de trabajo. (Excluye registros con estado "Cerrado").
     *
     * @param page número de página (0-indexed)
     * @param size tamaño de página
     * @param searchTerm término de búsqueda para filtrar por número de identificación
     * @return una página de {@link BatteryManagementRecordInformation}
     */
    Page<BatteryManagementRecordInformation> listPaginatedWithSearchTerm(Integer page, Integer size, String searchTerm);

    /**
     * Obtiene la información detallada de un registro de gestión de baterías por su ID.
     *
     * @param id ID del registro de gestión de baterías
     * @return DTO con la información detallada del registro
     */
    BatteryManagementRecordInformation getRecordInformationById(Long id);
}
