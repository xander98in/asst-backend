package com.unicuaca.asst.unicauca_asst.common.application.query;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.CivilStatusResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.ContractTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.EducationLevelResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.GenderResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.HousingTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.IdentificationTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.JobPositionTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.SalaryTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.SocioeconomicLevelResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.mappers.CatalogMapper;
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
        List<IdentificationTypeResponseDTO> responseDTOs = catalogQueryCUInputPort.getIdTypes().stream()
            .map(catalogMapper::toIdentificationTypeResponseDTO)
            .collect(Collectors.toList());
        return ApiResponse.success("Tipos de identificación obtenidos exitosamente", responseDTOs);
    }

    /**
     * Obtiene una lista de todos los estados civiles.
     *
     * @return una lista de objetos CivilStatusResponseDTO
     */
    @Override
    public ApiResponse<List<CivilStatusResponseDTO>> getCivilStatuses() {
        List<CivilStatusResponseDTO> responseDTOs = catalogQueryCUInputPort.getCivilStatuses().stream()
            .map(catalogMapper::toCivilStatusResponseDTO)
            .collect(Collectors.toList());
        return ApiResponse.success("Estados civiles obtenidos exitosamente", responseDTOs);
    }

    /**
     * Obtiene una lista de todos los niveles educativos.
     *
     * @return una lista de objetos EducationLevelResponseDTO
     */
    @Override
    public ApiResponse<List<EducationLevelResponseDTO>> getEducationLevels() {
        List<EducationLevelResponseDTO> responseDTOs = catalogQueryCUInputPort.getEducationLevels().stream()
            .map(catalogMapper::toEducationLevelResponseDTO)
            .collect(Collectors.toList());
        return ApiResponse.success("Niveles educativos obtenidos exitosamente", responseDTOs);
    }

    /**
     * Obtiene una lista de todos los tipos de vivienda.
     *
     * @return una lista de objetos HousingTypeResponseDTO
     */
    @Override
    public ApiResponse<List<HousingTypeResponseDTO>> getHousingTypes() {
        List<HousingTypeResponseDTO> responseDTOs = catalogQueryCUInputPort.getHousingTypes().stream()
            .map(catalogMapper::toHousingTypeResponseDTO)
            .collect(Collectors.toList());
        return ApiResponse.success("Tipos de vivienda obtenidos exitosamente", responseDTOs);
    }

    /**
     * Obtiene una lista de todos los niveles socioeconómicos.
     *
     * @return una lista de objetos SocioeconomicLevelResponseDTO
     */
    @Override
    public ApiResponse<List<SocioeconomicLevelResponseDTO>> getSocioeconomicLevels() {
        List<SocioeconomicLevelResponseDTO> responseDTOs = catalogQueryCUInputPort.getSocioeconomicLevels().stream()
            .map(catalogMapper::toSocioeconomicLevelResponseDTO)
            .collect(Collectors.toList());
        return ApiResponse.success("Niveles socioeconómicos obtenidos exitosamente", responseDTOs);
    }

    /**
     * Obtiene una lista de todos los tipos de cargo.
     *
     * @return una lista de objetos JobPositionTypeResponseDTO
     */
    @Override
    public ApiResponse<List<JobPositionTypeResponseDTO>> getJobPositionTypes() {
        List<JobPositionTypeResponseDTO> responseDTOs = catalogQueryCUInputPort.getJobPositionTypes().stream()
            .map(catalogMapper::toJobPositionTypeResponseDTO)
            .collect(Collectors.toList());
        return ApiResponse.success("Tipos de cargo obtenidos exitosamente", responseDTOs);
    }

    /**
     * Obtiene una lista de todos los tipos de contrato.
     *
     * @return una lista de objetos ContractTypeResponseDTO
     */
    @Override
    public ApiResponse<List<ContractTypeResponseDTO>> getContractTypes() {
        List<ContractTypeResponseDTO> responseDTOs = catalogQueryCUInputPort.getContractTypes().stream()
            .map(catalogMapper::toContractTypeResponseDTO)
            .collect(Collectors.toList());
        return ApiResponse.success("Tipos de contrato obtenidos exitosamente", responseDTOs);
    }

    /**
     * Obtiene una lista de todos los tipos de salario.
     *
     * @return una lista de objetos SalaryTypeResponseDTO
     */
    @Override
    public ApiResponse<List<SalaryTypeResponseDTO>> getSalaryTypes() {
        List<SalaryTypeResponseDTO> responseDTOs = catalogQueryCUInputPort.getSalaryTypes().stream()
            .map(catalogMapper::toSalaryTypeResponseDTO)
            .collect(Collectors.toList());
        return ApiResponse.success("Tipos de salario obtenidos exitosamente", responseDTOs);
    }

    /**
     * Obtiene una lista de todos los géneros.
     *
     * @return una lista de objetos GenderResponseDTO
     */
    @Override
    public ApiResponse<List<GenderResponseDTO>> getGenders() {
        List<GenderResponseDTO> responseDTOs = catalogQueryCUInputPort.getGenders().stream()
            .map(catalogMapper::toGenderResponseDTO)
            .collect(Collectors.toList());
        return ApiResponse.success("Géneros obtenidos exitosamente", responseDTOs);
    }
}
