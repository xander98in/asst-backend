package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumerador que define los tipos de cuestionarios disponibles en el sistema
 * (identificados por su abreviatura).
 *
 * <p>Se utiliza para evitar el uso de cadenas de texto hardcodeadas en la lógica
 * de negocio al referenciar cuestionarios intralaborales, extralaborales y de estrés.</p>
 */
@Getter
@RequiredArgsConstructor
public enum QuestionnaireEnum {

    /** Cuestionario Intralaboral - Forma A (cargos de jefatura y profesionales). */
    ILA("ILA"),

    /** Cuestionario Intralaboral - Forma B (cargos auxiliares y operarios). */
    ILB("ILB"),

    /** Cuestionario Extralaboral. */
    EXT("EXT"),

    /** Cuestionario de Estrés. */
    EST("EST");

    /** Abreviatura del cuestionario, utilizada como identificador de negocio. */
    private final String abbreviation;
}
