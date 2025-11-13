package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;

/**
 * Puerto de entrada para los casos de uso relacionados con la consulta de los estados de los registros de gestión de cuestionarios.
 *
 * <p>Esta interfaz define las operaciones de tipo "Query" del modelo {@link QuestionnaireManagementRecordStatus}, 
 * permitiendo que los adaptadores de entrada (como controladores REST o handlers de aplicación) interactúen
 * con la lógica de negocio sin acoplarse directamente a su implementación.</p>
 *
 * <p>Forma parte de la capa de dominio dentro de la arquitectura hexagonal, sirviendo como punto de acceso a los casos de uso 
 * encargados de recuperar la información de los estados de registro de gestión de cuestionarios.</p>
 */
public interface QuestionnaireManagementRecordStatusQueryCUInputPort {

    /**
     * Obtiene un estado de registro de gestión de cuestionarios por su identificador.
     *
     * @param id identificador del estado
     * @return el {@link QuestionnaireManagementRecordStatus} encontrado
     * @throws com.unicuaca.asst.unicauca_asst.common.exceptions.BusinessException
     *         si el estado no existe (delegado al {@code ResultFormatterOutputPort})
     */
    QuestionnaireManagementRecordStatus getQuestionnaireManagementRecordStatusById(Long id);

    /**
     * Obtiene un estado de registro de gestión de cuestionarios por su nombre.
     *
     * @param name nombre del estado
     * @return el {@link QuestionnaireManagementRecordStatus} encontrado
     * @throws com.unicuaca.asst.unicauca_asst.common.exceptions.BusinessException
     *         si el estado no existe (delegado al {@code ResultFormatterOutputPort})
     */
    QuestionnaireManagementRecordStatus getQuestionnaireManagementRecordStatusByName(String name);

    /**
     * Obtiene todos los estados de registro de gestión de cuestionarios.
     *
     * @return lista de {@link QuestionnaireManagementRecordStatus}
     */
    List<QuestionnaireManagementRecordStatus> getAllQuestionnaireManagementRecordStatuses();

}
