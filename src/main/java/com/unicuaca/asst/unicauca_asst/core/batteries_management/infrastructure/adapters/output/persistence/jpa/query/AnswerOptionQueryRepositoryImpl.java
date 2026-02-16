package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.AnswerOption;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.AnswerOptionQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.AnswerOptionSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.AnswerOptionPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del puerto de salida {@link AnswerOptionQueryRepository}
 * utilizando JPA como tecnología de persistencia.
 *
 * Actúa como adaptador entre el dominio y la infraestructura para
 * recuperar información del catálogo de opciones de respuesta.
 */
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class AnswerOptionQueryRepositoryImpl implements AnswerOptionQueryRepository {

    private final AnswerOptionSpringJpaRepository answerOptionSpringJpaRepository;
    private final AnswerOptionPersistenceMapper answerOptionPersistenceMapper;

    /**
     * Obtiene una opción de respuesta por su identificador único.
     *
     * @param id identificador de la opción.
     * @return un {@link Optional} con el modelo de dominio o vacío si no existe.
     */
    @Override
    public Optional<AnswerOption> getAnswerOptionById(Long id) {
        return answerOptionSpringJpaRepository.findById(id)
                .map(answerOptionPersistenceMapper::toDomain);
    }

    /**
     * Verifica la existencia de una opción de respuesta por su ID.
     *
     * @param id identificador de la opción.
     * @return true si existe, false si no.
     */
    @Override
    public boolean existsAnswerOptionById(Long id) {
        return answerOptionSpringJpaRepository.existsById(id);
    }

    /**
     * Obtiene una opción de respuesta por su número de orden.
     *
     * @param order número de orden visual.
     * @return un {@link Optional} con el modelo de dominio o vacío si no existe.
     */
    @Override
    public Optional<AnswerOption> getAnswerOptionByOrder(Integer order) {
        return answerOptionSpringJpaRepository.findByOrder(order)
                .map(answerOptionPersistenceMapper::toDomain);
    }

    /**
     * Verifica la existencia de una opción de respuesta por su número de orden.
     *
     * @param order número de orden visual.
     * @return true si existe, false si no.
     */
    @Override
    public boolean existsAnswerOptionByOrder(Integer order) {
        return answerOptionSpringJpaRepository.existsByOrder(order);
    }

    /**
     * Obtiene todas las opciones de respuesta disponibles en el sistema.
     *
     * @return lista de opciones de respuesta.
     */
    @Override
    public List<AnswerOption> getAllAnswerOptions() {
        return answerOptionSpringJpaRepository.findAll()
                .stream()
                .map(answerOptionPersistenceMapper::toDomain)
                .toList();
    }

    /**
     * Obtiene una opción de respuesta por su valor numérico.
     *
     * @param value valor numérico de la opción (ej. 5, 3, 1).
     * @return un {@link Optional} con el modelo de dominio o vacío si no existe.
     */
    @Override
    public Optional<AnswerOption> getAnswerOptionByValue(Integer value) {
        return answerOptionSpringJpaRepository.findByValue(value)
            .map(answerOptionPersistenceMapper::toDomain);
    }
}
