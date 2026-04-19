package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Representa un registro de gestión de cuestionarios.
 *
 * <p>Incluye información sobre su estado y la asociación con el registro
 * de gestión de batería y el cuestionario correspondiente.</p>
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class QuestionnaireManagementRecord {

    /** Identificador único del registro de gestión de cuestionario. */
    private Long id;

    /** Fecha y hora de creación del registro. */
    private LocalDateTime createdAt;

    /** Fecha y hora de la última actualización del registro. */
    private LocalDateTime updatedAt;

    /** Registro de gestión de batería al que pertenece este cuestionario. */
    private BatteryManagementRecord batteryManagementRecord;

    /** Cuestionario asociado a este registro de gestión. */
    private Questionnaire questionnaire;

    /** Estado actual del registro de gestión del cuestionario. */
    private QuestionnaireManagementRecordStatus status;

}
