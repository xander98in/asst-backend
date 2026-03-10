package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumerador que define los estados posibles de una persona evaluada.
 * Se utiliza para evitar el uso de cadenas de texto hardcodeadas en la lógica de negocio.
 */
@Getter
@RequiredArgsConstructor
public enum StatusPersonEvaluatedEnum {

    WITHOUT_RECORD("Sin registro"),
    WITH_RECORD("Con registro");

    private final String description;

}
