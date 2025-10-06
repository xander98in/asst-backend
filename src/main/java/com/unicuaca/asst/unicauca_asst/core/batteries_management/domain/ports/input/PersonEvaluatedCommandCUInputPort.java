package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;

/**
 * Puerto de entrada para los casos de uso relacionados con la creación o actualización de una persona.
 * 
 * Esta interfaz define las operaciones de tipo "Command" (crear o modificar estado) del modelo {@link PersonEvaluated},
 * permitiendo que los adaptadores de entrada (como controladores REST, interfaces gráficas, etc.) interactúen 
 * con la lógica de negocio sin acoplarse directamente a su implementación.
 * 
 * Forma parte de la capa de aplicación dentro de la arquitectura hexagonal.
 */
public interface PersonEvaluatedCommandCUInputPort {

    /**
     * Crea una nueva persona evaluada en el sistema.
     * 
     * Este método representa el caso de uso de creación de una entidad {@link PersonEvaluated}.
     * La implementación concreta debe encargarse de validar la información, aplicar reglas de negocio 
     * y delegar la persistencia en los puertos de salida.
     *
     * @param person el objeto {@link PersonEvaluated} que contiene los datos de la persona evaluada a crear
     * @return la entidad {@link PersonEvaluated} creada, incluyendo su ID generado u otros datos resultantes del proceso
     */
    PersonEvaluated createPersonEvaluated(PersonEvaluated person);  

    /**
     * Actualiza parcialmente los datos de una persona evaluada.
     *
     * Este método representa el caso de uso de actualización de una entidad {@link PersonEvaluated}.
     * La implementación concreta debe encargarse de validar la información, aplicar reglas de negocio
     * y delegar la persistencia en los puertos de salida.
     *
     * @param personEvaluated objeto de dominio con datos modificables
     * @return la persona actualizada
     */
    PersonEvaluated updatePersonEvaluated(PersonEvaluated personEvaluated);
}
