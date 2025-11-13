package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordStatusResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.QuestionnaireManagementRecordStatusMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireManagementRecordStatusQueryCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del manejador de consultas para los estados de
 * los registros de gestión de cuestionarios.
 */
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class QuestionnaireManagementRecordStatusHandlerImpl implements QuestionnaireManagementRecordStatusHandler {

    private final QuestionnaireManagementRecordStatusQueryCUInputPort questionnaireManagementRecordStatusQueryCUInputPort;
    private final QuestionnaireManagementRecordStatusMapper questionnaireManagementRecordStatusMapper;

    /**
     * Lista todos los estados de registro de gestión de cuestionarios. 
     * 
     * @return lista de {@link QuestionnaireManagementRecordStatusResponseDTO}.
     */
    @Override
    public List<QuestionnaireManagementRecordStatusResponseDTO> getAllStatuses() {
        return questionnaireManagementRecordStatusMapper.toResponseDTO(
            questionnaireManagementRecordStatusQueryCUInputPort.getAllQuestionnaireManagementRecordStatuses()
        );
    }

    /**
     * Obtiene un estado de registro de gestión de cuestionarios por su identificador.
     * 
     * @param id identificador del estado.
     * @return {@link QuestionnaireManagementRecordStatusResponseDTO} correspondiente.
     */
    @Override
    public QuestionnaireManagementRecordStatusResponseDTO getById(Long id) {
        return questionnaireManagementRecordStatusMapper.toResponseDTO(
            questionnaireManagementRecordStatusQueryCUInputPort.getQuestionnaireManagementRecordStatusById(id)
        );
    }

    /**
     * Obtiene un estado de registro de gestión de cuestionarios por su nombre.
     * 
     * @param name nombre del estado (por ejemplo, "Creado", "En diligenciamiento").
     * @return {@link QuestionnaireManagementRecordStatusResponseDTO} correspondiente.
     */
    @Override
    public QuestionnaireManagementRecordStatusResponseDTO getByName(String name) {
        return questionnaireManagementRecordStatusMapper.toResponseDTO(
            questionnaireManagementRecordStatusQueryCUInputPort.getQuestionnaireManagementRecordStatusByName(name)
        );
    }
}