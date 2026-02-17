package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumerador que define los tipos de cuestionarios disponibles en el sistema. (Por su abreviatura)
 * Se utiliza para evitar el uso de cadenas de texto hardcodeadas en la lógica de negocio.
 */
@Getter
@RequiredArgsConstructor
public enum QuestionnaireEnum {

    ILA("ILA"),
    ILB("ILB"),
    EXT("EXT"),
    EST("EST");

    private final String abbreviation;
}
