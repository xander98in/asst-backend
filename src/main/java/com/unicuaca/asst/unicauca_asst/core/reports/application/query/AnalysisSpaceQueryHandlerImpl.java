package com.unicuaca.asst.unicauca_asst.core.reports.application.query;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceSummaryResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.mappers.AnalysisSpaceMapper;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.AnalysisSpaceQueryCUInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del handler de consultas para espacios de análisis.
 *
 * <p>Delega al puerto de entrada de dominio y transforma los resultados
 * a DTOs de respuesta.</p>
 */
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class AnalysisSpaceQueryHandlerImpl implements AnalysisSpaceQueryHandler {

    private final AnalysisSpaceQueryCUInputPort analysisSpaceQueryCUInputPort;
    private final AnalysisSpaceMapper analysisSpaceMapper;

    @Override
    public List<AnalysisSpaceSummaryResponseDTO> getAnalysisSpacesByUser(Long userId) {
        List<AnalysisSpace> spaces = analysisSpaceQueryCUInputPort.getAnalysisSpacesByUser(userId);
        return spaces.stream()
            .map(analysisSpaceMapper::toSummaryDTO)
            .toList();
    }

    @Override
    public AnalysisSpaceResponseDTO getAnalysisSpaceById(Long spaceId, Long userId) {
        AnalysisSpace space = analysisSpaceQueryCUInputPort.getAnalysisSpaceById(spaceId, userId);
        return analysisSpaceMapper.toResponseDTO(space);
    }
}
