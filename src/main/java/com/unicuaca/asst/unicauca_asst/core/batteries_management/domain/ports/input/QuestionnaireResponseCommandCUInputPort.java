package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;

import java.util.List;

/**
 * Puerto de entrada para los casos de uso relacionados con las respuestas de los cuestionarios.
 *
 * Define las operaciones de escritura (Command) que la capa de aplicación puede solicitar al dominio.
 */
public interface QuestionnaireResponseCommandCUInputPort {

    /**
     * Procesa y almacena un lote de respuestas de cuestionario.
     *
     * @param responses Lista de modelos de dominio con las respuestas a guardar.
     */
    void createQuestionnaireResponseBatch(List<QuestionnaireResponse> responses);
}
