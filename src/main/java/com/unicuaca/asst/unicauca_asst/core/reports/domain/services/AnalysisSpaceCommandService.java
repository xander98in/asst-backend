package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.AnalysisSpaceCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.ReportDataQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la gestión de comandos de espacios de análisis.
 *
 * <p>Orquesta la creación, modificación y eliminación de espacios de análisis,
 * validando reglas de negocio como el tope máximo por usuario, unicidad del nombre
 * y estado de las baterías asociadas.</p>
 */
@RequiredArgsConstructor
public class AnalysisSpaceCommandService implements AnalysisSpaceCommandCUInputPort {

    private static final int MAX_SPACES_PER_USER = 10;

    private final AnalysisSpaceCommandRepository analysisSpaceCommandRepository;
    private final AnalysisSpaceQueryRepository analysisSpaceQueryRepository;
    private final ReportDataQueryRepository reportDataQueryRepository;
    private final EvaluatorQueryRepository evaluatorQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Crea un nuevo espacio de análisis validando el tope máximo, la unicidad del nombre
     * y la existencia/propiedad del evaluador asociado.
     *
     * @param name          nombre descriptivo del espacio
     * @param evaluatorId   ID del evaluador asociado al espacio
     * @param creatorUserId ID del usuario que crea el espacio
     * @return el espacio de análisis creado
     */
    @Override
    public AnalysisSpace createAnalysisSpace(String name, Long evaluatorId, Long creatorUserId) {
        // Validar tope de espacios por usuario
        int currentCount = analysisSpaceCommandRepository.countByCreatorUserId(creatorUserId);
        if (currentCount >= MAX_SPACES_PER_USER) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.ANALYSIS_SPACE_LIMIT_REACHED,
                "user.report.space_limit_reached",
                creatorUserId, MAX_SPACES_PER_USER
            );
        }

        // Validar nombre no duplicado para el mismo usuario
        if (analysisSpaceCommandRepository.existsByNameAndCreatorUserId(name, creatorUserId)) {
            resultFormatter.throwEntityAlreadyExists(
                ErrorCode.ANALYSIS_SPACE_NAME_EXISTS,
                "user.report.space_name_exists",
                name
            );
        }

        // Validar existencia y propiedad del evaluador
        Optional<Evaluator> optionalEvaluator = evaluatorQueryRepository.findById(evaluatorId);
        if (optionalEvaluator.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.EVALUATOR_NOT_FOUND,
                "user.report.evaluator_not_found",
                evaluatorId
            );
        }
        Evaluator evaluator = optionalEvaluator.get();

        if (!evaluator.getCreatorUserId().equals(creatorUserId)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.EVALUATOR_ACCESS_DENIED,
                "user.report.evaluator_access_denied",
                creatorUserId, evaluatorId
            );
        }

        // Crear y persistir el espacio
        AnalysisSpace analysisSpace = AnalysisSpace.builder()
            .name(name)
            .evaluatorId(evaluatorId)
            .creatorUserId(creatorUserId)
            .createdAt(LocalDateTime.now())
            .build();

        return analysisSpaceCommandRepository.save(analysisSpace);
    }

    /**
     * Agrega baterías cerradas a un espacio de análisis existente.
     *
     * @param spaceId          ID del espacio de análisis
     * @param batteryRecordIds IDs de los registros de batería a agregar
     * @param userId           ID del usuario que realiza la operación
     */
    @Override
    public void addBatteriesToSpace(Long spaceId, List<Long> batteryRecordIds, Long userId) {
        // Obtener y validar el espacio
        Optional<AnalysisSpace> optionalSpace = analysisSpaceQueryRepository.findByIdWithBatteries(spaceId);
        if (optionalSpace.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.ANALYSIS_SPACE_NOT_FOUND,
                "user.report.space_not_found",
                spaceId
            );
        }
        AnalysisSpace space = optionalSpace.get();

        // Validar que el espacio pertenece al usuario
        if (!space.getCreatorUserId().equals(userId)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.ANALYSIS_SPACE_ACCESS_DENIED,
                "user.report.space_access_denied",
                userId, spaceId
            );
        }

        // Validar y agregar cada batería
        for (Long batteryRecordId : batteryRecordIds) {
            // Validar que la batería existe
            Optional<BatteryManagementRecord> optionalRecord = reportDataQueryRepository
                .getBatteryRecordById(batteryRecordId);
            if (optionalRecord.isEmpty()) {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.battery.query_not_found",
                    batteryRecordId
                );
            }
            BatteryManagementRecord record = optionalRecord.get();

            // Validar que la batería está cerrada
            if (!BatteryManagementRecordStatusCode.CLOSED.getDescription().equals(record.getStatus().getName())) {
                resultFormatter.throwBusinessRuleViolation(
                    ErrorCode.REPORT_BATTERY_NOT_CLOSED,
                    "user.report.battery_not_closed",
                    batteryRecordId
                );
            }

            // Si ya está en el espacio, simplemente saltarla
            if (analysisSpaceQueryRepository.existsBatteryInSpace(spaceId, batteryRecordId)) {
                continue;
            }

            analysisSpaceCommandRepository.addBatteryToSpace(spaceId, batteryRecordId);
        }
    }

    /**
     * Remueve una batería de un espacio de análisis, validando que no quede vacío.
     *
     * @param spaceId             ID del espacio de análisis
     * @param batteryRecordId     ID del registro de batería a remover
     * @param userId              ID del usuario que realiza la operación
     */
    @Override
    public void removeBatteryFromSpace(Long spaceId, Long batteryRecordId, Long userId) {
        // Obtener y validar el espacio
        Optional<AnalysisSpace> optionalSpace = analysisSpaceQueryRepository.findByIdWithBatteries(spaceId);
        if (optionalSpace.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.ANALYSIS_SPACE_NOT_FOUND,
                "user.report.space_not_found",
                spaceId
            );
        }
        AnalysisSpace space = optionalSpace.get();

        // Validar que el espacio pertenece al usuario
        if (!space.getCreatorUserId().equals(userId)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.ANALYSIS_SPACE_ACCESS_DENIED,
                "user.report.space_access_denied",
                userId, spaceId
            );
        }

        // Validar que la batería está en el espacio
        if (!analysisSpaceQueryRepository.existsBatteryInSpace(spaceId, batteryRecordId)) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.ANALYSIS_SPACE_BATTERY_NOT_FOUND,
                "user.report.space_battery_not_found",
                batteryRecordId, spaceId
            );
        }

        // Validar que el espacio no quede vacío
        if (space.getBatteries() != null && space.getBatteries().size() <= 1) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.ANALYSIS_SPACE_MIN_BATTERIES,
                "user.report.space_min_batteries"
            );
        }

        analysisSpaceCommandRepository.removeBatteryFromSpace(spaceId, batteryRecordId);
    }

    /**
     * Elimina un espacio de análisis completo con todas sus asociaciones.
     *
     * @param spaceId ID del espacio de análisis a eliminar
     * @param userId  ID del usuario que realiza la operación
     */
    @Override
    public void deleteAnalysisSpace(Long spaceId, Long userId) {
        // Obtener y validar el espacio
        Optional<AnalysisSpace> optionalSpace = analysisSpaceQueryRepository.findByIdWithBatteries(spaceId);
        if (optionalSpace.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.ANALYSIS_SPACE_NOT_FOUND,
                "user.report.space_not_found",
                spaceId
            );
        }
        AnalysisSpace space = optionalSpace.get();

        // Validar que el espacio pertenece al usuario
        if (!space.getCreatorUserId().equals(userId)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.ANALYSIS_SPACE_ACCESS_DENIED,
                "user.report.space_access_denied",
                userId, spaceId
            );
        }

        analysisSpaceCommandRepository.deleteById(spaceId);
    }
}
