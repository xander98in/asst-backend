package com.unicuaca.asst.unicauca_asst.common.domain.ports.output;

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

    /**
     * Obtiene una lista de todos los estados civiles.
     *
     * @return una lista de objetos CivilStatus
     */
    List<CivilStatus> getCivilStatuses();

    /**
     * Obtiene una lista de todos los niveles educativos.
     *
     * @return una lista de objetos EducationLevel
     */
    List<EducationLevel> getEducationLevels();

    /**
     * Obtiene una lista de todos los tipos de vivienda.
     *
     * @return una lista de objetos HousingType
     */
    List<HousingType> getHousingTypes();

    /**
     * Obtiene una lista de todos los niveles socioeconómicos.
     *
     * @return una lista de objetos SocioeconomicLevel
     */
    List<SocioeconomicLevel> getSocioeconomicLevels();

    /**
     * Obtiene una lista de todos los tipos de cargo.
     *
     * @return una lista de objetos JobPositionType
     */
    List<JobPositionType> getJobPositionTypes();

    /**
     * Obtiene una lista de todos los tipos de contrato.
     *
     * @return una lista de objetos ContractType
     */
    List<ContractType> getContractTypes();

    /**
     * Obtiene una lista de todos los tipos de salario.
     *
     * @return una lista de objetos SalaryType
     */
    List<SalaryType> getSalaryTypes();

    /**
     * Obtiene una lista de todos los géneros.
     *
     * @return una lista de objetos Gender
     */
    List<Gender> getGenders();
}
