package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.pdf;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.IndividualReportPdfOutputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.ScoringEngine;

import lombok.RequiredArgsConstructor;

/**
 * Adaptador de salida que genera el PDF del informe individual de riesgo psicosocial
 * usando Thymeleaf para el renderizado HTML y OpenHTMLtoPDF para la conversión a PDF.
 */
@Component
@RequiredArgsConstructor
public class IndividualReportPdfGenerator implements IndividualReportPdfOutputPort {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final TemplateEngine templateEngine;

    /** {@inheritDoc} */
    @Override
    public byte[] generatePdf(
        ScoringEngine.IndividualScoringResult scoringResult,
        PersonEvaluatedDetails personDetails,
        BatteryManagementRecord batteryRecord,
        Evaluator evaluator,
        String intralaboralForm
    ) {
        Context context = new Context();
        populateWorkerData(context, personDetails, batteryRecord);
        populateEvaluatorData(context, evaluator);
        populateForm(context, intralaboralForm);
        populateIntralaboralResults(context, scoringResult);
        populateExtralaboralResults(context, scoringResult);
        populateGeneralTotal(context, scoringResult);
        populateStress(context, scoringResult);
        context.setVariable("reportDate", LocalDate.now().format(DATE_FORMATTER));
        context.setVariable("observations", generateObservations(scoringResult, batteryRecord));
        context.setVariable("recommendations", generateRecommendations());

        String html = templateEngine.process("reports/individual-report", context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Error generando el PDF del informe individual", e);
        }
    }

    private void populateWorkerData(Context context, PersonEvaluatedDetails details, BatteryManagementRecord record) {
        PersonEvaluated person = record.getPersonEvaluated();
        String fullName = buildFullName(person);

        context.setVariable("workerName", fullName);
        context.setVariable("workerId", person.getIdentificationNumber());
        context.setVariable("workerPosition", details.getJobTitle());
        context.setVariable("workerDepartment", details.getWorkAreaName());
        context.setVariable("workerAge", calculateAge(person.getBirthYear()));
        context.setVariable("workerGender", details.getGender() != null ? details.getGender().getName() : "—");
        context.setVariable("applicationDate", formatDate(record.getCreatedAt()));
        context.setVariable("companyName", "Universidad del Cauca");
    }

    private void populateEvaluatorData(Context context, Evaluator evaluator) {
        context.setVariable("evaluatorName", evaluator.getFullName());
        context.setVariable("evaluatorId", evaluator.getIdentificationNumber());
        context.setVariable("evaluatorProfession", evaluator.getProfession());
        context.setVariable("evaluatorPostgraduate",
            evaluator.getPostgraduateDegree() != null ? evaluator.getPostgraduateDegree() : "—");
        context.setVariable("evaluatorProfessionalCard", evaluator.getProfessionalCardNumber());
        context.setVariable("evaluatorLicense", evaluator.getOccupationalHealthLicense());
        context.setVariable("evaluatorLicenseDate",
            evaluator.getLicenseExpirationDate() != null
                ? evaluator.getLicenseExpirationDate().format(DATE_FORMATTER)
                : "—");
    }

    private void populateForm(Context context, String intralaboralForm) {
        context.setVariable("intralaboralForm", "ILA".equals(intralaboralForm) ? "A" : "B");
    }

    private void populateIntralaboralResults(Context context, ScoringEngine.IndividualScoringResult result) {
        List<Map<String, Object>> domains = new ArrayList<>();
        for (ScoringEngine.DomainScore domain : result.intralaboral().domains()) {
            Map<String, Object> domainMap = new LinkedHashMap<>();
            domainMap.put("domainName", domain.domainName());
            domainMap.put("transformedScore", domain.transformedScore());
            domainMap.put("riskLevel", domain.riskLevel());

            List<Map<String, Object>> dimensions = new ArrayList<>();
            for (ScoringEngine.DimensionScore dim : domain.dimensions()) {
                Map<String, Object> dimMap = new LinkedHashMap<>();
                dimMap.put("dimensionName", dim.dimensionName());
                dimMap.put("transformedScore", dim.transformedScore());
                dimMap.put("riskLevel", dim.riskLevel());
                dimensions.add(dimMap);
            }
            domainMap.put("dimensions", dimensions);
            domains.add(domainMap);
        }
        context.setVariable("intralaboralDomains", domains);
        context.setVariable("intralaboralTotalScore", result.intralaboral().totalTransformedScore());
        context.setVariable("intralaboralTotalRiskLevel", result.intralaboral().totalRiskLevel());
    }

    private void populateExtralaboralResults(Context context, ScoringEngine.IndividualScoringResult result) {
        List<Map<String, Object>> dimensions = new ArrayList<>();
        for (ScoringEngine.DimensionScore dim : result.extralaboral().dimensions()) {
            Map<String, Object> dimMap = new LinkedHashMap<>();
            dimMap.put("dimensionName", dim.dimensionName());
            dimMap.put("transformedScore", dim.transformedScore());
            dimMap.put("riskLevel", dim.riskLevel());
            dimensions.add(dimMap);
        }
        context.setVariable("extralaboralDimensions", dimensions);
        context.setVariable("extralaboralTotalScore", result.extralaboral().totalTransformedScore());
        context.setVariable("extralaboralTotalRiskLevel", result.extralaboral().totalRiskLevel());
    }

    private void populateGeneralTotal(Context context, ScoringEngine.IndividualScoringResult result) {
        context.setVariable("generalTotalScore", result.generalTotal().transformedScore());
        context.setVariable("generalTotalRiskLevel", result.generalTotal().riskLevel());
    }

