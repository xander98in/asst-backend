package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;

public interface PersonEvaluatedQueryCUInputPort {

    /**
     * Consulta una persona a partir de su identificador único.
     *
     * @param id el identificador de la persona
     * @return una instancia del modelo de dominio {@link PersonEvaluated}
     */
    PersonEvaluated getPersonEvaluatedById(Long id);

}
