package com.unicuaca.asst.unicauca_asst.common.config;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.*;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.services.*;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.BatteryManagementRecordQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.*;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services.*;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.IndividualReportPdfOutputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.ReportDataQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.AnalysisSpaceCommandService;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.AnalysisSpaceQueryService;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.EvaluatorCommandService;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.EvaluatorQueryService;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.GroupReportEngine;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.GroupReportQueryService;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.IndividualReportQueryService;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.ScoringEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.domain.services.CatalogQueryService;

/**
 * Registro central de los beans de servicios de dominio de la aplicación.
 *
 * <p>En la arquitectura hexagonal + CQRS del proyecto, los servicios de dominio se escriben
 * en Java puro sin anotaciones de Spring para mantener la capa {@code domain} libre del
 * framework. Esta clase declara manualmente cada uno de esos servicios como {@code @Bean},
 * inyectando los puertos de salida (repositorios y adaptadores) necesarios para construirlos.</p>
 *
 * <p>Los servicios de infraestructura (por ejemplo {@code GoogleTokenService} o
 * {@code JwtService}) quedan fuera de este registro porque utilizan directamente la anotación
 * {@code @Service}.</p>
 */
@Configuration
public class BeanConfigurations {

    /**
     * Registra el servicio de dominio {@link CatalogQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    CatalogQueryService catalogQueryService(
        CatalogQueryRepository catalogQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new CatalogQueryService(
            catalogQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link PersonEvaluatedQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    PersonEvaluatedQueryService personQueryService(
        PersonEvaluatedQueryRepository personQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort,
        CatalogQueryRepository catalogQueryRepository
    ) {
        return new PersonEvaluatedQueryService(
            personQueryRepository,
            catalogQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link PersonEvaluatedCommandService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    PersonEvaluatedCommandService personEvaluatedCommandService(
        PersonEvaluatedCommandRepository personEvaluatedCommandRepository,
        PersonEvaluatedQueryRepository personEvaluatedQueryRepository,
        BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository,
        CatalogQueryRepository catalogQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new PersonEvaluatedCommandService(
            personEvaluatedCommandRepository,
            personEvaluatedQueryRepository,
            batteryManagementRecordQueryRepository,
            catalogQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link BatteryManagementRecordCommandService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    BatteryManagementRecordCommandService batteryManagementRecordCommandService(
        BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository,
        BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository,
        PersonEvaluatedQueryRepository personEvaluatedQueryRepository,
        PersonEvaluatedCommandRepository personEvaluatedCommandRepository,
        QuestionnaireManagementRecordCommandRepository questionnaireManagementRecordCommandRepository,
        QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository,
        QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new BatteryManagementRecordCommandService(
            batteryManagementRecordCommandRepository,
            batteryManagementRecordQueryRepository,
            personEvaluatedQueryRepository,
            personEvaluatedCommandRepository,
            questionnaireManagementRecordCommandRepository,
            questionnaireManagementRecordQueryRepository,
            questionnaireManagementRecordStatusQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link QuestionnaireQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    QuestionnaireQueryService questionnaireQueryService(
        QuestionnaireQueryRepository questionnaireQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new QuestionnaireQueryService(
            questionnaireQueryRepository, resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link PersonEvaluatedDetailsCommandService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    PersonEvaluatedDetailsCommandService personEvaluatedDetailsCommandService(
        CatalogQueryRepository catalogQueryRepository,
        BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository,
        PersonEvaluatedDetailsCommandRepository personEvaluatedDetailsCommandRepository,
        PersonEvaluatedDetailsQueryRepository personEvaluatedDetailsQueryRepository,
        QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository,
        BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository,
        BatteryManagementRecordStatusQueryRepository batteryManagementRecordStatusQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new PersonEvaluatedDetailsCommandService(
            catalogQueryRepository,
            batteryManagementRecordQueryRepository,
            personEvaluatedDetailsCommandRepository,
            personEvaluatedDetailsQueryRepository,
            questionnaireManagementRecordQueryRepository,
            batteryManagementRecordCommandRepository,
            batteryManagementRecordStatusQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link QuestionnaireManagementRecordStatusQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    QuestionnaireManagementRecordStatusQueryService questionnaireManagementRecordStatusQueryService(
        QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new QuestionnaireManagementRecordStatusQueryService(
            questionnaireManagementRecordStatusQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link QuestionQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    QuestionQueryService questionQueryService(
        QuestionQueryRepository questionQueryRepository,
        QuestionnaireQueryRepository questionnaireQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new QuestionQueryService(
            questionQueryRepository,
            questionnaireQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link BatteryManagementRecordQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public BatteryManagementRecordQueryService batteryManagementRecordQueryCUInputPort(
        BatteryManagementRecordQueryRepository recordQueryRepository,
        PersonEvaluatedDetailsQueryRepository detailsQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new BatteryManagementRecordQueryService(
            recordQueryRepository,
            detailsQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link PersonEvaluatedDetailsQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public PersonEvaluatedDetailsQueryService personEvaluatedDetailsQueryService(
        PersonEvaluatedDetailsQueryRepository personEvaluatedDetailsQueryRepository,
        BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new PersonEvaluatedDetailsQueryService(
            personEvaluatedDetailsQueryRepository,
            batteryManagementRecordQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link QuestionnaireManagementRecordQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public QuestionnaireManagementRecordQueryService questionnaireManagementRecordQueryService(
        QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new QuestionnaireManagementRecordQueryService(
            questionnaireManagementRecordQueryRepository,
            resultFormatterOutputPort
        );
    }


    /**
     * Registra el servicio de dominio {@link QuestionnaireResponseCommandService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public QuestionnaireResponseCommandService questionnaireResponseCommandService(
        QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository,
        QuestionQueryRepository questionQueryRepository,
        AnswerOptionQueryRepository answerOptionQueryRepository,
        QuestionnaireResponseQueryRepository questionnaireResponseQueryRepository,
        QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository,
        BatteryManagementRecordStatusQueryRepository batteryManagementRecordStatusQueryRepository,
        BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository,
        QuestionnaireResponseCommandRepository questionnaireResponseCommandRepository,
        QuestionnaireManagementRecordCommandRepository questionnaireManagementRecordCommandRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new QuestionnaireResponseCommandService(
            questionnaireManagementRecordQueryRepository,
            questionQueryRepository,
            answerOptionQueryRepository,
            questionnaireResponseQueryRepository,
            questionnaireManagementRecordStatusQueryRepository,
            batteryManagementRecordStatusQueryRepository,
            batteryManagementRecordCommandRepository,
            questionnaireResponseCommandRepository,
            questionnaireManagementRecordCommandRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link QuestionnaireManagementRecordCommandService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public QuestionnaireManagementRecordCommandService questionnaireManagementRecordCommandService(
        BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository,
        QuestionnaireQueryRepository questionnaireQueryRepository,
        QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository,
        QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository,
        BatteryManagementRecordStatusQueryRepository batteryManagementRecordStatusQueryRepository,
        QuestionnaireResponseCommandRepository questionnaireResponseCommandRepository,
        QuestionnaireManagementRecordCommandRepository questionnaireManagementRecordCommandRepository,
        BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new QuestionnaireManagementRecordCommandService(
            batteryManagementRecordQueryRepository,
            questionnaireQueryRepository,
            questionnaireManagementRecordStatusQueryRepository,
            questionnaireManagementRecordQueryRepository,
            batteryManagementRecordStatusQueryRepository,
            questionnaireResponseCommandRepository,
            questionnaireManagementRecordCommandRepository,
            batteryManagementRecordCommandRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link QuestionnaireResponseQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public QuestionnaireResponseQueryService questionnaireResponseQueryService(
        QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository,
        QuestionnaireResponseQueryRepository questionnaireResponseQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new QuestionnaireResponseQueryService(
            questionnaireManagementRecordQueryRepository,
            questionnaireResponseQueryRepository,
            resultFormatterOutputPort
        );
    }

    // ── Módulo Auth ──────────────────────────────────────────────────────

    /**
     * Registra el servicio de dominio {@link AuthService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public AuthService authService(
        SystemUserQueryRepository systemUserQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new AuthService(
            systemUserQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link SystemUserCommandService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public SystemUserCommandService systemUserCommandService(
        SystemUserCommandRepository systemUserCommandRepository,
        SystemUserQueryRepository systemUserQueryRepository,
        UserStatusQueryRepository userStatusQueryRepository,
        RoleQueryRepository roleQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new SystemUserCommandService(
            systemUserCommandRepository,
            systemUserQueryRepository,
            userStatusQueryRepository,
            roleQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link SystemUserQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public SystemUserQueryService systemUserQueryService(
        SystemUserQueryRepository systemUserQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new SystemUserQueryService(
            systemUserQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link RoleQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public RoleQueryService roleQueryService(
        RoleQueryRepository roleQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new RoleQueryService(
            roleQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link UserStatusQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public UserStatusQueryService userStatusQueryService(
        UserStatusQueryRepository userStatusQueryRepository
    ) {
        return new UserStatusQueryService(userStatusQueryRepository);
    }

    // ── Módulo de Informes ──────────────────────────────────────────────

    /**
     * Registra el motor de dominio {@link ScoringEngine}.
     *
     * @return instancia del motor lista para ser inyectada
     */
    @Bean
    public ScoringEngine scoringEngine() {
        return new ScoringEngine();
    }

