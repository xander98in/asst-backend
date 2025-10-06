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
    public List<IdentificationTypeResponseDTO> getIdTypes() {
         return catalogQueryCUInputPort.getIdTypes().stream()
            .map(catalogMapper::toIdentificationTypeResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los estados civiles.
     *
     * @return una lista de objetos CivilStatusResponseDTO
     */
    @Override
    public List<CivilStatusResponseDTO> getCivilStatuses() {
        return catalogQueryCUInputPort.getCivilStatuses().stream()
            .map(catalogMapper::toCivilStatusResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los niveles educativos.
     *
     * @return una lista de objetos EducationLevelResponseDTO
     */
    @Override
    public List<EducationLevelResponseDTO> getEducationLevels() {
        return catalogQueryCUInputPort.getEducationLevels().stream()
            .map(catalogMapper::toEducationLevelResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los tipos de vivienda.
     *
     * @return una lista de objetos HousingTypeResponseDTO
     */
    @Override
    public List<HousingTypeResponseDTO> getHousingTypes() {
        return catalogQueryCUInputPort.getHousingTypes().stream()
            .map(catalogMapper::toHousingTypeResponseDTO)
            .collect(Collectors.toList());

    }

    /**
     * Obtiene una lista de todos los niveles socioeconómicos.
     *
     * @return una lista de objetos SocioeconomicLevelResponseDTO
     */
    @Override
    public List<SocioeconomicLevelResponseDTO> getSocioeconomicLevels() {
        return catalogQueryCUInputPort.getSocioeconomicLevels().stream()
            .map(catalogMapper::toSocioeconomicLevelResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los tipos de cargo.
     *
     * @return una lista de objetos JobPositionTypeResponseDTO
     */
    @Override
    public List<JobPositionTypeResponseDTO> getJobPositionTypes() {
        return catalogQueryCUInputPort.getJobPositionTypes().stream()
            .map(catalogMapper::toJobPositionTypeResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los tipos de contrato.
     *
     * @return una lista de objetos ContractTypeResponseDTO
     */
    @Override
    public List<ContractTypeResponseDTO> getContractTypes() {
        return catalogQueryCUInputPort.getContractTypes().stream()
            .map(catalogMapper::toContractTypeResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los tipos de salario.
     *
     * @return una lista de objetos SalaryTypeResponseDTO
     */
    @Override
    public List<SalaryTypeResponseDTO> getSalaryTypes() {
        return catalogQueryCUInputPort.getSalaryTypes().stream()
            .map(catalogMapper::toSalaryTypeResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los géneros.
     *
     * @return una lista de objetos GenderResponseDTO
     */
    @Override
    public List<GenderResponseDTO> getGenders() {
        return catalogQueryCUInputPort.getGenders().stream()
            .map(catalogMapper::toGenderResponseDTO)
            .collect(Collectors.toList());

    }
}
