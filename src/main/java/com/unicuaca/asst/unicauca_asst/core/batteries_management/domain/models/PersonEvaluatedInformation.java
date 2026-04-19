package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import com.unicuaca.asst.unicauca_asst.common.domain.models.Gender;
import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Proyección de consulta que reúne información básica de una persona evaluada.
 *
 * <p>Se utiliza para listados y vistas resumidas que requieren los datos
 * identificatorios, demográficos mínimos y el estado de la persona sin necesidad
 * de cargar todos los detalles asociados.</p>
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class PersonEvaluatedInformation {

    /** Identificador único de la persona evaluada. */
    private Long id;

    /** Tipo de documento de identificación. */
    private IdentificationType identificationType;

    /** Número del documento de identificación. */
    private String identificationNumber;

    /** Nombres de la persona evaluada. */
    private String firstName;

    /** Apellidos de la persona evaluada. */
    private String lastName;

    /** Año de nacimiento de la persona evaluada. */
    private Integer birthYear;

    /** Sexo de la persona evaluada. */
    private Gender gender;

    /** Estado actual de la persona evaluada (con o sin registro de batería asociada). */
    private StatusPersonEvaluated statusPersonEvaluated;

}
