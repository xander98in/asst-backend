package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumerador que define los estados posibles de un registro de gestión de cuestionario.
 * Se utiliza para evitar el uso de cadenas de texto hardcodeadas en la lógica de negocio.
 */
@Getter
@RequiredArgsConstructor
public enum QuestionnaireManagementRecordStatusEnum {

    CREADO("Creado"),
    EN_DILIGENCIAMIENTO("En diligenciamiento"),
    DILIGENCIADO("Diligenciado"),
    CERRADO("Cerrado");

    private final String name;
}
