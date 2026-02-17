package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.QuestionnaireManagementRecordSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.QuestionnaireManagementRecordPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del puerto de salida {@link QuestionnaireManagementRecordQueryRepository}
 * utilizando JPA como tecnología de persistencia.
 *
 * Actúa como adaptador entre el dominio y la infraestructura.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class QuestionnaireManagementRecordQueryRepositoryImpl implements QuestionnaireManagementRecordQueryRepository {

    private final QuestionnaireManagementRecordSpringJpaRepository questionnaireManagementRecordSpringJpaRepository;
    private final QuestionnaireManagementRecordPersistenceMapper mapper;

    /**
     * Verifica si existe un registro de gestión de cuestionario
     * asociado a un registro de gestión de batería específico
     * y a una colección de abreviaciones de cuestionarios.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería.
     * @param abbreviations             Colección de abreviaciones de cuestionarios.
     * @return {@code true} si existe al menos un registro que cumple con los criterios, {@code false} en caso contrario.
     */
    @Override
    public boolean existsByBatteryManagementRecordIdAndQuestionnaireAbbreviationIn(Long batteryManagementRecordId, Collection<String> abbreviations) {
        return this.questionnaireManagementRecordSpringJpaRepository.existsByBatteryManagementRecordIdAndQuestionnaireAbbreviations(
            batteryManagementRecordId,
            abbreviations
        );
    }

    /**
     * Verifica si existe al menos un registro de gestión de cuestionario
     * asociado a un registro de gestión de batería específico.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería.
     * @return {@code true} si existe al menos un registro asociado, {@code false} en caso contrario.
     */
    @Override
    public boolean existsByBatteryManagementRecordId(Long batteryManagementRecordId) {
        return this.questionnaireManagementRecordSpringJpaRepository.existsByBatteryManagementRecord_Id(batteryManagementRecordId);
    }

    /**
     * Busca un registro de gestión de cuestionario por el ID del registro de gestión de batería
     * y la abreviatura del cuestionario, incluyendo todas las relaciones necesarias.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería.
     * @param questionnaireAbbreviation Abreviatura del cuestionario.
     * @return Un {@link Optional} que contiene el registro encontrado, o vacío si no existe.
     */
    @Override
    public Optional<QuestionnaireManagementRecord> findByBatteryManagementRecordIdAndQuestionnaireAbbreviationWithAll(
        Long batteryManagementRecordId,
        String questionnaireAbbreviation
    ) {
        return questionnaireManagementRecordSpringJpaRepository
            .findByBatteryManagementRecordIdAndQuestionnaireAbbreviationWithAll(batteryManagementRecordId, questionnaireAbbreviation)
            .map(mapper::toDomain);
    }

    /**
     * Busca un registro de gestión de cuestionario por su ID, incluyendo todas las relaciones necesarias.
     *
     * @param id ID del registro de gestión de cuestionario.
     * @return Un {@link Optional} que contiene el registro encontrado, o vacío si no existe.
     */
    @Override
    public Optional<QuestionnaireManagementRecord> findByIdWithAll(Long id) {
        return questionnaireManagementRecordSpringJpaRepository.findByIdWithAll(id)
            .map(mapper::toDomain);
    }

    /**
     * Verifica si existe un registro de gestión de cuestionario específico
     * para una batería y un cuestionario dados (por sus IDs).
     *
     * @param batteryId ID del registro de gestión de batería.
     * @param questionnaireId ID del cuestionario.
     * @return true si ya existe, false si no.
     */
    @Override
    public boolean existsByBatteryManagementRecordIdAndQuestionnaireId(Long batteryId, Long questionnaireId) {
        return questionnaireManagementRecordSpringJpaRepository.existsByBatteryManagementRecord_IdAndQuestionnaire_Id(batteryId, questionnaireId);
    }

    /**
     * Obtiene la lista de abreviaturas de los cuestionarios asociados a una batería
     * que se encuentren en un estado específico (ej: "Diligenciado").
     *
     * @param batteryId ID del registro de gestión de batería.
     * @param statusName Nombre del estado (por ejemplo: "Diligenciado").
     * @return Lista de abreviaturas de cuestionarios que cumplen los criterios.
     */
    @Override
    public List<String> findAbbreviationsByBatteryIdAndStatusName(Long batteryId, String statusName) {
        return questionnaireManagementRecordSpringJpaRepository
            .findAbbreviationsByBatteryIdAndStatusName(batteryId, statusName);
    }
}
