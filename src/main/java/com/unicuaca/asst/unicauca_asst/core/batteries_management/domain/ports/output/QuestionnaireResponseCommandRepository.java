package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;

import java.util.List;

/**
 * Puerto de salida para operaciones de escritura (Command) sobre respuestas de cuestionarios.
 */
public interface QuestionnaireResponseCommandRepository {

    /**
     * Guarda una lista de respuestas de cuestionario en la base de datos.
     *
     * @param responses Lista de modelos de dominio completamente validados y enriquecidos.
     */
    void saveAll(List<QuestionnaireResponse> responses);
}