    /**
     * Registra el servicio de dominio {@link IndividualReportQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public IndividualReportQueryService individualReportQueryService(
        ReportDataQueryRepository reportDataQueryRepository,
        AnalysisSpaceQueryRepository analysisSpaceQueryRepository,
        EvaluatorQueryRepository evaluatorQueryRepository,
        IndividualReportPdfOutputPort individualReportPdfOutputPort,
        ScoringEngine scoringEngine,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new IndividualReportQueryService(
            reportDataQueryRepository,
            analysisSpaceQueryRepository,
            evaluatorQueryRepository,
            individualReportPdfOutputPort,
            scoringEngine,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link AnalysisSpaceCommandService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public AnalysisSpaceCommandService analysisSpaceCommandService(
        AnalysisSpaceCommandRepository analysisSpaceCommandRepository,
        AnalysisSpaceQueryRepository analysisSpaceQueryRepository,
        ReportDataQueryRepository reportDataQueryRepository,
        EvaluatorQueryRepository evaluatorQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new AnalysisSpaceCommandService(
            analysisSpaceCommandRepository,
            analysisSpaceQueryRepository,
            reportDataQueryRepository,
            evaluatorQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link AnalysisSpaceQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public AnalysisSpaceQueryService analysisSpaceQueryService(
        AnalysisSpaceQueryRepository analysisSpaceQueryRepository,
        BatteryManagementRecordQueryCUInputPort batteryManagementRecordQueryCUInputPort,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new AnalysisSpaceQueryService(
            analysisSpaceQueryRepository,
            batteryManagementRecordQueryCUInputPort,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el motor de dominio {@link GroupReportEngine}.
     *
     * @return instancia del motor lista para ser inyectada
     */
    @Bean
    public GroupReportEngine groupReportEngine() {
        return new GroupReportEngine();
    }

    /**
     * Registra el servicio de dominio {@link GroupReportQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public GroupReportQueryService groupReportQueryService(
        AnalysisSpaceQueryRepository analysisSpaceQueryRepository,
        ReportDataQueryRepository reportDataQueryRepository,
        ScoringEngine scoringEngine,
        GroupReportEngine groupReportEngine,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new GroupReportQueryService(
            analysisSpaceQueryRepository,
            reportDataQueryRepository,
            scoringEngine,
            groupReportEngine,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link EvaluatorCommandService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public EvaluatorCommandService evaluatorCommandService(
        EvaluatorCommandRepository evaluatorCommandRepository,
        EvaluatorQueryRepository evaluatorQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new EvaluatorCommandService(
            evaluatorCommandRepository,
            evaluatorQueryRepository,
            resultFormatterOutputPort
        );
    }

    /**
     * Registra el servicio de dominio {@link EvaluatorQueryService}.
     *
     * @return instancia del servicio lista para ser inyectada
     */
    @Bean
    public EvaluatorQueryService evaluatorQueryService(
        EvaluatorQueryRepository evaluatorQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new EvaluatorQueryService(
            evaluatorQueryRepository,
            resultFormatterOutputPort
        );
    }
}
