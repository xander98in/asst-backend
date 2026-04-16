package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para operaciones de consulta sobre el agregado {@link BatteryManagementRecord}.
 *
 * Define las firmas de los métodos que deben implementar los adaptadores de infraestructura encargados
 * de recuperar información desde fuentes externas (p. ej., base de datos relacional).
 *
 * Forma parte de la arquitectura hexagonal, separando las dependencias externas de la lógica del dominio.
 */
public interface BatteryManagementRecordQueryRepository {

    /**
     * Consulta un registro de gestión de batería por su identificador único.
     *
     * @param id identificador del registro de gestión de batería
     * @return un {@link Optional} con el registro encontrado o vacío si no existe
     */
    Optional<BatteryManagementRecord> getBatteryManagementRecordById(Long id);

    /**
     * Consulta un estado de registro de gestión de batería por su nombre.
     *
     * @param name nombre del estado del registro de gestión de batería
     * @return un {@link Optional} con el estado encontrado o vacío si no existe
     */
    Optional<BatteryManagementRecordStatus> getBatteryManagementRecordStatudByName(String name);

    /**
     * Verifica si existe un registro de gestión de batería con el ID proporcionado.
     *
     * @param id identificador del registro de gestión de batería
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsById(Long id);

    /**
     * Obtiene todos los registros de gestión de baterías asociados a una persona evaluada.
     *
     * @param personEvaluatedId identificador de la persona evaluada
     * @return lista de registros asociados (posiblemente vacía si no hay resultados)
     */
    List<BatteryManagementRecord> getBatteryManagementRecordsByPersonEvaluatedId(Long personEvaluatedId);

    /**
     * Verifica si una persona evaluada tiene al menos un registro de gestión de batería.
     *
     * @param personEvaluatedId identificador de la persona evaluada
     * @return {@code true} si existe al menos un registro, {@code false} si no
      */
     boolean existsByPersonEvaluatedId(Long personEvaluatedId);

    /**
     * Verifica si una persona evaluada tiene al menos un registro de gestión de batería en alguno de los estados proporcionados.
     *
     * @param personEvaluatedId identificador de la persona evaluada
     * @param statusNames lista de nombres de estados a buscar
     * @return {@code true} si existe al menos un registro en alguno de los estados, {@code false} en caso contrario
     */
    boolean existsByPersonEvaluatedIdAndStatusNameIn(Long personEvaluatedId, List<String> statusNames);

    /**
     * Lista registros de gestión de baterías de forma paginada por su nombre de estado.
     *
     * @param statusName nombre del estado a filtrar
     * @param page número de página (0-indexado)
     * @param size tamaño de página
     * @param sort criterio de ordenamiento
     * @return una página de {@link BatteryManagementRecord}
     */
    Page<BatteryManagementRecord> listPagedByStatus(String statusName, Integer page, Integer size, Sort sort);

    /**
     * Lista registros de gestión de baterías de forma paginada por su nombre de estado y término de búsqueda.
     *
     * @param statusName nombre del estado a filtrar
     * @param searchTerm término de búsqueda (identificación o área)
     * @param page número de página
     * @param size tamaño de página
     * @param sort criterio de ordenamiento
     * @return una página de {@link BatteryManagementRecord}
     */
    Page<BatteryManagementRecord> listPagedByStatusWithSearchTerm(String statusName, String searchTerm, Integer page, Integer size, Sort sort);

    /**
     * Lista registros de gestión de baterías de forma paginada, excluyendo los que tengan el estado indicado (ej: "Cerrado").
     *
     * @param excludedStatus nombre del estado a excluir
     * @param page número de página (0-indexado)
     * @param size tamaño de página
     * @param sort criterio de ordenamiento
     * @return una página de {@link BatteryManagementRecord}
     */
    Page<BatteryManagementRecord> listPagedExcludingStatus(String excludedStatus, Integer page, Integer size, Sort sort);

    /**
     * Lista registros de gestión de baterías paginados, excluyendo un estado específico
     * y filtrando por prefijo de número de identificación, ordenados según el criterio proporcionado.
     *
     * @param excludedStatus nombre del estado a excluir
     * @param identificationNumberPrefix prefijo del número de identificación para filtrar
     * @param page número de página (0-indexado)
     * @param size tamaño de página
     * @param sort criterio de ordenamiento
     * @return una página de {@link BatteryManagementRecord}
     */
    Page<BatteryManagementRecord> listPaginatedByIdentificationPrefix(String excludedStatus, String identificationNumberPrefix, Integer page, Integer size, Sort sort);

    /**
     * Lista registros de gestión de baterías paginados, excluyendo un estado específico y filtrando por término de búsqueda en número de identificación o nombre del área de trabajo,
     * ordenados según el criterio proporcionado.
     *
     * @param excludedStatus nombre del estado a excluir
     * @param searchTerm término de búsqueda para filtrar por número de identificación
     * @param page número de página (0-indexado)
     * @param size tamaño de página
     * @param sort criterio de ordenamiento
     * @return una página de {@link BatteryManagementRecord}
     */
    Page<BatteryManagementRecord> listPagedExcludingStatusWithSearchTerm(String excludedStatus, String searchTerm, Integer page, Integer size, Sort sort);

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
     * @return página de baterías cerradas filtradas
     */
    Page<BatteryManagementRecord> listClosedWithMultipleFilters(
        String identificationNumber, String workAreaName,
        LocalDateTime dateFrom, LocalDateTime dateTo,
        Long identificationTypeId, Long jobPositionTypeId,
        String intralaboralForm,
        Integer page, Integer size
    );

    /**
     * Lista baterías que pertenecen a un espacio de análisis específico, paginadas y
     * con filtros opcionales múltiples.
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
     * @return página de baterías del espacio filtradas
     */
    Page<BatteryManagementRecord> listByAnalysisSpaceWithMultipleFilters(
        Long spaceId,
        String identificationNumber, String workAreaName,
        LocalDateTime dateFrom, LocalDateTime dateTo,
        Long identificationTypeId, Long jobPositionTypeId,
        String intralaboralForm,
        Integer page, Integer size
    );
}
