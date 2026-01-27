package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

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
     * Lista registros de gestión de baterías de forma paginada,
     * excluyendo los que tengan el estado indicado (ej: "Cerrado").
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
     * Lista registros de gestión de baterías paginados, excluyendo un estado específico
     * y filtrando por término de búsqueda en número de identificación o nombre del área de trabajo,
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
}
