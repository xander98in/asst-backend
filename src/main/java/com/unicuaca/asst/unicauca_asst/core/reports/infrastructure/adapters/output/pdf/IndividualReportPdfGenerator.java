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

    /**
     * Genera el PDF del informe individual de riesgo psicosocial a partir
     * de los resultados calculados y los datos de contexto.
     *
     * @param scoringResult     resultados de calificación del {@link ScoringEngine}
     * @param personDetails     detalles sociodemográficos y laborales de la persona evaluada
     * @param batteryRecord     registro de batería (provee fecha de aplicación y datos personales)
     * @param evaluator         datos del evaluador responsable del informe
     * @param intralaboralForm  forma del cuestionario intralaboral aplicada: {@code "ILA"} o {@code "ILB"}
     * @return arreglo de bytes con el contenido del PDF generado
     */
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

    /**
     * Inyecta en el contexto Thymeleaf los datos sociodemográficos y laborales del
     * trabajador evaluado que se renderizan en la cabecera del informe.
     *
     * @param context contexto Thymeleaf donde se agregan las variables
     * @param details detalles sociodemográficos y laborales de la persona evaluada
     * @param record  registro de batería del que se toma la fecha de aplicación y los datos básicos
     */
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

    /**
     * Inyecta en el contexto Thymeleaf los datos del evaluador responsable del informe
     * (nombre, identificación, profesión, tarjeta profesional y licencia).
     *
     * @param context   contexto Thymeleaf donde se agregan las variables
     * @param evaluator datos del evaluador responsable del informe
     */
    private void populateEvaluatorData(Context context, Evaluator evaluator) {
        context.setVariable("evaluatorName", evaluator.getFullName());
        context.setVariable("evaluatorId", evaluator.getIdentificationNumber());
        context.setVariable("evaluatorProfession", evaluator.getProfession());
        context.setVariable("evaluatorPostgraduate",
            evaluator.getPostgraduateDegree() != null ? evaluator.getPostgraduateDegree() : "—");
        context.setVariable("evaluatorProfessionalCard", evaluator.getProfessionalCardNumber());
        context.setVariable("evaluatorLicense", evaluator.getOccupationalHealthLicense());
        context.setVariable("evaluatorLicenseDate",
            evaluator.getLicenseIssueDate() != null
                ? evaluator.getLicenseIssueDate().format(DATE_FORMATTER)
                : "—");
    }

    /**
     * Inyecta en el contexto Thymeleaf la forma del cuestionario intralaboral aplicada
     * ("A" o "B"), traduciendo el código interno ILA/ILB al formato mostrado en el PDF.
     *
     * @param context          contexto Thymeleaf donde se agrega la variable
     * @param intralaboralForm forma intralaboral aplicada ({@code "ILA"} o {@code "ILB"})
     */
    private void populateForm(Context context, String intralaboralForm) {
        context.setVariable("intralaboralForm", "ILA".equals(intralaboralForm) ? "A" : "B");
    }

    /**
     * Inyecta en el contexto Thymeleaf los resultados intralaborales (dominios con sus
     * dimensiones y el total) en formato de lista de mapas consumibles por la plantilla.
     *
     * @param context contexto Thymeleaf donde se agregan las variables
     * @param result  resultado individual completo producido por el motor de cálculo
     */
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

    /**
     * Inyecta en el contexto Thymeleaf los resultados extralaborales (dimensiones y total)
     * en formato de lista de mapas consumibles por la plantilla.
     *
     * @param context contexto Thymeleaf donde se agregan las variables
     * @param result  resultado individual completo producido por el motor de cálculo
     */
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

    /**
     * Inyecta en el contexto Thymeleaf el puntaje total general (intralaboral + extralaboral)
     * junto con su nivel de riesgo asociado.
     *
     * @param context contexto Thymeleaf donde se agregan las variables
     * @param result  resultado individual completo producido por el motor de cálculo
     */
    private void populateGeneralTotal(Context context, ScoringEngine.IndividualScoringResult result) {
        context.setVariable("generalTotalScore", result.generalTotal().transformedScore());
        context.setVariable("generalTotalRiskLevel", result.generalTotal().riskLevel());
    }

    /**
     * Inyecta en el contexto Thymeleaf el puntaje del cuestionario de estrés y su
     * nivel asociado.
     *
     * @param context contexto Thymeleaf donde se agregan las variables
     * @param result  resultado individual completo producido por el motor de cálculo
     */
    private void populateStress(Context context, ScoringEngine.IndividualScoringResult result) {
        context.setVariable("stressScore", result.stress().transformedScore());
        context.setVariable("stressLevel", result.stress().stressLevel());
    }

    /**
     * Construye el nombre completo de la persona evaluada concatenando nombres y
     * apellidos, tolerando valores nulos.
     *
     * @param person persona evaluada de la que se obtienen nombres y apellidos
     * @return nombre completo recortado, o cadena vacía si ambos campos son nulos
     */
    private String buildFullName(PersonEvaluated person) {
        String first = person.getFirstName() == null ? "" : person.getFirstName();
        String last = person.getLastName() == null ? "" : person.getLastName();
        return (first + " " + last).trim();
    }

    /**
     * Formatea una fecha y hora al formato dd/MM/yyyy esperado por el PDF,
     * retornando un guion largo si la entrada es nula.
     *
     * @param dateTime fecha y hora a formatear (puede ser nula)
     * @return representación textual de la fecha en formato dd/MM/yyyy, o {@code "—"} si es nula
     */
    private String formatDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate().format(DATE_FORMATTER) : "—";
    }

    /**
     * Calcula la edad aproximada a partir del año de nacimiento usando el año actual.
     *
     * @param birthYear año de nacimiento (puede ser nulo)
     * @return edad en años seguida del sufijo "años", o {@code "—"} si el año es nulo
     */
    private String calculateAge(Integer birthYear) {
        if (birthYear == null) {
            return "—";
        }
        int age = LocalDate.now().getYear() - birthYear;
        return age + " años";
    }

    /**
     * Construye el bloque HTML de observaciones del informe, resumiendo los resultados
     * de riesgo intralaboral, extralaboral y de estrés, destacando dominios y dimensiones
     * con nivel medio, alto o muy alto.
     *
     * @param result resultado individual completo producido por el motor de cálculo
     * @param record registro de batería del que se obtiene el nombre del evaluado
     * @return fragmento HTML con las observaciones listas para renderizar
     */
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

    /**
     * Construye el bloque HTML con las recomendaciones generales dirigidas a la empresa
     * y al trabajador que se incluye al final del informe individual.
     *
     * @return fragmento HTML con las recomendaciones listas para renderizar
     */
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

    /**
     * Determina si un nivel de riesgo corresponde a medio, alto o muy alto, comparando
     * el texto sin distinguir mayúsculas y minúsculas.
     *
     * @param riskLevel etiqueta textual del nivel de riesgo
     * @return {@code true} si es riesgo medio, alto o muy alto; {@code false} en caso contrario
     */
    private boolean isMediumOrHigherRisk(String riskLevel) {
        return "Riesgo medio".equalsIgnoreCase(riskLevel)
            || "Riesgo alto".equalsIgnoreCase(riskLevel)
            || "Riesgo muy alto".equalsIgnoreCase(riskLevel);
    }

    /**
     * Determina si un nivel de riesgo corresponde a alto o muy alto, comparando el
     * texto sin distinguir mayúsculas y minúsculas.
     *
     * @param riskLevel etiqueta textual del nivel de riesgo
     * @return {@code true} si es riesgo alto o muy alto; {@code false} en caso contrario
     */
    private boolean isHighOrVeryHighRisk(String riskLevel) {
        return "Riesgo alto".equalsIgnoreCase(riskLevel)
            || "Riesgo muy alto".equalsIgnoreCase(riskLevel);
    }
}
