package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Question;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.QuestionSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.QuestionPersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida {@link QuestionQueryRepository}
 * utilizando JPA como tecnología de persistencia.
 *
 * Actúa como adaptador entre el dominio y la infraestructura.
 */
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class QuestionQueryRepositoryImpl implements QuestionQueryRepository {

    private final QuestionSpringJpaRepository questionSpringJpaRepository;
    private final QuestionPersistenceMapper questionPersistenceMapper;

     /**
     * Consulta una pregunta por su identificador único,
     * sin requerir explícitamente la carga del cuestionario.
     *
     * @param id identificador de la pregunta.
     * @return un {@link Optional} con la pregunta encontrada o vacío si no existe.
     */
    @Override
    public Optional<Question> getQuestionById(Long id) {
        return questionSpringJpaRepository.findByIdWithoutQuestionnaire(id)
                .map(questionPersistenceMapper::toDomain);
    }

    /**
     * Consulta una pregunta por su identificador único,
     * cargando explícitamente la información del cuestionario asociado.
     *
     * @param id identificador de la pregunta.
     * @return un {@link Optional} con la pregunta encontrada (incluyendo su cuestionario) o vacío si no existe.
     */
    @Override
    public Optional<Question> getQuestionByIdWithQuestionnaire(Long id) {
        return questionSpringJpaRepository.findByIdWithQuestionnaire(id)
                .map(questionPersistenceMapper::toDomain);
    }

    /**
     * Obtiene todas las preguntas, sin requerir explícitamente la carga del cuestionario.
     *
     * @return lista de preguntas (posiblemente vacía si no hay resultados).
     */
    @Override
    public List<Question> getAllQuestions() {
        List<QuestionEntity> entities = questionSpringJpaRepository.findAllWithoutQuestionnaire();
        return entities.stream()
                .map(questionPersistenceMapper::toDomain)
                .toList();
    }

    /**
     * Obtiene todas las preguntas, cargando explícitamente la información
     * del cuestionario asociado a cada una.
     *
     * @return lista de preguntas con sus cuestionarios (posiblemente vacía si no hay resultados).
     */
    @Override
    public List<Question> getAllQuestionsWithQuestionnaire() {
        List<QuestionEntity> entities = questionSpringJpaRepository.findAllWithQuestionnaire();
        return entities.stream()
                .map(questionPersistenceMapper::toDomain)
                .toList();
    }

    /**
     * Obtiene una pregunta por su orden y el identificador del cuestionario,
     * cargando explícitamente la información del cuestionario asociado.
     *
     * @param order           orden de la pregunta dentro del cuestionario.
     * @param questionnaireId identificador del cuestionario.
     * @return un {@link Optional} con la pregunta encontrada (incluyendo su cuestionario) o vacío si no existe.
     */
    @Override
    public Optional<Question> getQuestionByOrderAndQuestionnaireIdWithQuestionnaire(Integer order, Long questionnaireId) {
        return questionSpringJpaRepository
                .findByOrderAndQuestionnaireIdWithQuestionnaire(order, questionnaireId)
                .map(questionPersistenceMapper::toDomain);
    }

    /**
     * Obtiene las preguntas de un cuestionario específico.
     *
     * @param questionnaireId identificador del cuestionario para el cual se desean obtener las preguntas.
     * @return lista de preguntas asociadas al cuestionario especificado (posiblemente vacía si no hay resultados).
     */
    @Override
    public List<Question> getByQuestionnaireId(Long questionnaireId) {
        return questionSpringJpaRepository.findByQuestionnaireId(questionnaireId)
            .stream()
            .map(questionPersistenceMapper::toDomain)
            .toList();
    }

    /**
     * Obtiene las preguntas de un cuestionario por su abreviatura.
     *
     * @param abbreviation abreviatura del cuestionario para el cual se desean obtener las preguntas (p. ej., "ILA", "EXT", "EST").
     * @return lista de preguntas asociadas al cuestionario con la abreviatura especificada (posiblemente vacía si no hay resultados).
     */
    @Override
    public List<Question> getByQuestionnaireAbbreviation(String abbreviation) {
        return questionSpringJpaRepository.findByQuestionnaireAbbreviation(abbreviation)
            .stream()
            .map(questionPersistenceMapper::toDomain)
            .toList();
    }
}
