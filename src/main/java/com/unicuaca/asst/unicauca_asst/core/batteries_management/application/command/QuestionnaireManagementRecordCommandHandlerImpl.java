package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.QuestionnaireManagementRecordMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireManagementRecordCommandCUInputPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireManagementRecordCreateRequestDTO;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Implementación del manejador de comandos para la creación, actualización o eliminación de
 * registros de gestión de cuestionarios (QuestionnaireManagementRecord).
 */
@RequiredArgsConstructor
@Component
@Transactional
public class QuestionnaireManagementRecordCommandHandlerImpl implements QuestionnaireManagementRecordCommandHandler{

    private final QuestionnaireManagementRecordCommandCUInputPort inputPort;
    private final QuestionnaireManagementRecordMapper mapper;

    /**
     * Procesa la creación de un nuevo registro de gestión de cuestionario.
     *
     * @param dto datos de entrada validados para la creación
     * @return el DTO de respuesta que representa el registro de gestión de cuestionario creado
     */
    @Override
    public QuestionnaireManagementRecordResponseDTO createQuestionnaireManagementRecord(QuestionnaireManagementRecordCreateRequestDTO dto) {
        QuestionnaireManagementRecord domainRequest = mapper.toDomain(dto);
        QuestionnaireManagementRecord domainResponse = inputPort.createQuestionnaireManagementRecord(domainRequest);
        return mapper.toResponseDTO(domainResponse);
    }

    /**
     * Procesa la eliminación de un registro de gestión de cuestionario por su ID.
     * También se encarga de eliminar en cascada las respuestas asociadas a este registro de gestión de cuestionario, si existen.
     *
     * @param id identificador del registro a eliminar.
     */
    @Override
    public void deleteQuestionnaireManagementRecord(Long id) {
        inputPort.deleteQuestionnaireManagementRecord(id);
    }
}
