package com.unicuaca.asst.unicauca_asst.core.reports.application.query;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordInformationResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceSummaryResponseDTO;

/**
 * Handler de consultas para operaciones de lectura sobre espacios de análisis.
 */
public interface AnalysisSpaceQueryHandler {

    /**
     * Lista los espacios de análisis del usuario autenticado.
     *
     * @param userId ID del usuario autenticado
     * @return lista de resúmenes de espacios de análisis
     */
    List<AnalysisSpaceSummaryResponseDTO> getAnalysisSpacesByUser(Long userId);

    /**
     * Obtiene el detalle completo de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario autenticado
     * @return DTO con el espacio y sus baterías
     */
    AnalysisSpaceResponseDTO getAnalysisSpaceById(Long spaceId, Long userId);

    /**
     * Lista las baterías de un espacio de análisis con múltiples filtros opcionales,
     * validando previamente que el espacio pertenezca al usuario.
     *
     * @param spaceId              ID del espacio de análisis (obligatorio)
     * @param userId               ID del usuario autenticado
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
    Page<BatteryManagementRecordInformationResponseDTO> getSpaceBatteriesWithMultifilter(
        Long spaceId, Long userId,
        String identificationNumber, String workAreaName,
        LocalDateTime dateFrom, LocalDateTime dateTo,
        Long identificationTypeId, Long jobPositionTypeId,
        String intralaboralForm,
        Integer page, Integer size
    );
}
