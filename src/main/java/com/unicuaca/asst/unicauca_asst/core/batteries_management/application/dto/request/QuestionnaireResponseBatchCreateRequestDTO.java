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
 * DTO para la creación masiva de respuestas de un cuestionario.
 * <p>
 * Agrupa múltiples respuestas bajo un mismo registro de gestión de cuestionario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({FirstGroup.class, SecondGroup.class, QuestionnaireResponseBatchCreateRequestDTO.class})
@Schema(
    description = "Solicitud para registrar un lote de respuestas de un cuestionario",
    example = """
    {
      "questionnaireManagementRecordId": 123,
      "answers": [
        { "questionId": 10, "value": 5 },
        { "questionId": 11, "value": 3 },
        { "questionId": 12, "value": 1 }
      ]
    }
    """
)
public class QuestionnaireResponseBatchCreateRequestDTO {

    /**
     * ID del registro de gestión de cuestionario al que pertenecen estas respuestas.
     */
    @NotNull(message = "{questionnaireResponse.questionnaireManagementRecordId.notNull}", groups = FirstGroup.class)
    @Positive(message = "{questionnaireResponse.questionnaireManagementRecordId.positive}", groups = SecondGroup.class)
    @Schema(description = "ID del registro de gestión de cuestionario asociado", example = "123")
    private Long questionnaireManagementRecordId;

    /**
     * Lista de respuestas individuales.
     * La anotación @Valid es crucial para que Spring valide cada objeto QuestionnaireAnswerRequestDTO dentro de la lista.
     */
    @NotNull(message = "{questionnaireResponse.answers.notNull}", groups = FirstGroup.class)
    @NotEmpty(message = "{questionnaireResponse.answers.notEmpty}", groups = SecondGroup.class)
    @Valid
    @Schema(description = "Lista de respuestas individuales")
    private List<QuestionnaireAnswerRequestDTO> answers;
}
