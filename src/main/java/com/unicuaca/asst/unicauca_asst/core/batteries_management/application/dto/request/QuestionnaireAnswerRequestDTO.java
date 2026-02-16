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
 * DTO que representa una respuesta individual dentro de un lote de respuestas.
 * Contiene el ID de la pregunta y el valor seleccionado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({FirstGroup.class, SecondGroup.class, QuestionnaireAnswerRequestDTO.class})
@Schema(description = "Objeto que representa una respuesta individual a una pregunta")
public class QuestionnaireAnswerRequestDTO {

    /**
     * ID de la pregunta que se está respondiendo.
     */
    @NotNull(message = "{questionnaireResponse.questionId.notNull}", groups = FirstGroup.class)
    @Positive(message = "{questionnaireResponse.questionId.positive}", groups = SecondGroup.class)
    @Schema(description = "ID único de la pregunta", example = "10")
    private Long questionId;

    /**
     * Valor numérico de la respuesta seleccionada.
     * Se valida que no sea nulo y que sea positivo o cero.
     */
    @NotNull(message = "{questionnaireResponse.value.notNull}", groups = FirstGroup.class)
    @Min(value = 0, message = "{questionnaireResponse.value.min}", groups = SecondGroup.class)
    @Schema(description = "Valor numérico de la opción seleccionada", example = "5")
    private Integer value;
}