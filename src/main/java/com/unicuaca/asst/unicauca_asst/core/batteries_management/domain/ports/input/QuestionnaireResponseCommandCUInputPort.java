package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;

import java.util.List;

/**
 * Puerto de entrada para los casos de uso relacionados con las respuestas de los cuestionarios.
 *
 * <p>Define las operaciones de tipo "Command" (escritura) que la capa de aplicación puede
 * solicitar al dominio sobre el modelo {@link QuestionnaireResponse}, incluyendo procesamiento
 * de lotes para creación y actualización de respuestas.</p>
 */
public interface QuestionnaireResponseCommandCUInputPort {

    /**
     * Procesa y almacena un lote de respuestas de cuestionario.
     *
     * @param responses Lista de modelos de dominio con las respuestas a guardar.
     */
    void createQuestionnaireResponseBatch(List<QuestionnaireResponse> responses);

    /**
     * Procesa y actualiza un lote de respuestas de cuestionario existentes asociadas a un registro de gestión de cuestionario.
     *
     * @param responses Lista de modelos de dominio con las respuestas a actualizar.
     */
    void updateQuestionnaireResponseBatch(List<QuestionnaireResponse> responses);
}
