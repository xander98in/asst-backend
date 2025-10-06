package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Representa un registro de gestión de batería.
 * Cada registro está asociado a una persona evaluada y tiene un estado.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class BatteryManagementRecord {

    private Long id;
    private PersonEvaluated personEvaluated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BatteryManagementRecordStatus status;
}
