package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
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
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Lista todos los espacios de análisis creados por un usuario.
     *
     * @param userId ID del usuario
     * @return lista de espacios de análisis del usuario
     */
    @Override
    public List<AnalysisSpace> getAnalysisSpacesByUser(Long userId) {
        return analysisSpaceQueryRepository.findAllByCreatorUserId(userId);
    }

    /**
     * Obtiene un espacio de análisis con sus baterías asociadas,
     * validando que pertenece al usuario solicitante.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario que realiza la consulta
     * @return el espacio de análisis con sus baterías
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
}
