package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;

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

}
