package com.unicuaca.asst.unicauca_asst.common.domain.ports.input;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;

public interface CatalogQueryCUInputPort {

    /**
     * Obtiene una lista de todos los tipos de identificación.
     *
     * @return una lista de objetos IdentificationType
     */
    List<IdentificationType> getIdTypes();

}
