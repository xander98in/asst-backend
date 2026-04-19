package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;

/**
 * Puerto de entrada para los casos de uso de consulta sobre detalles de una persona evaluada.
 *
 * <p>Define las operaciones de tipo "Query" del modelo {@link PersonEvaluatedDetails},
 * incluyendo la obtención de datos meta asociados a un registro de batería y la consulta
 * completa por identificador del detalle. Permite que los adaptadores de entrada interactúen
 * con la lógica de negocio sin acoplarse directamente a su implementación.</p>
 */
public interface PersonEvaluatedDetailsQueryCUInputPort {

    /**
     * Obtiene los detalles (modelo dominio) asociados a un registro de gestión de batería.
     * En este flujo se usará solo para exponer id/batteryManagementRecordId/createdAt/updatedAt.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return PersonEvaluatedDetails (con id, createdAt, updatedAt)
     */
    PersonEvaluatedDetails getMetaByBatteryManagementRecordId(Long batteryManagementRecordId);

    /**
     * Obtiene los detalles completos de una persona evaluada por el ID del detalle.
     *
     * @param personEvaluatedDetailsId ID del detalle de la persona evaluada
     * @return PersonEvaluatedDetails (modelo dominio)
     */
    PersonEvaluatedDetails getPersonEvaluatedDetailsById(Long personEvaluatedDetailsId);
}
