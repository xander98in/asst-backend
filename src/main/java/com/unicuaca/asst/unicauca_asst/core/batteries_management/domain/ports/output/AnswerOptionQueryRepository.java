package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.AnswerOption;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para operaciones de consulta sobre las opciones de respuesta.
 *
 * Define el contrato que debe cumplir la infraestructura para
 * recuperar información del catálogo de respuestas.
 */
public interface AnswerOptionQueryRepository {

    /**
     * Obtiene una opción de respuesta por su identificador único.
     *
     * @param id identificador de la opción.
     * @return un {@link Optional} con el modelo de dominio o vacío si no existe.
     */
    Optional<AnswerOption> getAnswerOptionById(Long id);

    /**
     * Verifica la existencia de una opción de respuesta por su ID.
     *
     * @param id identificador de la opción.
     * @return true si existe, false si no.
     */
    boolean existsAnswerOptionById(Long id);

    /**
     * Obtiene una opción de respuesta por su número de orden.
     *
     * @param order número de orden visual.
     * @return un {@link Optional} con el modelo de dominio o vacío si no existe.
     */
    Optional<AnswerOption> getAnswerOptionByOrder(Integer order);

    /**
     * Verifica la existencia de una opción de respuesta por su número de orden.
     *
     * @param order número de orden visual.
     * @return true si existe, false si no.
     */
    boolean existsAnswerOptionByOrder(Integer order);

    /**
     * Obtiene todas las opciones de respuesta disponibles en el sistema.
     *
     * @return lista de opciones de respuesta.
     */
    List<AnswerOption> getAllAnswerOptions();

    /**
     * Obtiene una opción de respuesta por su valor numérico.
     *
     * @param value valor numérico de la opción (ej. 5, 3, 1).
     * @return un {@link Optional} con el modelo de dominio o vacío si no existe.
     */
    Optional<AnswerOption> getAnswerOptionByValue(Integer value);
}
