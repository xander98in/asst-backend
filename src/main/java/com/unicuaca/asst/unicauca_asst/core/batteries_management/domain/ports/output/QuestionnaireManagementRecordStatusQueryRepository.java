package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;

/**
 * Puerto de salida para operaciones de consulta sobre los estados de los registros de gestión de cuestionarios.
 *
 * Define las firmas de los métodos que deben implementar los adaptadores de infraestructura encargados 
 * de recuperar información desde fuentes externas (p. ej., base de datos relacional).
 */
public interface QuestionnaireManagementRecordStatusQueryRepository {

    /**
     * Consulta un estado de registro de gestión de cuestionarios por su identificador único.
     *
     * @param id identificador del estado
     * @return un {@link Optional} con el estado encontrado o vacío si no existe
     */
    Optional<QuestionnaireManagementRecordStatus> getQuestionnaireManagementRecordStatusById(Long id);

    /**
     * Consulta un estado de registro de gestión de cuestionarios por su nombre.
     *
     * @param name nombre del estado
     * @return un {@link Optional} con el estado encontrado o vacío si no existe
     */
    Optional<QuestionnaireManagementRecordStatus> getQuestionnaireManagementRecordStatusByName(String name);

    /**
     * Verifica si existe un estado de registro de gestión de cuestionarios con el ID proporcionado.
     *
     * @param id identificador del estado
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsById(Long id);

    /**
     * Verifica si existe un estado de registro de gestión de cuestionarios con el nombre proporcionado.
     *
     * @param name nombre del estado
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsByName(String name);

    /**
     * Obtiene todos los estados de registros de gestión de cuestionarios.
     *
     * @return lista de estados (posiblemente vacía si no hay resultados)
     */
    List<QuestionnaireManagementRecordStatus> getAllQuestionnaireManagementRecordStatuses();

}
