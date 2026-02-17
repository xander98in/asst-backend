package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request;

import com.unicuaca.asst.unicauca_asst.common.validation.FirstGroup;
import com.unicuaca.asst.unicauca_asst.common.validation.SecondGroup;

import io.swagger.v3.oas.annotations.media.Schema;
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
@GroupSequence({FirstGroup.class, SecondGroup.class, QuestionnaireManagementRecordCreateRequestDTO.class})
@Schema(
    description = "Solicitud para crear un registro de gestión de cuestionarios",
    example = """
    {
      "batteryManagementRecordId": 123,
      "questionnaireId": 456
    }
    """
)
public class QuestionnaireManagementRecordCreateRequestDTO {

    /**
     * ID del registro de gestión de baterías al que se asociará este registro de gestión de cuestionarios.
     */
    @NotNull(message = "{questionnaireManagementRecord.batteryManagementRecordId.notNull}", groups = FirstGroup.class)
    @Min(value = 1, message = "{questionnaireManagementRecord.batteryManagementRecordId.min}", groups = SecondGroup.class)
    @Schema(description = "ID del registro de gestión de baterías asociado", example = "123")
    private Long batteryManagementRecordId;

    /**
     * ID del cuestionario que se asociará a este registro de gestión de cuestionarios.
     */
    @NotNull(message = "{questionnaireManagementRecord.questionnaireId.notNull}", groups = FirstGroup.class)
    @Min(value = 1, message = "{questionnaireManagementRecord.questionnaireId.min}", groups = SecondGroup.class)
    @Schema(description = "ID del cuestionario asociado", example = "456")
    private Long questionnaireId;
}
