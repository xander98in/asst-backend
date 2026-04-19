package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumerador que define los estados posibles de una persona evaluada.
 *
 * <p>Se utiliza para evitar el uso de cadenas de texto hardcodeadas en la lógica
 * de negocio al referenciar si la persona tiene o no un registro de batería asociado.</p>
 */
@Getter
@RequiredArgsConstructor
public enum StatusPersonEvaluatedEnum {

    /** La persona evaluada aún no tiene un registro de batería asociado. */
    WITHOUT_RECORD("Sin registro"),

    /** La persona evaluada ya tiene al menos un registro de batería asociado. */
    WITH_RECORD("Con registro");

    /** Descripción legible del estado, utilizada para mostrarse al usuario. */
    private final String description;

}
