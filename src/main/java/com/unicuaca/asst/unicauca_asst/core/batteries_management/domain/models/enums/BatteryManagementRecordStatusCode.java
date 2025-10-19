package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BatteryManagementRecordStatusCode {

    CREATED("Creado"),
    IN_PROCESSING("En diligenciamiento"),
    COMPLETED("Diligenciado"),
    CLOSED("Cerrado");

    private final String description;

}
