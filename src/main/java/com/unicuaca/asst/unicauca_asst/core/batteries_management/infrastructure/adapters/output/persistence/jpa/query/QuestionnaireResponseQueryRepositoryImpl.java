package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireResponseQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.QuestionnaireResponseSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.QuestionnaireResponsePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del puerto de salida {@link QuestionnaireResponseQueryRepository}
 * utilizando JPA como tecnología de persistencia.
 *
 * Actúa como adaptador entre el dominio y la infraestructura para
 * recuperar información de las respuestas a los cuestionarios.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionnaireResponseQueryRepositoryImpl implements QuestionnaireResponseQueryRepository {

    private final QuestionnaireResponseSpringJpaRepository repository;
    private final QuestionnaireResponsePersistenceMapper mapper;

    /**
     * Obtiene una respuesta por su ID, cargando todas sus relaciones de forma ansiosa (eager).
     *
     * @param id Identificador de la respuesta.
     * @return Opcional con el modelo de dominio y sus relaciones completas.
     */
    @Override
    public Optional<QuestionnaireResponse> getByIdWithAllRelations(Long id) {
        return repository.findByIdWithAll(id)
                .map(mapper::toDomain);
    }

    /**
     * Obtiene una respuesta a partir de id del registro de gestión de cuestionario
     * y el id de una pregunta, cargando todas sus relaciones de forma ansiosa (eager).
     *
     * @param recordId   ID del registro de gestión de cuestionario.
     * @param questionId ID de la pregunta.
     * @return Opcional con el modelo de dominio y sus relaciones completas.
     */
    @Override
    public Optional<QuestionnaireResponse> getByRecordIdAndQuestionIdWithAllRelations(Long recordId, Long questionId) {
        return repository.findByRecordIdAndQuestionIdWithAll(recordId, questionId)
                .map(mapper::toDomain);
    }

    /**
     * Verifica si una persona (a través de su registro) ya contestó una pregunta específica.
     *
     * @param recordId   ID del registro de gestión de cuestionario.
     * @param questionId ID de la pregunta.
     * @return true si la respuesta ya existe, false en caso contrario.
     */
    @Override
    public boolean existsByRecordIdAndQuestionId(Long recordId, Long questionId) {
        return repository.existsByQuestionnaireManagementRecord_IdAndQuestion_Id(recordId, questionId);
    }

    /**
     * Obtiene todas las respuestas asociadas a un registro de gestión de cuestionario,
     * cargando todas sus relaciones de forma ansiosa (eager).
     *
     * @param recordId ID del registro de gestión de cuestionario.
     * @return Lista de respuestas con sus relaciones completas.
     */
    @Override
    public List<QuestionnaireResponse> findAllByRecordIdWithAllRelations(Long recordId) {
        return repository.findAllByQuestionnaireManagementRecordIdWithAll(recordId)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
}
