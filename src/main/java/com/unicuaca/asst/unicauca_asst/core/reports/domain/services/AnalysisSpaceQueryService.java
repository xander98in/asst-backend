package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordInformation;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.BatteryManagementRecordQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.AnalysisSpaceQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la consulta de espacios de análisis.
 *
 * <p>Implementa la lógica para listar y obtener espacios de análisis,
 * validando que el usuario tenga acceso al espacio solicitado.</p>
 */
@RequiredArgsConstructor
public class AnalysisSpaceQueryService implements AnalysisSpaceQueryCUInputPort {

    private final AnalysisSpaceQueryRepository analysisSpaceQueryRepository;
    private final BatteryManagementRecordQueryCUInputPort batteryManagementRecordQueryCUInputPort;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnalysisSpace> getAnalysisSpacesByUser(Long userId) {
        return analysisSpaceQueryRepository.findAllByCreatorUserId(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnalysisSpace getAnalysisSpaceById(Long spaceId, Long userId) {
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

        return space;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Reutiliza {@link #getAnalysisSpaceById(Long, Long)} para validar la existencia
     * del espacio y la pertenencia al usuario antes de delegar la consulta paginada al
     * módulo de baterías.</p>
     */
    @Override
    public Page<BatteryManagementRecordInformation> getSpaceBatteriesWithMultifilter(
        Long spaceId, Long userId,
        String identificationNumber, String workAreaName,
        LocalDateTime dateFrom, LocalDateTime dateTo,
        Long identificationTypeId, Long jobPositionTypeId,
        String intralaboralForm,
        Integer page, Integer size
    ) {
        getAnalysisSpaceById(spaceId, userId);

        return batteryManagementRecordQueryCUInputPort.listByAnalysisSpaceWithMultipleFilters(
            spaceId,
            identificationNumber, workAreaName, dateFrom, dateTo,
            identificationTypeId, jobPositionTypeId, intralaboralForm,
            page, size
        );
    }
}
