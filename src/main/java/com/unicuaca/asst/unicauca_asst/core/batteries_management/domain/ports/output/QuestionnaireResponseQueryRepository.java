package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para operaciones de consulta sobre respuestas de cuestionarios.
 *
 * Define las firmas de los métodos que deben implementar los adaptadores de infraestructura
 * encargados de recuperar información desde fuentes externas (p. ej., base de datos relacional).
 */
public interface QuestionnaireResponseQueryRepository {

    /**
     * Obtiene una respuesta por su ID, cargando todas sus relaciones de forma ansiosa (eager).
     *
     * @param id Identificador de la respuesta.
     * @return Opcional con el modelo de dominio y sus relaciones completas.
     */
    Optional<QuestionnaireResponse> getByIdWithAllRelations(Long id);

    /**
     * Obtiene una respuesta a partir de id del registro de gestión de cuestionario
     * y el id de una pregunta, cargando todas sus relaciones de forma ansiosa (eager).
     *
     * @param recordId   ID del registro de gestión de cuestionario.
     * @param questionId ID de la pregunta.
     * @return Opcional con el modelo de dominio y sus relaciones completas.
     */
    Optional<QuestionnaireResponse> getByRecordIdAndQuestionIdWithAllRelations(Long recordId, Long questionId);

    /**
     * Verifica si existe una respuesta asociada a un registro de gestión de cuestionario
     * específico y a una pregunta específica.
     *
     * @param recordId   ID del registro de gestión de cuestionario.
     * @param questionId ID de la pregunta.
     * @return true si la respuesta ya existe, false en caso contrario.
     */
    boolean existsByRecordIdAndQuestionId(Long recordId, Long questionId);

    /**
     * Obtiene la lista completa de respuestas para un registro de gestión de cuestionario dado,
     * incluyendo todas sus relaciones cargadas.
     *
     * @param recordId ID del registro de gestión.
     * @return Lista de respuestas del dominio.
     */
    List<QuestionnaireResponse> findAllByRecordIdWithAllRelations(Long recordId);
}
