package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;

import java.util.Optional;

/**
 * Puerto de salida para operaciones de comando (escritura) sobre el agregado
 * {@link BatteryManagementRecord}.
 *
 * <p>Forma parte de la arquitectura hexagonal: el dominio depende del contrato,
 * no de la implementación concreta de persistencia.</p>
 */
public interface BatteryManagementRecordCommandRepository {

    /**
     * Persiste un nuevo registro de gestión de batería.
     *
     * <p>Debe retornar el registro con los datos generados por la persistencia
     * (por ejemplo, ID y timestamps).</p>
     *
     * @param record registro a persistir
     * @return un {@link Optional} con el registro persistido, o vacío si no se pudo guardar
     */
    Optional<BatteryManagementRecord> saveBatteryManagementRecord(BatteryManagementRecord record);

    /**
     * Actualiza un registro de gestión de batería existente.
     *
     * @param record Registro a actualizar
     * @return Registro actualizado, si la operación fue exitosa
     */
    Optional<BatteryManagementRecord> updateBatteryManagementRecord(BatteryManagementRecord record);

    /**
     * Elimina un registro de gestión de baterías por su ID.
     *
     * @param id ID del registro
     */
    void deleteBatteryManagementRecordById(Long id);
}
