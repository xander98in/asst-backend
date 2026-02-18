package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionnaireManagementRecordEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.QuestionnaireManagementRecordSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.QuestionnaireManagementRecordPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del puerto de salida {@link QuestionnaireManagementRecordCommandRepository}
 * utilizando JPA como tecnología de persistencia.
 *
 * Actúa como adaptador entre el dominio y la infraestructura para
 * persistir o eliminar información de los registros de gestión de cuestionarios.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class QuestionnaireManagementRecordCommandRepositoryImpl implements QuestionnaireManagementRecordCommandRepository {

    private final QuestionnaireManagementRecordSpringJpaRepository jpaRepository;
    private final QuestionnaireManagementRecordPersistenceMapper mapper;

    /**
     * Guarda un registro de gestión de cuestionario.
     *
     * @param record El modelo de dominio con toda la información hidratada (relaciones completas).
     * @return El modelo de dominio persistido con el ID generado y fechas de auditoría.
     */
    @Override
    public QuestionnaireManagementRecord save(QuestionnaireManagementRecord record) {
        QuestionnaireManagementRecordEntity entity = mapper.toEntity(record);
        QuestionnaireManagementRecordEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    /**
     * Elimina un registro de gestión de cuestionario por su ID.
     *
     * @param id Identificador del registro.
     */
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
