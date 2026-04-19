package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;

import java.util.List;

/**
 * Puerto de entrada para los casos de uso de consulta sobre las respuestas de los cuestionarios.
 *
 * <p>Define las operaciones de tipo "Query" del modelo {@link QuestionnaireResponse},
 * permitiendo recuperar las respuestas asociadas a un registro de gestión de cuestionario
 * sin acoplar los adaptadores de entrada a la implementación concreta.</p>
 */
public interface QuestionnaireResponseQueryCUInputPort {

    /**
     * Obtiene la lista completa de respuestas asociadas a un registro de gestión de cuestionario.
     *
     * @param recordId ID del registro de gestión de cuestionario.
     * @return Lista de respuestas del dominio.
     */
    List<QuestionnaireResponse> getResponsesByQuestionnaireManagementRecordId(Long recordId);
}
