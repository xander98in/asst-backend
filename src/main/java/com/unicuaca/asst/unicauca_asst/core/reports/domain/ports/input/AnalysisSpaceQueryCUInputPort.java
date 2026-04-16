package com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordInformation;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;

/**
 * Puerto de entrada para operaciones de lectura sobre espacios de análisis.
 */
public interface AnalysisSpaceQueryCUInputPort {

    /**
     * Lista todos los espacios de análisis creados por un usuario.
     *
     * @param userId ID del usuario
     * @return lista de espacios de análisis del usuario
     */
    List<AnalysisSpace> getAnalysisSpacesByUser(Long userId);

    /**
     * Obtiene un espacio de análisis con sus baterías asociadas.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario que realiza la consulta
     * @return el espacio de análisis con sus baterías
     */
    AnalysisSpace getAnalysisSpaceById(Long spaceId, Long userId);

    /**
     * Lista las baterías de un espacio de análisis con múltiples filtros opcionales,
     * validando previamente que el espacio exista y pertenezca al usuario.
     *
     * @param spaceId              ID del espacio de análisis (obligatorio)
     * @param userId               ID del usuario autenticado (para validar ownership)
     * @param identificationNumber número de identificación (prefijo, puede ser null)
     * @param workAreaName         área de trabajo (contenido parcial, puede ser null)
     * @param dateFrom             fecha inicial del rango (puede ser null)
     * @param dateTo               fecha final del rango (puede ser null)
     * @param identificationTypeId ID del tipo de identificación (puede ser null)
     * @param jobPositionTypeId    ID del tipo de cargo (puede ser null)
     * @param intralaboralForm     forma intralaboral: "A" (cargos 1-2) o "B" (cargos 3-4), puede ser null
     * @param page                 número de página
     * @param size                 tamaño de página
     * @return página de baterías del espacio filtradas
     */
    Page<BatteryManagementRecordInformation> getSpaceBatteriesWithMultifilter(
        Long spaceId, Long userId,
        String identificationNumber, String workAreaName,
        LocalDateTime dateFrom, LocalDateTime dateTo,
        Long identificationTypeId, Long jobPositionTypeId,
        String intralaboralForm,
        Integer page, Integer size
    );
}
