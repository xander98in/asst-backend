package com.unicuaca.asst.unicauca_asst.core.auth.domain.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {

    ADMIN("ADMIN"),
    PROFESIONAL_ASST("PROFESIONAL_ASST"),
    PERSONA_EVALUADA("PERSONA_EVALUADA");

    private final String description;
}
