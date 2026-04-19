package com.unicuaca.asst.unicauca_asst.core.reports.application.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.EvaluatorResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.mappers.EvaluatorMapper;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.EvaluatorQueryCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del handler de consultas para evaluadores.
 *
 * <p>Delega al puerto de entrada de dominio y transforma los resultados
 * a DTOs de respuesta.</p>
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EvaluatorQueryHandlerImpl implements EvaluatorQueryHandler {

    private final EvaluatorQueryCUInputPort evaluatorQueryCUInputPort;
    private final EvaluatorMapper evaluatorMapper;

    /**
     * Lista los evaluadores registrados por un usuario.
     *
     * @param userId ID del usuario creador
     * @return lista de DTOs de evaluadores
     */
    @Override
    public List<EvaluatorResponseDTO> getEvaluatorsByUser(Long userId) {
        return evaluatorQueryCUInputPort.getEvaluatorsByUser(userId).stream()
            .map(evaluatorMapper::toResponseDTO)
            .toList();
    }

    /**
     * Obtiene un evaluador específico por su ID.
     *
     * @param evaluatorId ID del evaluador
     * @param userId      ID del usuario que realiza la operación
     * @return DTO del evaluador solicitado
     */
    @Override
    public EvaluatorResponseDTO getEvaluatorById(Long evaluatorId, Long userId) {
        Evaluator evaluator = evaluatorQueryCUInputPort.getEvaluatorById(evaluatorId, userId);
        return evaluatorMapper.toResponseDTO(evaluator);
    }

    /**
     * Lista evaluadores del usuario de forma paginada con búsqueda opcional.
     *
     * @param userId     ID del usuario autenticado
     * @param searchTerm término de búsqueda (puede ser null)
     * @param page       número de página
     * @param size       tamaño de página
     * @return página de DTOs de evaluadores
     */
    @Override
    public Page<EvaluatorResponseDTO> getEvaluatorsByUserPaged(Long userId, String searchTerm, Integer page, Integer size) {
        Page<Evaluator> evaluatorPage = evaluatorQueryCUInputPort.getEvaluatorsByUserPaged(userId, searchTerm, page, size);
        List<EvaluatorResponseDTO> dtoList = evaluatorPage.getContent().stream()
            .map(evaluatorMapper::toResponseDTO).toList();
        return new PageImpl<>(dtoList, evaluatorPage.getPageable(), evaluatorPage.getTotalElements());
    }
}
