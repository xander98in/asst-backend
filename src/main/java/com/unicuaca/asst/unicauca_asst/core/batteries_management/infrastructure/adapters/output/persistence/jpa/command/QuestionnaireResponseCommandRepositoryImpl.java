package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireResponseCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionnaireResponseEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.QuestionnaireResponseSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.QuestionnaireResponsePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del puerto de salida {@link QuestionnaireResponseCommandRepository}
 * utilizando JPA como tecnología de persistencia.
 *
 * Actúa como adaptador entre el dominio y la infraestructura para
 * guardar información de las respuestas a los cuestionarios.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class QuestionnaireResponseCommandRepositoryImpl implements QuestionnaireResponseCommandRepository {

    private final QuestionnaireResponseSpringJpaRepository questionnaireResponseSpringJpaRepository;
    private final QuestionnaireResponsePersistenceMapper questionnaireResponsePersistenceMapper;

    /**
     * Guarda una lista de respuestas a cuestionarios en la base de datos.
     *
     * @param responses Lista de respuestas a guardar.
     */
    @Override
    public void saveAll(List<QuestionnaireResponse> responses) {
        List<QuestionnaireResponseEntity> entities = questionnaireResponsePersistenceMapper.toEntityList(responses);
        questionnaireResponseSpringJpaRepository.saveAll(entities);
    }

    /**
     * Elimina todas las respuestas asociadas a un registro de gestión de cuestionario.
     *
     * @param recordId ID del registro de gestión de cuestionario.
     */
    @Override
    public void deleteByQuestionnaireManagementRecordId(Long recordId) {
        questionnaireResponseSpringJpaRepository.deleteByQuestionnaireManagementRecord_Id(recordId);
    }
}
