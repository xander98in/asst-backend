package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;

/**
 * Puerto de salida que define las operaciones de comando (creación o modificación)
 * relacionadas con el agregado {@link PersonEvaluated}.
 *
 * Esta interfaz representa el contrato que debe implementar la infraestructura
 * para permitir la persistencia de datos de personas en una fuente externa (por ejemplo, base de datos).
 *
 * <p>Hace parte del patrón de arquitectura hexagonal, permitiendo que el dominio
 * se mantenga desacoplado de los detalles técnicos de almacenamiento.</p>
 */
public interface PersonEvaluatedCommandRepository {

    /**
     * Persiste una nueva persona o actualiza una existente.
     *
     * @param personEvaluated objeto de dominio que representa a la persona a guardar
     * @return la persona persistida con sus datos actualizados (por ejemplo, con ID generado)
     */
    Optional<PersonEvaluated> savePersonEvaluated(PersonEvaluated personEvaluated);

    /**
     * Puerto de salida para actualizar una persona evaluada.
     * 
     * @param personEvaluated objeto con los datos a persistir
     * @return objeto actualizado o vacío si no se encontró
     */
    Optional<PersonEvaluated> updatePersonEvaluated(PersonEvaluated personEvaluated);

    /**
     * Elimina una persona evaluada por su ID.
     *
     * @param id identificador de la persona a eliminar
     */
    void deletePersonEvaluatedById(Long id);
}
