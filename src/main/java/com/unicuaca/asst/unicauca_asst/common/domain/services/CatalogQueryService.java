package com.unicuaca.asst.unicauca_asst.common.domain.services;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.domain.models.CivilStatus;
import com.unicuaca.asst.unicauca_asst.common.domain.models.ContractType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.EducationLevel;
import com.unicuaca.asst.unicauca_asst.common.domain.models.Gender;
import com.unicuaca.asst.unicauca_asst.common.domain.models.HousingType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.JobPositionType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.SalaryType;
import com.unicuaca.asst.unicauca_asst.common.domain.models.SocioeconomicLevel;
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

    /**
     * Obtiene una lista de todos los estados civiles.
     *
     * @return una lista de objetos CivilStatus
     */
    @Override
    public List<CivilStatus> getCivilStatuses() {
        List<CivilStatus> list = catalogQueryRepository.getCivilStatuses();
        if (list == null || list.isEmpty()) {
            throw new CatalogEmptyException(ErrorCode.CATALOG_EMPTY,"El catálogo de estados civiles está vacío. Debe existir al menos un registro."
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los niveles educativos.
     *
     * @return una lista de objetos EducationLevel
     */
    @Override
    public List<EducationLevel> getEducationLevels() {
        List<EducationLevel> list = catalogQueryRepository.getEducationLevels();
        if (list == null || list.isEmpty()) {
            throw new CatalogEmptyException(ErrorCode.CATALOG_EMPTY,"El catálogo de niveles educativos está vacío. Debe existir al menos un registro."
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los tipos de vivienda.
     *
     * @return una lista de objetos HousingType
     */
    @Override
    public List<HousingType> getHousingTypes() {
        List<HousingType> list = catalogQueryRepository.getHousingTypes();
        if (list == null || list.isEmpty()) {
            throw new CatalogEmptyException(ErrorCode.CATALOG_EMPTY,"El catálogo de tipos de vivienda está vacío. Debe existir al menos un registro."
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los niveles socioeconómicos.
     *
     * @return una lista de objetos SocioeconomicLevel
     */
    @Override
    public List<SocioeconomicLevel> getSocioeconomicLevels() {
        List<SocioeconomicLevel> list = catalogQueryRepository.getSocioeconomicLevels();
        if (list == null || list.isEmpty()) {
            throw new CatalogEmptyException(ErrorCode.CATALOG_EMPTY,"El catálogo de niveles socioeconómicos está vacío. Debe existir al menos un registro."
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los tipos de cargo.
     *
     * @return una lista de objetos JobPositionType
     */
    @Override
    public List<JobPositionType> getJobPositionTypes() {
        List<JobPositionType> list = catalogQueryRepository.getJobPositionTypes();
        if (list == null || list.isEmpty()) {
            throw new CatalogEmptyException(ErrorCode.CATALOG_EMPTY,"El catálogo de tipos de cargo está vacío. Debe existir al menos un registro."
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los tipos de contrato.
     *
     * @return una lista de objetos ContractType
     */
    @Override
    public List<ContractType> getContractTypes() {
        List<ContractType> list = catalogQueryRepository.getContractTypes();
        if (list == null || list.isEmpty()) {
            throw new CatalogEmptyException(ErrorCode.CATALOG_EMPTY,"El catálogo de tipos de contrato está vacío. Debe existir al menos un registro."
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los tipos de salario.
     *
     * @return una lista de objetos SalaryType
     */
    @Override
    public List<SalaryType> getSalaryTypes() {
        List<SalaryType> list = catalogQueryRepository.getSalaryTypes();
        if (list == null || list.isEmpty()) {
            throw new CatalogEmptyException(ErrorCode.CATALOG_EMPTY,"El catálogo de tipos de salario está vacío. Debe existir al menos un registro."
            );
        }
        return list;
    }

    /**
     * Obtiene una lista de todos los géneros.
     *
     * @return una lista de objetos Gender
     */
    @Override
    public List<Gender> getGenders() {
        List<Gender> list = catalogQueryRepository.getGenders();
        if (list == null || list.isEmpty()) {
            throw new CatalogEmptyException(ErrorCode.CATALOG_EMPTY,"El catálogo de géneros está vacío. Debe existir al menos un registro."
            );
        }
        return list;
    }
}