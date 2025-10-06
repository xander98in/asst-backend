package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.StatusPersonEvaluated;

/**
 * Puerto de salida para operaciones de consulta sobre el agregado {@link PersonEvaluated}.
 * 
 * Define las firmas de los métodos que deben implementar los adaptadores de infraestructura encargados
 * de recuperar información desde fuentes externas, como bases de datos relacionales.
 * 
 * <p>Hace parte de la arquitectura hexagonal, separando las dependencias externas
 * de la lógica del dominio.</p>
 */
public interface    PersonEvaluatedQueryRepository {

    /**
     * Consulta una persona evaluada a partir de su identificador único.
     *
     * @param id identificador de la persona evaluada      
     * @return un {@link Optional} con la persona evaluada encontrada o vacío si no existe
     */
    Optional<PersonEvaluated> getPersonEvaluatedById(Long id);

    /**
     * Verifica si existe una persona evaluada con el ID proporcionado.
     *
     * @param id identificador de la persona evaluada
     * @return true si la persona evaluada existe, false si no
     */
    boolean existsById(Long id);

    /**
     * Verifica si existe una persona evaluada con un tipo y número de identificación específicos.
     *
     * @param identificationTypeId ID del tipo de identificación (por ejemplo: cédula, pasaporte)
     * @param identificationNumber número de identificación de la persona evaluada
     * @return true si la persona evaluada existe, false si no
     */
    boolean existsByIdentification(Long identificationTypeId, String identificationNumber);

     /**
     * Verifica si existe una persona evaluada con el correo electrónico especificado.
     *
     * @param email dirección de correo electrónico a verificar
     * @return {@code true} si ya hay una persona registrada con ese correo, {@code false} si no
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si el correo ya está asignado a una persona distinta del ID dado.
     * 
     * @param email correo a verificar
     * @param id ID de la persona actual
     * @return true si el correo pertenece a otra persona, false en caso contrario
     */
    boolean isEmailAssignedToDifferentPerson(String email, Long id);

    /**
     * Consulta una lista paginada de personas evaluadas por su identidad.
     *
     * @param identificationType  el tipo de identificación
     * @param page                el número de página
     * @param size                el tamaño de la página
     * @return una página de personas evaluadas
     */
    Page<PersonEvaluated> queryByIdentity(IdentificationType identificationType, String identificationNumber, Integer page, Integer size, Sort sort);

    /**
     * Consulta un estado de persona evaluada por su nombre.
     *
     * @param name el nombre del estado a buscar
     * @return un Optional que contiene el estado encontrado, o vacío si no se encuentra
     */
    Optional<StatusPersonEvaluated> getStatusPersonEvaluatedByName(String name);

    /**
     * Verifica si existe una persona evaluada con el mismo tipo y número de identificación, excluyendo un ID específico.
     * 
     * @param identificationTypeId ID del tipo de identificación
     * @param identificationNumber número de identificación
     * @param id ID de la persona a excluir en la verificación
     * @return true si existe otra persona con esos datos, false en caso contrario
     */
    boolean isIdentificationAssignedToDifferentPerson(Long identificationTypeId, String identificationNumber, Long id);
}
