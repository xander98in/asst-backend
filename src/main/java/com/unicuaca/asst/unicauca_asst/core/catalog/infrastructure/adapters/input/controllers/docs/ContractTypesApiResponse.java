package com.unicuaca.asst.unicauca_asst.core.catalog.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.core.catalog.application.dto.response.ContractTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Wrapper solo para documentación OpenAPI del genérico
 * {@code ApiResponse<List<ContractTypeResponseDTO>>}.
 *
 * <p>Permite que Swagger resuelva el tipo genérico y muestre correctamente el esquema
 * de respuesta que contiene la lista de tipos de contrato.</p>
 */
@Schema(name = "ContractTypesApiResponse", description = "Respuesta API que contiene una lista de tipos de contrato")
public class ContractTypesApiResponse extends ApiResponse<List<ContractTypeResponseDTO>> {
}
