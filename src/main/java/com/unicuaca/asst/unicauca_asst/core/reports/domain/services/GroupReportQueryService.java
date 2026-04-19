package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpaceBattery;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.GroupReportQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.ReportDataQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la generación de informes grupales de riesgo psicosocial.
 *
 * <p>Orquesta la obtención de datos individuales de cada batería del espacio de análisis,
 * calcula el informe individual de cada persona con {@link ScoringEngine} y delega
 * el cálculo de estadísticas grupales al {@link GroupReportEngine}.</p>
 */
@RequiredArgsConstructor
public class GroupReportQueryService implements GroupReportQueryCUInputPort {

    private final AnalysisSpaceQueryRepository analysisSpaceQueryRepository;
    private final ReportDataQueryRepository reportDataQueryRepository;
    private final ScoringEngine scoringEngine;
    private final GroupReportEngine groupReportEngine;
    private final ResultFormatterOutputPort resultFormatter;

    /** Contexto con los resultados individuales calculados y las formas usadas. */
    private record CalculationContext(
        List<ScoringEngine.IndividualScoringResult> results,
        List<String> forms
    ) {}

    /**
     * Genera el resumen general grupal de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario que solicita el informe
     * @return resumen grupal con distribuciones, promedios y nivel por cuestionario
     */
    @Override
    public GroupReportEngine.GroupSummaryResult getGroupSummary(Long spaceId, Long userId) {
        CalculationContext context = calculateAllIndividualReports(spaceId, userId);
        return groupReportEngine.calculateGroupSummary(context.results(), context.forms());
    }

    /**
     * Genera el desglose por dominios y dimensiones de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario que solicita el informe
     * @return distribuciones de riesgo por dominio y dimensión
     */
    @Override
    public GroupReportEngine.DomainsAndDimensionsResult getDomainsAndDimensions(Long spaceId, Long userId) {
        CalculationContext context = calculateAllIndividualReports(spaceId, userId);
        return groupReportEngine.calculateDomainsAndDimensions(context.results());
    }

    /**
     * Genera la matriz de riesgo de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario que solicita el informe
     * @return matriz con magnitud del riesgo, índice de asociación y semáforos
     */
    @Override
    public GroupReportEngine.RiskMatrixResult getRiskMatrix(Long spaceId, Long userId) {
        CalculationContext context = calculateAllIndividualReports(spaceId, userId);
        List<String> stressLevels = context.results().stream()
            .map(r -> r.stress().stressLevel())
            .toList();
        return groupReportEngine.calculateRiskMatrix(context.results(), stressLevels);
    }

    /**
     * Calcula los informes individuales de todas las baterías del espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario que solicita el informe
     * @return contexto con los resultados individuales y las formas intralaborales usadas
     */
    private CalculationContext calculateAllIndividualReports(Long spaceId, Long userId) {
        // 1. Obtener espacio con baterías, validar existencia y propiedad
        Optional<AnalysisSpace> optionalSpace = analysisSpaceQueryRepository.findByIdWithBatteries(spaceId);
        if (optionalSpace.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.ANALYSIS_SPACE_NOT_FOUND,
                "user.report.space_not_found",
                spaceId
            );
        }
        AnalysisSpace space = optionalSpace.get();

        if (!space.getCreatorUserId().equals(userId)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.ANALYSIS_SPACE_ACCESS_DENIED,
                "user.report.space_access_denied",
                userId, spaceId
            );
        }

        // 2. Calcular informe individual para cada batería del espacio
        List<ScoringEngine.IndividualScoringResult> results = new ArrayList<>();
        List<String> forms = new ArrayList<>();

        for (AnalysisSpaceBattery battery : space.getBatteries()) {
            Long batteryRecordId = battery.getBatteryManagementRecordId();

            // 2a. Obtener detalles de la persona evaluada
            Optional<PersonEvaluatedDetails> optionalDetails =
                reportDataQueryRepository.getPersonDetailsByBatteryRecordId(batteryRecordId);
            if (optionalDetails.isEmpty()) {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND,
                    "user.person_evaluated_details.query_by_record_not_found",
                    batteryRecordId
                );
            }
            PersonEvaluatedDetails personDetails = optionalDetails.get();

            // 2b. Determinar forma intralaboral según tipo de cargo
            int jobPositionTypeId = personDetails.getJobPositionType().getId().intValue();
            String intralaboralForm = (jobPositionTypeId <= 2) ? "ILA" : "ILB";

            // 2c. Obtener respuestas de los 3 cuestionarios
            List<QuestionnaireManagementRecord> qRecords =
                reportDataQueryRepository.getQuestionnaireRecordsByBatteryId(batteryRecordId);

            List<QuestionnaireResponse> intralaboralResponses =
                getResponsesByAbbreviation(qRecords, intralaboralForm, batteryRecordId);
            List<QuestionnaireResponse> extralaboralResponses =
                getResponsesByAbbreviation(qRecords, "EXT", batteryRecordId);
            List<QuestionnaireResponse> stressResponses =
                getResponsesByAbbreviation(qRecords, "EST", batteryRecordId);

            // 2d. Calcular y guardar resultado
            ScoringEngine.IndividualScoringResult result = scoringEngine.calculateIndividualReport(
                intralaboralResponses, extralaboralResponses, stressResponses,
                intralaboralForm, jobPositionTypeId
            );

            results.add(result);
            forms.add(intralaboralForm);
        }

        return new CalculationContext(results, forms);
    }

    /**
     * Busca el registro de gestión de cuestionario por abreviatura y obtiene sus respuestas.
     * Lanza excepción si no se encuentra el cuestionario o no tiene respuestas.
     *
     * @param records         lista de registros de gestión de cuestionarios de la batería
     * @param abbreviation    abreviatura del cuestionario a buscar ("ILA", "ILB", "EXT", "EST")
     * @param batteryRecordId ID del registro de gestión de batería (para mensajes de error)
     * @return lista de respuestas del cuestionario
     */
    private List<QuestionnaireResponse> getResponsesByAbbreviation(
        List<QuestionnaireManagementRecord> records,
        String abbreviation,
        Long batteryRecordId
    ) {
        Optional<QuestionnaireManagementRecord> optionalRecord = records.stream()
            .filter(r -> abbreviation.equals(r.getQuestionnaire().getAbbreviation()))
            .findFirst();

        if (optionalRecord.isEmpty()) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.REPORT_INCOMPLETE_BATTERY,
                "user.report.incomplete_battery",
                batteryRecordId, abbreviation
            );
        }

        QuestionnaireManagementRecord record = optionalRecord.get();
        List<QuestionnaireResponse> responses =
            reportDataQueryRepository.getResponsesByQuestionnaireRecordId(record.getId());

        if (responses.isEmpty()) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.REPORT_INCOMPLETE_BATTERY,
                "user.report.incomplete_battery",
                batteryRecordId, abbreviation
            );
        }

        return responses;
    }
}
