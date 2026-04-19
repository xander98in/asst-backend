package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordInformation;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.BatteryManagementRecordQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.AnalysisSpaceQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la consulta de espacios de análisis.
 *
 * <p>Implementa la lógica para listar y obtener espacios de análisis,
 * validando que el usuario tenga acceso al espacio solicitado.</p>
 */
@RequiredArgsConstructor
public class AnalysisSpaceQueryService implements AnalysisSpaceQueryCUInputPort {

    private final AnalysisSpaceQueryRepository analysisSpaceQueryRepository;
    private final BatteryManagementRecordQueryCUInputPort batteryManagementRecordQueryCUInputPort;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Lista todos los espacios de análisis creados por un usuario.
     *
     * @param userId ID del usuario
     * @return lista de espacios de análisis del usuario
     */
    @Override
    public List<AnalysisSpace> getAnalysisSpacesByUser(Long userId) {
        return analysisSpaceQueryRepository.findAllByCreatorUserId(userId);
    }

    /**
     * Obtiene un espacio de análisis con sus baterías asociadas.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario que realiza la consulta
     * @return el espacio de análisis con sus baterías
     */
    @Override
    public AnalysisSpace getAnalysisSpaceById(Long spaceId, Long userId) {
        Optional<AnalysisSpace> optionalSpace = analysisSpaceQueryRepository.findByIdWithBatteries(spaceId);
        if (optionalSpace.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.ANALYSIS_SPACE_NOT_FOUND,
                "user.report.space_not_found",
                spaceId
            );
        }
        AnalysisSpace space = optionalSpace.get();

        // Validar que el espacio pertenece al usuario
        if (!space.getCreatorUserId().equals(userId)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.ANALYSIS_SPACE_ACCESS_DENIED,
                "user.report.space_access_denied",
                userId, spaceId
            );
        }

        return space;
    }

    /**
     * Lista las baterías de un espacio de análisis con múltiples filtros opcionales,
     * validando previamente que el espacio exista y pertenezca al usuario.
     *
     * <p>Reutiliza {@link #getAnalysisSpaceById(Long, Long)} para validar la existencia
     * del espacio y la pertenencia al usuario antes de delegar la consulta paginada al
     * módulo de baterías.</p>
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
    @Override
    public Page<BatteryManagementRecordInformation> getSpaceBatteriesWithMultifilter(
        Long spaceId, Long userId,
        String identificationNumber, String workAreaName,
        LocalDateTime dateFrom, LocalDateTime dateTo,
        Long identificationTypeId, Long jobPositionTypeId,
        String intralaboralForm,
        Integer page, Integer size
    ) {
        getAnalysisSpaceById(spaceId, userId);

        return batteryManagementRecordQueryCUInputPort.listByAnalysisSpaceWithMultipleFilters(
            spaceId,
            identificationNumber, workAreaName, dateFrom, dateTo,
            identificationTypeId, jobPositionTypeId, intralaboralForm,
            page, size
        );
    }
}