    private void populateStress(Context context, ScoringEngine.IndividualScoringResult result) {
        context.setVariable("stressScore", result.stress().transformedScore());
        context.setVariable("stressLevel", result.stress().stressLevel());
    }

    private String buildFullName(PersonEvaluated person) {
        String first = person.getFirstName() == null ? "" : person.getFirstName();
        String last = person.getLastName() == null ? "" : person.getLastName();
        return (first + " " + last).trim();
    }

    private String formatDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate().format(DATE_FORMATTER) : "—";
    }

    private String calculateAge(Integer birthYear) {
        if (birthYear == null) {
            return "—";
        }
        int age = LocalDate.now().getYear() - birthYear;
        return age + " años";
    }

    private String generateObservations(ScoringEngine.IndividualScoringResult result, BatteryManagementRecord record) {
        String workerName = buildFullName(record.getPersonEvaluated());

        StringBuilder sb = new StringBuilder();
        sb.append("De acuerdo con la valoración de factores de riesgo psicosocial aplicado al funcionario: ")
          .append("<strong>").append(workerName).append("</strong>, se obtuvo la siguiente información.")
          .append("<br/><br/>");

        sb.append("<strong>1. FACTORES DE RIESGO INTRALABORAL</strong><br/>");
        sb.append("Los resultados de factores de RIESGO PSICOSOCIAL INTRALABORAL a nivel general obtuvieron una puntuación de ")
          .append(result.intralaboral().totalTransformedScore())
          .append(" con nivel <strong>").append(result.intralaboral().totalRiskLevel()).append("</strong>. ")
          .append("Los dominios y dimensiones que se encuentran en riesgo medio, alto y muy alto son:<br/><br/>");

        for (ScoringEngine.DomainScore domain : result.intralaboral().domains()) {
            if (isMediumOrHigherRisk(domain.riskLevel())) {
                sb.append("• <strong>").append(domain.domainName()).append("</strong> - ")
                  .append(domain.riskLevel()).append("<br/>");
                for (ScoringEngine.DimensionScore dim : domain.dimensions()) {
                    if (isMediumOrHigherRisk(dim.riskLevel())) {
                        sb.append("&#160;&#160;&#160;&#160;- ").append(dim.dimensionName())
                          .append(" - ").append(dim.riskLevel()).append("<br/>");
                    }
                }
            }
        }
        sb.append("<br/>");

        sb.append("<strong>2. FACTORES DE RIESGO EXTRALABORAL</strong><br/>");
        sb.append("Los resultados de factores de RIESGO PSICOSOCIAL EXTRALABORAL a nivel general obtuvieron una puntuación de ")
          .append(result.extralaboral().totalTransformedScore())
          .append(" con nivel <strong>").append(result.extralaboral().totalRiskLevel()).append("</strong>. ")
          .append("Las dimensiones de riesgo que se encuentran en riesgo alto y muy alto son:<br/><br/>");

        for (ScoringEngine.DimensionScore dim : result.extralaboral().dimensions()) {
            if (isHighOrVeryHighRisk(dim.riskLevel())) {
                sb.append("• ").append(dim.dimensionName())
                  .append(" - ").append(dim.riskLevel()).append("<br/>");
            }
        }
        sb.append("<br/>");

        sb.append("<strong>3. FACTORES DE RIESGO INDIVIDUAL</strong><br/>");
        sb.append("El cuestionario de Estrés presentó un nivel de Riesgo <strong>")
          .append(result.stress().stressLevel()).append("</strong> con una puntuación transformada de ")
          .append(result.stress().transformedScore()).append(".");

        return sb.toString();
    }

    private String generateRecommendations() {
        return "<strong>Empresa:</strong><br/>"
            + "• Intervenir los Dominios y Dimensiones que se encuentran en riesgo medio, alto y muy alto a través de actividades individuales y grupales dirigidas desde el área de Seguridad y Salud en el Trabajo.<br/>"
            + "• Implementar evaluaciones regulares de riesgos psicosociales para monitorear la evolución de los factores identificados.<br/>"
            + "• Fomentar un ambiente de trabajo saludable mediante políticas claras de bienestar, comunicación y reconocimiento.<br/>"
            + "• Fortalecer en el programa de capacitación temas de relaciones humanas, manejo del estrés y trabajo en equipo.<br/>"
            + "• Realizar seguimientos regulares a las medidas implementadas.<br/><br/>"
            + "<strong>Trabajador:</strong><br/>"
            + "• Es necesario revisar los dominios y dimensiones que presentaron mayor riesgo con el apoyo de la Psicóloga del área de Seguridad y Salud en el Trabajo.<br/>"
            + "• Dedica tiempo para cuidar tu salud física y emocional, incluyendo descanso, ejercicio y alimentación balanceada.<br/>"
            + "• Establece límites claros entre el trabajo y tu vida personal para mantener un equilibrio saludable.<br/>"
            + "• Adherencia a las actividades de intervención programadas por la institución.<br/>"
            + "• Informar al área de Seguridad y Salud en el Trabajo condiciones de salud relevantes.";
    }

    private boolean isMediumOrHigherRisk(String riskLevel) {
        return "Riesgo medio".equalsIgnoreCase(riskLevel)
            || "Riesgo alto".equalsIgnoreCase(riskLevel)
            || "Riesgo muy alto".equalsIgnoreCase(riskLevel);
    }

    private boolean isHighOrVeryHighRisk(String riskLevel) {
        return "Riesgo alto".equalsIgnoreCase(riskLevel)
            || "Riesgo muy alto".equalsIgnoreCase(riskLevel);
    }
}
