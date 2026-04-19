package com.unicuaca.asst.unicauca_asst.core.reports.application.query;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordInformationResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.BatteryManagementRecordMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordInformation;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceSummaryResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.mappers.AnalysisSpaceMapper;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.AnalysisSpaceQueryCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del handler de consultas para espacios de análisis.
 *
 * <p>Delega al puerto de entrada de dominio y transforma los resultados
 * a DTOs de respuesta.</p>
 */
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class AnalysisSpaceQueryHandlerImpl implements AnalysisSpaceQueryHandler {

    private final AnalysisSpaceQueryCUInputPort analysisSpaceQueryCUInputPort;
    private final AnalysisSpaceMapper analysisSpaceMapper;
    private final BatteryManagementRecordMapper batteryManagementRecordMapper;

    /**
     * Lista los espacios de análisis del usuario autenticado.
     *
     * @param userId ID del usuario autenticado
     * @return lista de resúmenes de espacios de análisis
     */
    @Override
    public List<AnalysisSpaceSummaryResponseDTO> getAnalysisSpacesByUser(Long userId) {
        List<AnalysisSpace> spaces = analysisSpaceQueryCUInputPort.getAnalysisSpacesByUser(userId);
        return spaces.stream()
            .map(analysisSpaceMapper::toSummaryDTO)
            .toList();
    }

    /**
     * Obtiene el detalle completo de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario autenticado
     * @return DTO con el espacio y sus baterías
     */
    @Override
    public AnalysisSpaceResponseDTO getAnalysisSpaceById(Long spaceId, Long userId) {
        AnalysisSpace space = analysisSpaceQueryCUInputPort.getAnalysisSpaceById(spaceId, userId);
        return analysisSpaceMapper.toResponseDTO(space);
    }

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
    @Override
    public Page<BatteryManagementRecordInformationResponseDTO> getSpaceBatteriesWithMultifilter(
        Long spaceId, Long userId,
        String identificationNumber, String workAreaName,
        LocalDateTime dateFrom, LocalDateTime dateTo,
        Long identificationTypeId, Long jobPositionTypeId,
        String intralaboralForm,
        Integer page, Integer size
    ) {
        Page<BatteryManagementRecordInformation> infoPage =
            analysisSpaceQueryCUInputPort.getSpaceBatteriesWithMultifilter(
                spaceId, userId, identificationNumber, workAreaName, dateFrom, dateTo,
                identificationTypeId, jobPositionTypeId, intralaboralForm, page, size
            );
        List<BatteryManagementRecordInformationResponseDTO> dtoList = infoPage.getContent().stream()
            .map(batteryManagementRecordMapper::toInformationResponseDTO)
            .toList();
        return new PageImpl<>(dtoList, infoPage.getPageable(), infoPage.getTotalElements());
    }
}
