package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumerador que define los estados posibles de un registro de gestión de batería.
 *
 * <p>Se utiliza para evitar el uso de cadenas de texto hardcodeadas en la lógica
 * de negocio y modela el flujo:
 * {@code CREATED → IN_PROCESSING → COMPLETED → CLOSED}.</p>
 */
@Getter
@RequiredArgsConstructor
public enum BatteryManagementRecordStatusCode {

    /** Registro recién creado, sin cuestionarios iniciados. */
    CREATED("Creado"),

    /** Registro con al menos un cuestionario en proceso de diligenciamiento. */
    IN_PROCESSING("En diligenciamiento"),

    /** Registro con todos los cuestionarios requeridos diligenciados. */
    COMPLETED("Diligenciado"),

    /** Registro cerrado, no admite más modificaciones. */
    CLOSED("Cerrado");

    /** Descripción legible del estado, utilizada para mostrarse al usuario. */
    private final String description;

}
