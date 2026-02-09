package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;

/**
 * Puerto de salida para operaciones de persistencia relacionadas con los detalles de una persona evaluada (PersonEvaluatedDetails).
 *
 * <p>Esta interfaz define los métodos necesarios para que los adaptadores de salida
 * (como repositorios o servicios externos) implementen la lógica de almacenamiento
 * y recuperación de datos relacionados con los detalles de una persona evaluada.</p>
 *
 * <p>Forma parte de la capa de infraestructura dentro de la arquitectura hexagonal,
 * permitiendo que la lógica de negocio interactúe con mecanismos de persistencia
 * sin acoplarse directamente a su implementación.</p>
 */
public interface PersonEvaluatedDetailsCommandRepository {

    /** 
     * Guarda los detalles de una persona evaluada.
     *
     * @param personEvaluatedDetails Los detalles de la persona evaluada a guardar.
     * @return Un Optional que contiene los detalles guardados de la persona evaluada, o vacío si no se pudo guardar.
     */
    Optional<PersonEvaluatedDetails> savePersonEvaluatedDetails(PersonEvaluatedDetails personEvaluatedDetails);

    /**
     * Actualiza los detalles de una persona evaluada.
     *
     * @param id ID de los detalles de la persona evaluada a actualizar.
     * @param personEvaluatedDetails Los nuevos detalles de la persona evaluada.
     * @return Un Optional que contiene los detalles actualizados de la persona evaluada, o vacío si no se pudo actualizar.
     */
    Optional<PersonEvaluatedDetails> updatePersonEvaluatedDetails(Long id, PersonEvaluatedDetails personEvaluatedDetails);

    /**
     * Elimina los detalles de una persona evaluada por su ID.
     *
     * @param id ID del detalle a eliminar
     */
    void deleteById(Long id);

}
