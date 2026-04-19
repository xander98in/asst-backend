package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.IndividualReportQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.IndividualReportPdfOutputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.ReportDataQueryRepository;
import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la generación de informes individuales de riesgo psicosocial.
 *
 * <p>Orquesta la obtención de datos desde los repositorios y delega el cálculo
 * al {@link ScoringEngine}. Valida precondiciones: que la batería exista, esté cerrada
 * y tenga todos los cuestionarios respondidos.</p>
 */
@RequiredArgsConstructor
public class IndividualReportQueryService implements IndividualReportQueryCUInputPort {

    private final ReportDataQueryRepository reportDataQueryRepository;
    private final AnalysisSpaceQueryRepository analysisSpaceQueryRepository;
    private final EvaluatorQueryRepository evaluatorQueryRepository;
    private final IndividualReportPdfOutputPort pdfOutputPort;
    private final ScoringEngine scoringEngine;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Genera el informe individual de calificación de riesgo psicosocial
     * para una batería cerrada.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return resultado consolidado con intralaboral, extralaboral, total general y estrés
     */
    @Override
    public ScoringEngine.IndividualScoringResult getIndividualReport(Long batteryManagementRecordId) {
        BatteryManagementRecord record = loadAndValidateClosedBattery(batteryManagementRecordId);
        PersonEvaluatedDetails personDetails = loadPersonDetails(batteryManagementRecordId);
        return calculateReport(record, personDetails, batteryManagementRecordId);
    }

