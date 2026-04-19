package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumerador que define los estados posibles de un registro de gestión de cuestionario.
 *
 * <p>Se utiliza para evitar el uso de cadenas de texto hardcodeadas en la lógica
 * de negocio al referenciar el estado actual de un cuestionario dentro de un
 * registro de batería.</p>
 */
@Getter
@RequiredArgsConstructor
public enum QuestionnaireManagementRecordStatusEnum {

    /** Cuestionario recién creado, aún sin respuestas registradas. */
    CREADO("Creado"),

    /** Cuestionario con respuestas parciales, en proceso de diligenciamiento. */
    EN_DILIGENCIAMIENTO("En diligenciamiento"),

    /** Cuestionario con todas sus preguntas respondidas. */
    DILIGENCIADO("Diligenciado"),

    /** Cuestionario cerrado, no admite más modificaciones. */
    CERRADO("Cerrado");

    /** Nombre legible del estado, utilizado para mostrarse al usuario. */
    private final String name;
}
