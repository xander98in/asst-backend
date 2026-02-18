package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Representa un registro de gestión de cuestionarios, incluyecndo información sobre su
 * estado y asociación con registros de gestión de baterías y cuestionarios.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class QuestionnaireManagementRecord {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BatteryManagementRecord batteryManagementRecord;
    private Questionnaire questionnaire;
    private QuestionnaireManagementRecordStatus status;

}
