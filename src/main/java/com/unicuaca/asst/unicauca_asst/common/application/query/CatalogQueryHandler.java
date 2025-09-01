package com.unicuaca.asst.unicauca_asst.common.application.query;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.CivilStatusResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.ContractTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.EducationLevelResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.GenderResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.HousingTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.IdentificationTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.JobPositionTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.SalaryTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.SocioeconomicLevelResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;

public interface CatalogQueryHandler {

    /**
     * Consulta los tipos de identificación disponibles en el sistema.
     *
     * @return una {@link ApiResponse} que contiene una lista de {@link IdentificationTypeResponseDTO} representando los tipos de identificación.
     */
    ApiResponse<List<IdentificationTypeResponseDTO>> getIdTypes();

    /**
     * Consulta los estados civiles disponibles en el sistema.
     *
     * @return una {@link ApiResponse} que contiene una lista de {@link CivilStatusResponseDTO} representando los estados civiles.
     */
    ApiResponse<List<CivilStatusResponseDTO>> getCivilStatuses();

    /**
     * Consulta los niveles educativos disponibles en el sistema.
     *
     * @return una {@link ApiResponse} que contiene una lista de {@link EducationLevelResponseDTO} representando los niveles educativos.
     */
    ApiResponse<List<EducationLevelResponseDTO>> getEducationLevels();

    /**
     * Consulta los tipos de vivienda disponibles en el sistema.
     *
     * @return una {@link ApiResponse} que contiene una lista de {@link HousingTypeResponseDTO} representando los tipos de vivienda.
     */
    ApiResponse<List<HousingTypeResponseDTO>> getHousingTypes();

    /**
     * Consulta los niveles socioeconómicos disponibles en el sistema.
     *
     * @return una {@link ApiResponse} que contiene una lista de {@link SocioeconomicLevelResponseDTO} representando los niveles socioeconómicos.
     */
    ApiResponse<List<SocioeconomicLevelResponseDTO>> getSocioeconomicLevels();

    /**
     * Consulta los tipos de cargo disponibles en el sistema.
     *
     * @return una {@link ApiResponse} que contiene una lista de {@link JobPositionTypeResponseDTO} representando los tipos de cargo.
     */
    ApiResponse<List<JobPositionTypeResponseDTO>> getJobPositionTypes();

    /**
     * Consulta los tipos de contrato disponibles en el sistema.
     *
     * @return una {@link ApiResponse} que contiene una lista de {@link ContractTypeResponseDTO} representando los tipos de contrato.
     */
    ApiResponse<List<ContractTypeResponseDTO>> getContractTypes();

    /**
     * Consulta los tipos de salario disponibles en el sistema.
     *
     * @return una {@link ApiResponse} que contiene una lista de {@link SalaryTypeResponseDTO} representando los tipos de salario.
     */
    ApiResponse<List<SalaryTypeResponseDTO>> getSalaryTypes();

    /**
     * Consulta los géneros disponibles en el sistema.
     *
     * @return una {@link ApiResponse} que contiene una lista de {@link GenderResponseDTO} representando los géneros.
     */
    ApiResponse<List<GenderResponseDTO>> getGenders();
}
