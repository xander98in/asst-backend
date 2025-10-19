package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;

import java.util.List;

/**
 * Puerto de entrada para casos de uso de consulta de cuestionarios.
 *
 * Expone operaciones de solo lectura para que los adaptadores de entrada (p. ej. controladores REST)
 * interactúen con la lógica de negocio sin acoplarse a su implementación.
 */
public interface QuestionnaireQueryCUInputPort {

    /**
     * Recupera todos los cuestionarios disponibles.
     *
     * @return lista de cuestionarios.
     */
    List<Questionnaire> getAllQuestionnaires();

    /**
     * Obtiene un cuestionario por su abreviatura exacta.
     * Si no existe, se debe lanzar una excepción de entidad no encontrada desde la capa de dominio.
     *
     * @param abbreviation abreviatura a buscar (p. ej., "ILA", "ILB", "EXT", "EST").
     * @return el cuestionario correspondiente.
     */
    Questionnaire getQuestionnaireByAbbreviation(String abbreviation);
}
