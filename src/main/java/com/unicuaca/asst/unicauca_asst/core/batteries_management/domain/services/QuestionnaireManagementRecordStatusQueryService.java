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
 * Implementación de los casos de uso de consulta para los estados de los registros de gestión de cuestionarios.
 *
 * Esta clase pertenece a la capa de dominio y orquesta la lógica de negocio para la recuperación de los estados, 
 * delegando la persistencia en los puertos de salida correspondientes.
 */
@RequiredArgsConstructor
public class QuestionnaireManagementRecordStatusQueryService implements QuestionnaireManagementRecordStatusQueryCUInputPort {

    private final QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Obtiene un estado de registro de gestión de cuestionarios por su identificador.
     *
     * @param id identificador del estado
     * @return el {@link QuestionnaireManagementRecordStatus} encontrado
     * @throws com.unicuaca.asst.unicauca_asst.common.exceptions.BusinessException
     *         si el estado no existe (delegado al {@code ResultFormatterOutputPort})
     */
    @Override
    public QuestionnaireManagementRecordStatus getQuestionnaireManagementRecordStatusById(Long id) {
        return fetchOrThrow(
            () -> questionnaireManagementRecordStatusQueryRepository.getQuestionnaireManagementRecordStatusById(id),
            String.format("El estado de registro de gestión de cuestionarios con ID %d no fue encontrado.", id)
        );
    }

    /**
     * Obtiene un estado de registro de gestión de cuestionarios por su nombre.
     *
     * @param name nombre del estado
     * @return el {@link QuestionnaireManagementRecordStatus} encontrado
     * @throws com.unicuaca.asst.unicauca_asst.common.exceptions.BusinessException
     *         si el estado no existe (delegado al {@code ResultFormatterOutputPort})
     */
    @Override
    public QuestionnaireManagementRecordStatus getQuestionnaireManagementRecordStatusByName(String name) {
        return fetchOrThrow(
            () -> questionnaireManagementRecordStatusQueryRepository.getQuestionnaireManagementRecordStatusByName(name),
            String.format("El estado de registro de gestión de cuestionarios con nombre '%s' no fue encontrado.", name)
        );
    }

    /**
     * Obtiene todos los estados de registro de gestión de cuestionarios.
     *
     * @return lista de {@link QuestionnaireManagementRecordStatus}
     */
    @Override
    public List<QuestionnaireManagementRecordStatus> getAllQuestionnaireManagementRecordStatuses() {
        return questionnaireManagementRecordStatusQueryRepository.getAllQuestionnaireManagementRecordStatuses();
    }

    /**
     * Helper genérico para obtener un recurso o lanzar excepción estandarizada.
     *
     * @param fetcher     supplier que obtiene el Optional del recurso
     * @param notFoundMsg mensaje específico cuando no existe
     * @return recurso resuelto
     */
    private <T> T fetchOrThrow(Supplier<Optional<T>> fetcher, String notFoundMsg) {
        return fetcher.get().orElseGet(() -> {
            resultFormatter.throwEntityNotFound(
                ErrorCode.ENTITY_NOT_FOUND.getCode(),
                String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), notFoundMsg)
            );
            return null; // requerido por el compilador
        });
    }

}
