package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;

/**
 * Puerto de entrada para los casos de uso de escritura sobre registros de gestión de baterías.
 *
 * <p>Define las operaciones de tipo "Command" del modelo {@link BatteryManagementRecord},
 * permitiendo que los adaptadores de entrada (como controladores REST o handlers de aplicación)
 * interactúen con la lógica de negocio sin acoplarse directamente a su implementación.</p>
 */
public interface BatteryManagementRecordCommandCUInputPort {

    /**
     * Crea un nuevo registro de gestión de baterías para la persona evaluada indicada.
     *
     * @param personEvaluatedId ID de la persona evaluada para la cual se crea el registro.
     * @return El registro de gestión de baterías creado.
     */
    BatteryManagementRecord createBatteryManagementRecord(Long personEvaluatedId);

    /**
     * Elimina un registro de gestión de baterías por su ID.
     *
     * @param id ID del registro de gestión de baterías a eliminar.
     */
    void deleteBatteryManagementRecord(Long id);

    /**
     * Cierra un registro de gestión de baterías por su ID.
     *
     * @param recordId ID del registro de gestión de baterías a cerrar.
     * @return El registro de gestión de baterías cerrado.
     */
    BatteryManagementRecord closeBatteryManagementRecord(Long recordId);
}
