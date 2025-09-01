package com.unicuaca.asst.unicauca_asst.common.domain.ports.output;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;

/**
 * Puerto de salida para las consultas de catálogos.
 *
 * Define las firmas de los métodos que deben implementar los adaptadores de infraestructura
 * encargados de recuperar la información desde fuentes externas, como bases de datos relacionales.
 */
public interface CatalogQueryRepository {

    /**
     * Obtiene una lista de todos los tipos de identificación.
     *
     * @return una lista de objetos IdentificationType
     */
    List<IdentificationType> getIdTypes();

}
