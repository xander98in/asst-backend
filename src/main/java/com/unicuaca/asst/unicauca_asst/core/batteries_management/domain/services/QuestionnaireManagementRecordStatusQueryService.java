package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireManagementRecordStatusQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordStatusQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la consulta de estados de registros de gestión de cuestionarios.
 * 
 * <p>Esta clase orquesta la recuperación de los estados válidos para los cuestionarios,
 * delegando la persistencia en el repositorio correspondiente. Implementa i18n para reportar
 * fallos de búsqueda de forma profesional.</p>
 */
@RequiredArgsConstructor
public class QuestionnaireManagementRecordStatusQueryService implements QuestionnaireManagementRecordStatusQueryCUInputPort {

    private final QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Obtiene un estado específico mediante su identificador único.
     *
     * @param id identificador del estado
     * @return el estado encontrado
     */
    @Override
    public QuestionnaireManagementRecordStatus getQuestionnaireManagementRecordStatusById(Long id) {
        return fetchOrThrow(
            () -> questionnaireManagementRecordStatusQueryRepository.getQuestionnaireManagementRecordStatusById(id),
            id.toString(),
            "user.questionnaire_management.status_id_not_found"
        );
    }

    /**
     * Obtiene un estado específico mediante su nombre descriptivo.
     *
     * @param name nombre del estado (ej: 'Creado', 'Diligenciado')
     * @return el estado encontrado
     */
    @Override
    public QuestionnaireManagementRecordStatus getQuestionnaireManagementRecordStatusByName(String name) {
        return fetchOrThrow(
            () -> questionnaireManagementRecordStatusQueryRepository.getQuestionnaireManagementRecordStatusByName(name),
            name,
            "user.questionnaire_management.status_name_not_found"
        );
    }

    /**
     * Obtiene la lista completa de todos los estados de cuestionario disponibles en el sistema.
     *
     * @return lista de estados
     */
    @Override
    public List<QuestionnaireManagementRecordStatus> getAllQuestionnaireManagementRecordStatuses() {
        return questionnaireManagementRecordStatusQueryRepository.getAllQuestionnaireManagementRecordStatuses();
    }

    /**
     * Helper centralizado para la resolución de opcionales con lanzamiento de excepción i18n.
     *
     * @param fetcher supplier que obtiene el Optional del recurso
     * @param reference valor de referencia (ID o Nombre) para el log técnico {0}
     * @param userKey clave de traducción para el mensaje de usuario final
     * @return el recurso encontrado
     */
    private <T> T fetchOrThrow(Supplier<Optional<T>> fetcher, String reference, String userKey) {
        return fetcher.get().orElseGet(() -> {
            resultFormatter.throwEntityNotFound(
                ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND,
                userKey,
                reference
            );
            return null;
        });
    }
}
