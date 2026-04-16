package com.unicuaca.asst.unicauca_asst.core.reports.application.command;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.AnalysisSpaceAddBatteriesRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.AnalysisSpaceCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.mappers.AnalysisSpaceMapper;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.AnalysisSpaceCommandCUInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del handler de comandos para espacios de análisis.
 *
 * <p>Delega la lógica de negocio al puerto de entrada de dominio y transforma
 * los resultados a DTOs de respuesta.</p>
 */
@RequiredArgsConstructor
@Component
@Transactional
public class AnalysisSpaceCommandHandlerImpl implements AnalysisSpaceCommandHandler {

    private final AnalysisSpaceCommandCUInputPort analysisSpaceCommandCUInputPort;
    private final AnalysisSpaceMapper analysisSpaceMapper;

    @Override
    public AnalysisSpaceResponseDTO createAnalysisSpace(AnalysisSpaceCreateRequestDTO request, Long userId) {
        AnalysisSpace space = analysisSpaceCommandCUInputPort.createAnalysisSpace(
            request.getName(), request.getEvaluatorId(), userId);
        return analysisSpaceMapper.toResponseDTO(space);
    }

    @Override
    public void addBatteriesToSpace(Long spaceId, AnalysisSpaceAddBatteriesRequestDTO request, Long userId) {
        analysisSpaceCommandCUInputPort.addBatteriesToSpace(spaceId, request.getBatteryRecordIds(), userId);
    }

    @Override
    public void removeBatteryFromSpace(Long spaceId, Long batteryRecordId, Long userId) {
        analysisSpaceCommandCUInputPort.removeBatteryFromSpace(spaceId, batteryRecordId, userId);
    }

    @Override
    public void deleteAnalysisSpace(Long spaceId, Long userId) {
        analysisSpaceCommandCUInputPort.deleteAnalysisSpace(spaceId, userId);
    }
}
