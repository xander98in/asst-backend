package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionnaireEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.QuestionnaireSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.QuestionnairePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del puerto de salida {@link QuestionnaireQueryRepository}
 * para la obtención de datos de cuestionarios desde una base de datos relacional.
 *
 * <p>Este adaptador utiliza un repositorio JPA para acceder a los registros
 * persistentes y un mapper de MapStruct para convertir las entidades
 * {@link QuestionnaireEntity} a los modelos de dominio {@link Questionnaire}.</p>
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class QuestionnaireQueryRepositoryImpl implements QuestionnaireQueryRepository {

    private final QuestionnaireSpringJpaRepository questionnaireJpaRepository;
    private final QuestionnairePersistenceMapper questionnaireMapper;

    /**
     * Recupera todos los cuestionarios disponibles en la base de datos.
     *
     * @return lista de todos los cuestionarios mapeados al modelo de dominio.
     */
    @Override
    public List<Questionnaire> getAll() {
        return questionnaireJpaRepository.findAll()
            .stream()
            .map(questionnaireMapper::toDomain)
            .toList();
    }

    /**
     * Busca un cuestionario por su abreviatura exacta.
     *
     * @param abbreviation abreviatura del cuestionario a buscar.
     * @return un {@link Optional} con el cuestionario encontrado, o vacío si no existe.
     */
    @Override
    public Optional<Questionnaire> getByAbbreviation(String abbreviation) {
        //        QuestionnaireEntity entity = questionnaireJpaRepository.findByAbbreviation(abbreviation);
        //        return Optional.ofNullable(entity).map(questionnaireMapper::toDomain);
        return questionnaireJpaRepository.findByAbbreviation(abbreviation)
            .map(questionnaireMapper::toDomain);
    }

    /**
     * Verifica si existe un cuestionario con la abreviatura especificada.
     *
     * @param abbreviation abreviatura a verificar.
     * @return {@code true} si existe un cuestionario con esa abreviatura, {@code false} en caso contrario.
     */
    @Override
    public boolean existsByAbbreviation(String abbreviation) {
        return questionnaireJpaRepository.existsByAbbreviation(abbreviation);
    }

    /**
     * Busca un cuestionario por su nombre exacto.
     *
     * @param name nombre del cuestionario a buscar.
     * @return un {@link Optional} con el cuestionario encontrado, o vacío si no existe.
     */
    @Override
    public Optional<Questionnaire> getByName(String name) {
        return questionnaireJpaRepository.findByName(name)
            .map(questionnaireMapper::toDomain);
    }

    /**
     * Verifica si existe un cuestionario con el nombre especificado.
     *
     * @param name nombre del cuestionario a verificar.
     * @return {@code true} si existe un cuestionario con ese nombre, {@code false} en caso contrario.
     */
    @Override
    public boolean existsByName(String name) {
        return questionnaireJpaRepository.existsByName(name);
    }
}
