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
     * @return una lista de {@link IdentificationTypeResponseDTO} representando los tipos de identificación.
     */
    List<IdentificationTypeResponseDTO> getIdTypes();

    /**
     * Consulta los estados civiles disponibles en el sistema.
     *
     * @return una lista de {@link CivilStatusResponseDTO} representando los estados civiles.
     */
    List<CivilStatusResponseDTO> getCivilStatuses();

    /**
     * Consulta los niveles de educación disponibles en el sistema.
     *
     * @return una lista de {@link EducationLevelResponseDTO} representando los niveles de educación.
     */
    List<EducationLevelResponseDTO> getEducationLevels();

    /**
     * Consulta los tipos de vivienda disponibles en el sistema.
     *
     * @return una lista de {@link HousingTypeResponseDTO} representando los tipos de vivienda.
     */
    List<HousingTypeResponseDTO> getHousingTypes();

    /**
     * Consulta los niveles socioeconómicos disponibles en el sistema.
     *
     * @return una lista de {@link SocioeconomicLevelResponseDTO} representando los niveles socioeconómicos.
     */
    List<SocioeconomicLevelResponseDTO> getSocioeconomicLevels();

    /**
     * Consulta los tipos de cargo disponibles en el sistema.
     *
     * @return una lista de {@link JobPositionTypeResponseDTO} representando los tipos de cargo.
     */
    List<JobPositionTypeResponseDTO> getJobPositionTypes();

    /**
     * Consulta los tipos de contrato disponibles en el sistema.
     *
     * @return una lista de {@link ContractTypeResponseDTO} representando los tipos de contrato.
     */
    List<ContractTypeResponseDTO> getContractTypes();

    /**
     * Consulta los tipos de salario disponibles en el sistema.
     *
     * @return una lista de {@link SalaryTypeResponseDTO} representando los tipos de salario.
     */
    List<SalaryTypeResponseDTO> getSalaryTypes();

    /**
     * Consulta los géneros disponibles en el sistema.
     *
     * @return una lista de {@link GenderResponseDTO} representando los géneros.
     */
    List<GenderResponseDTO> getGenders();
}
