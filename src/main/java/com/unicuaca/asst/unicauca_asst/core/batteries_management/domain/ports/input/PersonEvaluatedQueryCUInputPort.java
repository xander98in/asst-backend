package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;

/**
 * Puerto de entrada para operaciones de consulta sobre el agregado {@link PersonEvaluated}.
 *
 * Define las firmas de los métodos que deben implementar los casos de uso encargados
 * de recuperar información de personas evaluadas desde el dominio.
 *
 * <p>Hace parte de la arquitectura hexagonal, separando las dependencias externas
 * de la lógica del dominio.</p>
 */
public interface PersonEvaluatedQueryCUInputPort {

    /**
     * Consulta una persona a partir de su identificador único.
     *
     * @param id el identificador de la persona
     * @return una instancia del modelo de dominio {@link PersonEvaluated}
     */
    PersonEvaluated getPersonEvaluatedById(Long id);

    /**
     * Consulta una lista paginada de personas evaluadas por su identidad.
     *
     * @param abbreviation       la abreviatura del tipo de identificación
     * @param identificationNumber el número de identificación
     * @param page               el número de página
     * @param size               el tamaño de la página
     * @return una página de información de personas evaluadas
     */
    Page<PersonEvaluated> queryByIdentity(String abbreviation, String identificationNumber, Integer page, Integer size);

    /**
     * Lista todas las personas evaluadas de forma paginada.
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @return una página de personas evaluadas
     */
    Page<PersonEvaluated> listPaginatedPersons(Integer page, Integer size);

    /**
     * Lista personas evaluadas de forma paginada filtrando por término de búsqueda.
     *
     * @param page       número de página (0-indexado)
     * @param size       cantidad de registros por página
     * @param searchTerm término de búsqueda (identificación, nombre o apellido)
     * @return una página de personas evaluadas
     */
    Page<PersonEvaluated> listPaginatedWithSearchTerm(Integer page, Integer size, String searchTerm);

}
