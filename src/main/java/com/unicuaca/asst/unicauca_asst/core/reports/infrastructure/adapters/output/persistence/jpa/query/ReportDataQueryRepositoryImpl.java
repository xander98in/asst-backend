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

    @Override
    public Optional<BatteryManagementRecord> getBatteryRecordById(Long id) {
        return batteryRecordRepository.findByIdWithRelations(id)
            .map(batteryRecordMapper::toDomain);
    }

    @Override
    public Optional<PersonEvaluatedDetails> getPersonDetailsByBatteryRecordId(Long batteryRecordId) {
        return personDetailsRepository.findByBatteryManagementRecordIdWithAll(batteryRecordId)
            .map(personDetailsMapper::toDomain);
    }

    @Override
    public List<QuestionnaireManagementRecord> getQuestionnaireRecordsByBatteryId(Long batteryRecordId) {
        return questionnaireRecordRepository.findAllByBatteryManagementRecord_Id(batteryRecordId)
            .stream()
            .map(questionnaireRecordMapper::toDomain)
            .toList();
    }

    @Override
    public List<QuestionnaireResponse> getResponsesByQuestionnaireRecordId(Long questionnaireRecordId) {
        return questionnaireResponseRepository.findAllByQuestionnaireManagementRecordIdWithAll(questionnaireRecordId)
            .stream()
            .map(questionnaireResponseMapper::toDomain)
            .toList();
    }
}
