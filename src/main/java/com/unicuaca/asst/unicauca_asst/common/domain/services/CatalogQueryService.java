package com.unicuaca.asst.unicauca_asst.common.domain.services;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.input.CatalogQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.exceptions.CatalogEmptyException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio de consulta de catálogos.
 *
 * Se encarga de manejar las solicitudes de consulta relacionadas con catálogos
 */
@RequiredArgsConstructor
public class CatalogQueryService implements CatalogQueryCUInputPort {

    private final CatalogQueryRepository catalogQueryRepository;

    /**
     * Obtiene una lista de todos los tipos de identificación.
     *
     * @return una lista de objetos IdentificationType
     */
    @Override
    public List<IdentificationType> getIdTypes() {
        List<IdentificationType> list = catalogQueryRepository.getIdTypes();
        if (list == null || list.isEmpty()) {
            throw new CatalogEmptyException(ErrorCode.CATALOG_EMPTY,"El catálogo de tipos de identificación está vacío. Debe existir al menos un registro."
            );
        }
        return list;
    }

}
