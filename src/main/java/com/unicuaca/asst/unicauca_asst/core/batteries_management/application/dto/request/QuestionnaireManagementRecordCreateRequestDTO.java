package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request;

import com.unicuaca.asst.unicauca_asst.common.validation.FirstGroup;
import com.unicuaca.asst.unicauca_asst.common.validation.SecondGroup;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO para la creación de un registro de gestión de cuestionarios.
 *
 * Contiene los identificadores necesarios para asociar un cuestionario a un registro de gestión de baterías.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({QuestionnaireManagementRecordCreateRequestDTO.class, FirstGroup.class, SecondGroup.class})
public class QuestionnaireManagementRecordCreateRequestDTO {

    @NotNull(message = "{questionnaireManagementRecord.batteryManagementRecordId.notNull}")
    @Min(value = 1, message = "{questionnaireManagementRecord.batteryManagementRecordId.min}")  
    private Long batteryManagementRecordId;

    @NotNull(message = "{questionnaireManagementRecord.questionnaireId.notNull}")
    @Min(value = 1, message = "{questionnaireManagementRecord.questionnaireId.min}")
    private Long questionnaireId;
}
