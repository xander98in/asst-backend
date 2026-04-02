package com.unicuaca.asst.unicauca_asst.core.auth.domain.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatusEnum {

    ACTIVE("Activo"),
    INACTIVE("Inactivo"),
    BLOCKED("Bloqueado");

    private final String description;
}
