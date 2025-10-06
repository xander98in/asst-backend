package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;

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
}
