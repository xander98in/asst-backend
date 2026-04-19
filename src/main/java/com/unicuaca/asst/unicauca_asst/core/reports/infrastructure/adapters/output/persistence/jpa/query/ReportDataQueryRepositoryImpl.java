package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.query;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.BatteryManagementRecordSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.PersonEvaluatedDetailsSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.QuestionnaireManagementRecordSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.QuestionnaireResponseSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.BatteryManagementRecordPersistenceMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.PersonEvaluatedDetailsPersistenceMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.QuestionnaireManagementRecordPersistenceMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.QuestionnaireResponsePersistenceMapper;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.ReportDataQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del puerto de salida {@link ReportDataQueryRepository}
 * para el módulo de informes.
 *
 * <p>Reutiliza los repositorios JPA y persistence mappers existentes del módulo
 * de gestión de baterías para obtener los datos necesarios para calcular informes.</p>
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportDataQueryRepositoryImpl implements ReportDataQueryRepository {

    private final BatteryManagementRecordSpringJpaRepository batteryRecordRepository;
    private final PersonEvaluatedDetailsSpringJpaRepository personDetailsRepository;
    private final QuestionnaireManagementRecordSpringJpaRepository questionnaireRecordRepository;
    private final QuestionnaireResponseSpringJpaRepository questionnaireResponseRepository;
    private final BatteryManagementRecordPersistenceMapper batteryRecordMapper;
    private final PersonEvaluatedDetailsPersistenceMapper personDetailsMapper;
    private final QuestionnaireManagementRecordPersistenceMapper questionnaireRecordMapper;
    private final QuestionnaireResponsePersistenceMapper questionnaireResponseMapper;

    /**
     * Obtiene un registro de batería por su ID.
     * Debe incluir la persona evaluada asociada.
     *
     * @param id identificador del registro de batería
     * @return Optional con el registro encontrado
     */
    @Override
    public Optional<BatteryManagementRecord> getBatteryRecordById(Long id) {
        return batteryRecordRepository.findByIdWithRelations(id)
            .map(batteryRecordMapper::toDomain);
    }

    /**
     * Obtiene los detalles sociodemográficos y laborales de una persona
     * asociados a un registro de batería específico.
     *
     * @param batteryRecordId ID del registro de batería
     * @return Optional con los detalles encontrados
     */
    @Override
    public Optional<PersonEvaluatedDetails> getPersonDetailsByBatteryRecordId(Long batteryRecordId) {
        return personDetailsRepository.findByBatteryManagementRecordIdWithAll(batteryRecordId)
            .map(personDetailsMapper::toDomain);
    }

    /**
     * Obtiene todos los registros de gestión de cuestionarios de una batería.
     * Cada registro incluye el cuestionario asociado (con su abreviatura).
     *
     * @param batteryRecordId ID del registro de batería
     * @return lista de registros de gestión de cuestionarios
     */
    @Override
    public List<QuestionnaireManagementRecord> getQuestionnaireRecordsByBatteryId(Long batteryRecordId) {
        return questionnaireRecordRepository.findAllByBatteryManagementRecord_Id(batteryRecordId)
            .stream()
            .map(questionnaireRecordMapper::toDomain)
            .toList();
    }

    /**
     * Obtiene todas las respuestas de un registro de gestión de cuestionario,
     * incluyendo las relaciones con pregunta (con orden) y opción de respuesta (con valor).
     *
     * @param questionnaireRecordId ID del registro de gestión de cuestionario
     * @return lista de respuestas con relaciones cargadas
     */
    @Override
    public List<QuestionnaireResponse> getResponsesByQuestionnaireRecordId(Long questionnaireRecordId) {
        return questionnaireResponseRepository.findAllByQuestionnaireManagementRecordIdWithAll(questionnaireRecordId)
            .stream()
            .map(questionnaireResponseMapper::toDomain)
            .toList();
    }
}
