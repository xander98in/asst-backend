package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;

public interface PersonQueryRepository {

    /**
     * Consulta una persona a partir de su identificador único.
     *
     * @param id el identificador de la persona
     * @return una instancia del modelo de dominio {@link Person}
     */
    Person getById(Long id);

}
