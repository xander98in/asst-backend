package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Modelo de dominio que representa la información de un registro de gestión de baterías.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class BatteryManagementRecordInformation {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BatteryManagementRecordStatus status;
    private PersonEvaluated personEvaluated;
    private PersonEvaluatedDetails personEvaluatedDetails;
}
