package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Implementación de los casos de uso de consulta de cuestionarios.
 *
 * Orquesta la lógica de negocio de solo lectura apoyándose en el puerto de salida
 * {@link QuestionnaireQueryRepository} y estandariza errores a través de
 * {@link ResultFormatterOutputPort}.
 */
@RequiredArgsConstructor
public class QuestionnaireQueryService implements QuestionnaireQueryCUInputPort {

    private final QuestionnaireQueryRepository questionnaireQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Recupera todos los cuestionarios disponibles en el sistema.
     *
     * @return una lista con todos los cuestionarios registrados.
     */
    @Override
    public List<Questionnaire> getAllQuestionnaires() {
        return questionnaireQueryRepository.getAll();
    }

    /**
     * Obtiene un cuestionario a partir de su abreviatura exacta.
     *
     * @param abbreviation abreviatura del cuestionario (por ejemplo, "ILA", "ILB", "EXT", "EST").
     * @return el cuestionario correspondiente a la abreviatura indicada.
     */
    @Override
    public Questionnaire getQuestionnaireByAbbreviation(String abbreviation) {
        return questionnaireQueryRepository.getByAbbreviation(abbreviation)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(
                        ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "El cuestionario con abreviatura '" + abbreviation + "' no fue encontrado."
                    )
                );
                return null; // requerido por el compilador; nunca se ejecuta
            });
    }
}
