package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordStatusResponseDTO;

/**
 * Manejador de consultas (Application Layer) para los estados de los registros de gestión de cuestionarios.
 */
public interface QuestionnaireManagementRecordStatusHandler {

    /**
     * Lista todos los estados de registro de gestión de cuestionarios.
     *
     * @return lista de {@link QuestionnaireManagementRecordStatusResponseDTO}.
     */
    List<QuestionnaireManagementRecordStatusResponseDTO> getAllStatuses();

    /**
     * Obtiene un estado de registro de gestión de cuestionarios por su identificador.
     *
     * @param id identificador del estado.
     * @return {@link QuestionnaireManagementRecordStatusResponseDTO} correspondiente.
     */
    QuestionnaireManagementRecordStatusResponseDTO getById(Long id);

    /**
     * Obtiene un estado de registro de gestión de cuestionarios por su nombre.
     *
     * @param name nombre del estado (por ejemplo, "Creado", "En diligenciamiento").
     * @return {@link QuestionnaireManagementRecordStatusResponseDTO} correspondiente.
     */
    QuestionnaireManagementRecordStatusResponseDTO getByName(String name);

}
