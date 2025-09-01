package com.unicuaca.asst.unicauca_asst.common.application.query;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.IdentificationTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.mappers.CatalogMapper;
import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.input.CatalogQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del manejador de consultas de catálogos.
 *
 * Delega la lógica de negocio al puerto de entrada y transforma
 * el modelo de dominio en un DTO para la respuesta.
 */
@RequiredArgsConstructor
@Component
public class CatalogQueryHandlerImpl implements CatalogQueryHandler {

    private final CatalogQueryCUInputPort catalogQueryCUInputPort;
    private final CatalogMapper catalogMapper;

    /**
     * Obtiene una lista de todos los tipos de identificación.
     *
     * @return una lista de objetos IdentificationTypeResponseDTO
     */
    @Override
    public ApiResponse<List<IdentificationTypeResponseDTO>> getIdTypes() {
        List<IdentificationType> idTypes = catalogQueryCUInputPort.getIdTypes();
        List<IdentificationTypeResponseDTO> responseDTOs = idTypes.stream()
            .map(catalogMapper::toIdentificationTypeResponseDTO)
            .collect(Collectors.toList());
        return ApiResponse.success("Tipos de identificación obtenidos exitosamente", responseDTOs);
    }

}
