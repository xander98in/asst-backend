package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordInformationResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

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
     * Lista registros de gestión de baterías de forma paginada que tienen el estado "Cerrado".
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @return una página de {@link BatteryManagementRecordInformationResponseDTO}
     */
    Page<BatteryManagementRecordInformationResponseDTO> listPaginatedClosedRecords(Integer page, Integer size);

    /**
     * Lista registros de gestión de baterías de forma paginada que tienen el estado "Cerrado",
     * filtrando por término de búsqueda.
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param term término de búsqueda (identificación o área)
     * @return una página de {@link BatteryManagementRecordInformationResponseDTO}
     */
    Page<BatteryManagementRecordInformationResponseDTO> listPaginatedClosedRecordsWithSearchTerm(Integer page, Integer size, String term);

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
     * @return una página de {@link BatteryManagementRecordInformationResponseDTO}
     */
    Page<BatteryManagementRecordInformationResponseDTO> listClosedWithMultipleFilters(
        String identificationNumber, String workAreaName,
        LocalDateTime dateFrom, LocalDateTime dateTo,
        Long identificationTypeId, Long jobPositionTypeId,
        String intralaboralForm,
        Integer page, Integer size
    );

    /**
     * Obtiene la información detallada de un registro de gestión de baterías por su ID.
     *
     * @param id ID del registro de gestión de baterías
     * @return DTO con la información detallada del registro
     */
    BatteryManagementRecordInformationResponseDTO getRecordInformationById(Long id);
}
