package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordInformation;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

/**
 * Puerto de entrada para los casos de uso de consulta sobre registros de gestión de baterías.
 *
 * <p>Define las operaciones de tipo "Query" del modelo {@link BatteryManagementRecordInformation},
 * incluyendo listados paginados, búsquedas con filtros múltiples y obtención por identificador.
 * Permite que los adaptadores de entrada interactúen con la lógica de negocio sin acoplarse
 * directamente a su implementación.</p>
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
     * Lista registros de gestión de baterías de forma paginada que tienen el estado "Cerrado".
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @return una página de {@link BatteryManagementRecordInformation}
     */
    Page<BatteryManagementRecordInformation> listPaginatedClosedRecords(Integer page, Integer size);

    /**
     * Lista registros de gestión de baterías de forma paginada que tienen el estado "Cerrado",
     * filtrando por término de búsqueda.
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param searchTerm término de búsqueda (identificación o área)
     * @return una página de {@link BatteryManagementRecordInformation}
     */
    Page<BatteryManagementRecordInformation> listPaginatedClosedRecordsWithSearchTerm(Integer page, Integer size, String searchTerm);

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
    Page<BatteryManagementRecordInformation> listClosedWithMultipleFilters(
        String identificationNumber, String workAreaName,
        LocalDateTime dateFrom, LocalDateTime dateTo,
        Long identificationTypeId, Long jobPositionTypeId,
        String intralaboralForm,
        Integer page, Integer size
    );

    /**
     * Lista baterías que pertenecen a un espacio de análisis específico,
     * paginadas y con filtros opcionales múltiples.
     *
     * @param spaceId              ID del espacio de análisis (obligatorio)
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
    Page<BatteryManagementRecordInformation> listByAnalysisSpaceWithMultipleFilters(
        Long spaceId,
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
    BatteryManagementRecordInformation getRecordInformationById(Long id);
}
