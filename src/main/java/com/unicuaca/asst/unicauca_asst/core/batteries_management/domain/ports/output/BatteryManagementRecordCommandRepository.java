package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;

import java.util.Optional;

/**
 * Puerto de salida para operaciones de comando (escritura) sobre el agregado BatteryManagementRecord.
 * Forma parte de la arquitectura hexagonal: el dominio depende del contrato, no de la implementación.
 */
public interface BatteryManagementRecordCommandRepository {

    /**
     * Persiste un nuevo registro de gestión de batería.
     * Debe retornar el registro con datos generados por la persistencia (p.ej. ID, timestamps).
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
