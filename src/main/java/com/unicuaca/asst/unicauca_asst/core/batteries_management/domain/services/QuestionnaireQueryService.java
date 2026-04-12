package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de dominio para la consulta de catálogos de cuestionarios.
 * 
 * <p>Esta clase orquesta la lógica de negocio de solo lectura para los cuestionarios disponibles
 * en el sistema. Utiliza i18n para reportar fallos de búsqueda mediante abreviaturas semánticas.</p>
 */
@RequiredArgsConstructor
public class QuestionnaireQueryService implements QuestionnaireQueryCUInputPort {

    private final QuestionnaireQueryRepository questionnaireQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Recupera todos los cuestionarios registrados en el sistema.
     *
     * @return lista completa de cuestionarios
     */
    @Override
    public List<Questionnaire> getAllQuestionnaires() {
        return questionnaireQueryRepository.getAll();
    }

    /**
     * Obtiene un cuestionario específico a partir de su abreviatura única.
     *
     * @param abbreviation abreviatura del cuestionario (ej: 'ILA', 'EXT')
     * @return el cuestionario encontrado
     */
    @Override
    public Questionnaire getQuestionnaireByAbbreviation(String abbreviation) {
        Optional<Questionnaire> questionnaireOpt = questionnaireQueryRepository.getByAbbreviation(abbreviation);
        if (questionnaireOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF,
                "user.questionnaire.not_found",
                abbreviation
            );
        }
        return questionnaireOpt.get();
    }
}
