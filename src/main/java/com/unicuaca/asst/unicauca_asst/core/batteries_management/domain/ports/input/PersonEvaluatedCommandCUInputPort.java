package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;

/**
 * Puerto de entrada para los casos de uso relacionados con la creación o actualización de una persona.
 *
 * <p>Esta interfaz define las operaciones de tipo "Command" (crear o modificar estado) del modelo
 * {@link PersonEvaluated}, permitiendo que los adaptadores de entrada (como controladores REST,
 * interfaces gráficas, etc.) interactúen con la lógica de negocio sin acoplarse directamente a su
 * implementación.</p>
 *
 * <p>Forma parte de la capa de aplicación dentro de la arquitectura hexagonal.</p>
 */
public interface PersonEvaluatedCommandCUInputPort {

    /**
     * Crea una nueva persona evaluada en el sistema.
     *
     * <p>Este método representa el caso de uso de creación de una entidad {@link PersonEvaluated}.
     * La implementación concreta debe encargarse de validar la información, aplicar reglas de negocio
     * y delegar la persistencia en los puertos de salida.</p>
     *
     * @param person el objeto {@link PersonEvaluated} que contiene los datos de la persona evaluada a crear
     * @return la entidad {@link PersonEvaluated} creada, incluyendo su ID generado u otros datos resultantes del proceso
     */
    PersonEvaluated createPersonEvaluated(PersonEvaluated person);

    /**
     * Actualiza parcialmente los datos de una persona evaluada.
     *
     * <p>Este método representa el caso de uso de actualización de una entidad {@link PersonEvaluated}.
     * La implementación concreta debe encargarse de validar la información, aplicar reglas de negocio
     * y delegar la persistencia en los puertos de salida.</p>
     *
     * @param personEvaluated objeto de dominio con datos modificables
     * @return la persona actualizada
     */
    PersonEvaluated updatePersonEvaluated(PersonEvaluated personEvaluated);

    /**
     * Elimina una persona evaluada del sistema por su identificador.
     *
     * @param id identificador de la persona a eliminar
     */
    void deletePersonEvaluated(Long id);
}
