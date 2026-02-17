package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request;

import com.unicuaca.asst.unicauca_asst.common.validation.FirstGroup;
import com.unicuaca.asst.unicauca_asst.common.validation.SecondGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una respuesta individual a actualizar dentro de un lote.
 * Contiene el ID de la respuesta existente, el ID de la pregunta y el nuevo valor.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({FirstGroup.class, SecondGroup.class, QuestionnaireAnswerUpdateRequestDTO.class})
@Schema(description = "Objeto que representa una respuesta individual a actualizar")
public class QuestionnaireAnswerUpdateRequestDTO {

    /**
     * ID único del registro de respuesta (QuestionnaireResponse) que se va a actualizar.
     */
    @NotNull(message = "{questionnaireResponse.id.notNull}", groups = FirstGroup.class)
    @Positive(message = "{questionnaireResponse.id.positive}", groups = SecondGroup.class)
    @Schema(description = "ID único del registro de la respuesta a actualizar", example = "501")
    private Long id;

    /**
     * ID de la pregunta asociada.
     * Aunque el ID de respuesta ya identifica el registro, el ID de pregunta sirve para validaciones de consistencia.
     */
    @NotNull(message = "{questionnaireResponse.questionId.notNull}", groups = FirstGroup.class)
    @Positive(message = "{questionnaireResponse.questionId.positive}", groups = SecondGroup.class)
    @Schema(description = "ID único de la pregunta", example = "10")
    private Long questionId;

    /**
     * Nuevo valor numérico de la respuesta seleccionada.
     */
    @NotNull(message = "{questionnaireResponse.value.notNull}", groups = FirstGroup.class)
    @Min(value = 0, message = "{questionnaireResponse.value.min}", groups = SecondGroup.class)
    @Schema(description = "Nuevo valor numérico de la opción seleccionada", example = "4")
    private Integer value;
}
