package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordStatusQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionnaireManagementRecordStatusEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.QuestionnaireManagementRecordStatusSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.QuestionnaireManagementRecordStatusPersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida {@link QuestionnaireManagementRecordStatusQueryRepository}
 * utilizando JPA como tecnología de persistencia.
 *
 * Actúa como adaptador entre el dominio y la infraestructura.
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class QuestionnaireManagementRecordStatusQueryRepositoryImpl implements QuestionnaireManagementRecordStatusQueryRepository{

    private final QuestionnaireManagementRecordStatusSpringJpaRepository questionnaireManagementRecordStatusSpringJpaRepository;
    private final QuestionnaireManagementRecordStatusPersistenceMapper questionnaireManagementRecordStatusPersistenceMapper;

    /**
     * Consulta un estado de registro de gestión de cuestionarios por su identificador único.
     *
     * @param id identificador del estado
     * @return un {@link Optional} con el estado encontrado o vacío si no existe
     */
    @Override
    public Optional<QuestionnaireManagementRecordStatus> getQuestionnaireManagementRecordStatusById(Long id) {
        return questionnaireManagementRecordStatusSpringJpaRepository.findById(id)
            .map(questionnaireManagementRecordStatusPersistenceMapper::toDomain);
    }

    /**
     * Consulta un estado de registro de gestión de cuestionarios por su nombre.
     *
     * @param name nombre del estado
     * @return un {@link Optional} con el estado encontrado o vacío si no existe
     */
    @Override
    public Optional<QuestionnaireManagementRecordStatus> getQuestionnaireManagementRecordStatusByName(String name) {
        return questionnaireManagementRecordStatusSpringJpaRepository.findByName(name)
            .map(questionnaireManagementRecordStatusPersistenceMapper::toDomain);
    }

    /**
     * Verifica si existe un estado de registro de gestión de cuestionarios con el ID proporcionado.
     *
     * @param id identificador del estado
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    @Override
    public boolean existsById(Long id) {
        return questionnaireManagementRecordStatusSpringJpaRepository.existsById(id);
    }

    /**
     * Verifica si existe un estado de registro de gestión de cuestionarios con el nombre proporcionado.
     *
     * @param name nombre del estado
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    @Override
    public boolean existsByName(String name) {
        return questionnaireManagementRecordStatusSpringJpaRepository.existsByName(name);
    }

    /**
     * Obtiene todos los estados de registros de gestión de cuestionarios.
     *
     * @return lista de estados (posiblemente vacía si no hay resultados)
     */
    @Override
    public List<QuestionnaireManagementRecordStatus> getAllQuestionnaireManagementRecordStatuses() {
        List<QuestionnaireManagementRecordStatusEntity> entities =
            questionnaireManagementRecordStatusSpringJpaRepository.findAll();
        return entities.stream()
            .map(questionnaireManagementRecordStatusPersistenceMapper::toDomain)
            .toList();
    }
}
