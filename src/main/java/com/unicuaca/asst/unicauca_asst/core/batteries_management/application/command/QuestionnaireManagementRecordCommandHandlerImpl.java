package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireManagementRecordCreateRequestDTO;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del manejador de comandos para la creación de
 * registros de gestión de cuestionarios (QuestionnaireManagementRecord).
 * 
 */
@RequiredArgsConstructor
@Component
@Transactional
public class QuestionnaireManagementRecordCommandHandlerImpl implements QuestionnaireManagementRecordCommandHandler{

    @Override
    public void createQuestionnaireManagementRecord(QuestionnaireManagementRecordCreateRequestDTO dto) {
        System.out.println("\n\ncreateQuestionnaireManagementRecord called with DTO: " + dto + "\n");
    }
}
