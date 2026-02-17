package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request;

import com.unicuaca.asst.unicauca_asst.common.validation.FirstGroup;
import com.unicuaca.asst.unicauca_asst.common.validation.SecondGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para la actualización masiva de respuestas de un cuestionario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({FirstGroup.class, SecondGroup.class, QuestionnaireResponseBatchUpdateRequestDTO.class})
@Schema(
    description = "Solicitud para actualizar un lote de respuestas existentes",
    example = """
    {
      "questionnaireManagementRecordId": 123,
      "answers": [
        { "id": 501, "questionId": 10, "value": 4 },
        { "id": 502, "questionId": 11, "value": 2 },
        { "id": 503, "questionId": 12, "value": 0 }
      ]
    }
    """
)
public class QuestionnaireResponseBatchUpdateRequestDTO {

    /**
     * ID del registro de gestión de cuestionario al que pertenecen estas respuestas.
     */
    @NotNull(message = "{questionnaireResponse.questionnaireManagementRecordId.notNull}", groups = FirstGroup.class)
    @Positive(message = "{questionnaireResponse.questionnaireManagementRecordId.positive}", groups = SecondGroup.class)
    @Schema(description = "ID del registro de gestión de cuestionario asociado", example = "123")
    private Long questionnaireManagementRecordId;

    /**
     * Lista de respuestas individuales a actualizar.
     * La anotación @Valid es crucial para que Spring valide cada objeto QuestionnaireAnswerUpdateRequestDTO dentro de la lista.
     */
    @NotNull(message = "{questionnaireResponse.answers.notNull}", groups = FirstGroup.class)
    @NotEmpty(message = "{questionnaireResponse.answers.notEmpty}", groups = SecondGroup.class)
    @Valid
    @Schema(description = "Lista de respuestas a actualizar")
    private List<QuestionnaireAnswerUpdateRequestDTO> answers;
}
