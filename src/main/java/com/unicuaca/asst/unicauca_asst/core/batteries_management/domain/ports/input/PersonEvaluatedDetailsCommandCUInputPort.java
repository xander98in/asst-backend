package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;

/**
 * Puerto de entrada para los casos de uso relacionados con la creación o actualización de detalles
 * complementarios de una persona evaluada.
 *
 * <p>Esta interfaz define las operaciones de tipo "Command" del modelo {@link PersonEvaluatedDetails}, 
 * permitiendo que los adaptadores de entrada (como controladores REST o handlers de aplicación) interactúen 
 * con la lógica de negocio sin acoplarse directamente a su implementación.</p>
 *
 * <p>Forma parte de la capa de dominio dentro de la arquitectura hexagonal, sirviendo como punto de 
 * acceso a los casos de uso encargados de gestionar la información detallada asociada a 
 * una persona evaluada.</p>
 */
public interface PersonEvaluatedDetailsCommandCUInputPort {

    /**
     * Crea los detalles de una persona evaluada.
     *
     * <p>Este método representa el caso de uso de creación de una entidad {@link PersonEvaluatedDetails}. 
     * La implementación concreta debe encargarse de validar la información, aplicar reglas de negocio 
     * y delegar la persistencia en los puertos de salida correspondientes.</p>
     *
     * @param personEvaluatedDetails el objeto {@link PersonEvaluatedDetails} que contiene los datos a crear
     * @return la entidad {@link PersonEvaluatedDetails} creada, incluyendo su ID generado u otros datos resultantes del proceso
     */
    PersonEvaluatedDetails createPersonEvaluatedDetails(PersonEvaluatedDetails personEvaluatedDetails);

}