    /**
     * Genera el PDF del informe individual de riesgo psicosocial para una batería cerrada,
     * incluyendo los datos del evaluador asociado al espacio de análisis.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @param spaceId                   ID del espacio de análisis (para obtener el evaluador)
     * @param userId                    ID del usuario que solicita el informe
     * @return arreglo de bytes con el contenido del PDF generado
     */
    @Override
    public byte[] getIndividualReportPdf(Long batteryManagementRecordId, Long spaceId, Long userId) {
        // 1. Validar espacio y ownership
        Optional<AnalysisSpace> optionalSpace = analysisSpaceQueryRepository.findByIdWithBatteries(spaceId);
        if (optionalSpace.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.ANALYSIS_SPACE_NOT_FOUND, "user.report.space_not_found", spaceId);
        }
        AnalysisSpace space = optionalSpace.get();
        if (!space.getCreatorUserId().equals(userId)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.ANALYSIS_SPACE_ACCESS_DENIED, "user.report.space_access_denied", userId, spaceId);
        }

        // 2. Evaluador
        Optional<Evaluator> optionalEvaluator = evaluatorQueryRepository.findById(space.getEvaluatorId());
        if (optionalEvaluator.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.EVALUATOR_NOT_FOUND, "user.report.evaluator_not_found", space.getEvaluatorId());
        }
        Evaluator evaluator = optionalEvaluator.get();

        // 3. Datos de batería y persona
        BatteryManagementRecord record = loadAndValidateClosedBattery(batteryManagementRecordId);
        PersonEvaluatedDetails personDetails = loadPersonDetails(batteryManagementRecordId);

        // 4. Calcular
        ScoringEngine.IndividualScoringResult scoringResult =
            calculateReport(record, personDetails, batteryManagementRecordId);

        int jobPositionTypeId = personDetails.getJobPositionType().getId().intValue();
        String intralaboralForm = (jobPositionTypeId <= 2) ? "ILA" : "ILB";

        // 5. Generar PDF
        return pdfOutputPort.generatePdf(scoringResult, personDetails, record, evaluator, intralaboralForm);
    }

    // ── Métodos auxiliares privados ──────────────────────────────────────

    /**
     * Carga el registro de gestión de batería por su ID y valida que se encuentre en estado CERRADO.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return el registro de gestión de batería cargado y validado
     */
    private BatteryManagementRecord loadAndValidateClosedBattery(Long batteryManagementRecordId) {
        Optional<BatteryManagementRecord> optionalRecord =
            reportDataQueryRepository.getBatteryRecordById(batteryManagementRecordId);
        if (optionalRecord.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.BATTERY_RECORD_NOT_FOUND, "user.battery.query_not_found", batteryManagementRecordId);
        }
        BatteryManagementRecord record = optionalRecord.get();
        if (!BatteryManagementRecordStatusCode.CLOSED.getDescription().equals(record.getStatus().getName())) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.REPORT_BATTERY_NOT_CLOSED, "user.report.battery_not_closed", batteryManagementRecordId);
        }
        return record;
    }

    /**
     * Carga los detalles de la persona evaluada asociada al registro de gestión de batería.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return los detalles de la persona evaluada
     */
    private PersonEvaluatedDetails loadPersonDetails(Long batteryManagementRecordId) {
        Optional<PersonEvaluatedDetails> optionalDetails =
            reportDataQueryRepository.getPersonDetailsByBatteryRecordId(batteryManagementRecordId);
        if (optionalDetails.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.PERSON_DETAILS_NOT_FOUND,
                "user.person_evaluated_details.query_by_record_not_found", batteryManagementRecordId);
        }
        return optionalDetails.get();
    }

    /**
     * Determina la forma intralaboral (ILA o ILB) según el tipo de cargo, recupera las respuestas
     * de los cuestionarios intralaboral, extralaboral y de estrés, y delega el cálculo del informe
     * individual al {@link ScoringEngine}.
     *
     * @param record                    registro de gestión de batería (en estado CERRADO)
     * @param personDetails             detalles de la persona evaluada
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return resultado consolidado del informe individual
     */
    private ScoringEngine.IndividualScoringResult calculateReport(
        BatteryManagementRecord record, PersonEvaluatedDetails personDetails, Long batteryManagementRecordId
    ) {
        int jobPositionTypeId = personDetails.getJobPositionType().getId().intValue();
        String intralaboralForm = (jobPositionTypeId <= 2) ? "ILA" : "ILB";

        List<QuestionnaireManagementRecord> questionnaireRecords =
            reportDataQueryRepository.getQuestionnaireRecordsByBatteryId(batteryManagementRecordId);
        List<QuestionnaireResponse> intralaboralResponses =
            getResponsesByAbbreviation(questionnaireRecords, intralaboralForm, batteryManagementRecordId);
        List<QuestionnaireResponse> extralaboralResponses =
            getResponsesByAbbreviation(questionnaireRecords, "EXT", batteryManagementRecordId);
        List<QuestionnaireResponse> stressResponses =
            getResponsesByAbbreviation(questionnaireRecords, "EST", batteryManagementRecordId);

        return scoringEngine.calculateIndividualReport(
            intralaboralResponses, extralaboralResponses, stressResponses, intralaboralForm, jobPositionTypeId
        );
    }

    /**
     * Busca el registro de gestión de cuestionario por abreviatura y obtiene sus respuestas.
     * Lanza excepción si no se encuentra el cuestionario o no tiene respuestas.
     *
     * @param records                   lista de registros de gestión de cuestionarios de la batería
     * @param abbreviation              abreviatura del cuestionario a buscar ("ILA", "ILB", "EXT", "EST")
     * @param batteryManagementRecordId ID del registro de gestión de batería (para mensajes de error)
     * @return lista de respuestas del cuestionario
     */
    private List<QuestionnaireResponse> getResponsesByAbbreviation(
        List<QuestionnaireManagementRecord> records,
        String abbreviation,
        Long batteryManagementRecordId
    ) {
        Optional<QuestionnaireManagementRecord> optionalRecord = records.stream()
            .filter(r -> abbreviation.equals(r.getQuestionnaire().getAbbreviation()))
            .findFirst();
        if (optionalRecord.isEmpty()) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.REPORT_INCOMPLETE_BATTERY,
                "user.report.incomplete_battery",
                batteryManagementRecordId, abbreviation
            );
        }
        QuestionnaireManagementRecord record = optionalRecord.get();

        List<QuestionnaireResponse> responses =
            reportDataQueryRepository.getResponsesByQuestionnaireRecordId(record.getId());

        if (responses.isEmpty()) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.REPORT_INCOMPLETE_BATTERY,
                "user.report.incomplete_battery",
                batteryManagementRecordId, abbreviation
            );
        }

        return responses;
    }
}
