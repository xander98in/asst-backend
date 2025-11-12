package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

public interface PersonEvaluatedDetailsQueryRepository {

    /**
     * Verifica si ya existe un PersonEvaluatedDetails asociado al registro de gestión de batería dado (relación 1:1).
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return true si existe, false si no
     */
    boolean existsByBatteryManagementRecordId(Long batteryManagementRecordId);

}
