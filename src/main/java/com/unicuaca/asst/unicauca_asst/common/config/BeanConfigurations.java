package com.unicuaca.asst.unicauca_asst.common.config;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.*;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.domain.services.CatalogQueryService;

@Configuration
public class BeanConfigurations {

    @Bean
    CatalogQueryService catalogQueryService(CatalogQueryRepository catalogQueryRepository, ResultFormatterOutputPort resultFormatterOutputPort) {
        return new CatalogQueryService(catalogQueryRepository, resultFormatterOutputPort);
    }

    @Bean
    PersonEvaluatedQueryService personQueryService(PersonEvaluatedQueryRepository personQueryRepository, ResultFormatterOutputPort resultFormatterOutputPort,
                                                   CatalogQueryRepository catalogQueryRepository) {
        return new PersonEvaluatedQueryService(personQueryRepository, catalogQueryRepository, resultFormatterOutputPort);
    }

    /**
     * Crea una instancia de PersonEvaluatedCommandService.
     *
     * @param personEvaluatedCommandRepository
     * @param personEvaluatedQueryRepository
     * @param batteryManagementRecordQueryRepository
     * @param catalogQueryRepository
     * @param resultFormatterOutputPort
     * @return
     */
    @Bean
    PersonEvaluatedCommandService personEvaluatedCommandService(
        PersonEvaluatedCommandRepository personEvaluatedCommandRepository,
        PersonEvaluatedQueryRepository personEvaluatedQueryRepository,
        BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository,
        CatalogQueryRepository catalogQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new PersonEvaluatedCommandService(personEvaluatedCommandRepository, personEvaluatedQueryRepository, batteryManagementRecordQueryRepository, catalogQueryRepository, resultFormatterOutputPort);
    }

    @Bean
    BatteryManagementRecordCommandService batteryManagementRecordCommandService(
        BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository,
        BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository,
        PersonEvaluatedQueryRepository personEvaluatedQueryRepository,
        PersonEvaluatedCommandRepository personEvaluatedCommandRepository,
        ResultFormatterOutputPort resultFormatterOutputPort) {
        return new BatteryManagementRecordCommandService(batteryManagementRecordCommandRepository,
            batteryManagementRecordQueryRepository, personEvaluatedQueryRepository, personEvaluatedCommandRepository, resultFormatterOutputPort);
    }

    @Bean
    QuestionnaireQueryService questionnaireQueryService(
        QuestionnaireQueryRepository questionnaireQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort) {
        return new QuestionnaireQueryService(questionnaireQueryRepository, resultFormatterOutputPort);
    }

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

    @Bean
    QuestionnaireManagementRecordStatusQueryService questionnaireManagementRecordStatusQueryService(
        QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort) {
        return new QuestionnaireManagementRecordStatusQueryService(
            questionnaireManagementRecordStatusQueryRepository,
            resultFormatterOutputPort
        );
    }

    @Bean
    QuestionQueryService questionQueryService(
        QuestionQueryRepository questionQueryRepository,
        QuestionnaireQueryRepository questionnaireQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort) {
        return new QuestionQueryService(
            questionQueryRepository,
            questionnaireQueryRepository,
            resultFormatterOutputPort
        );
    }

    @Bean
    public BatteryManagementRecordQueryService batteryManagementRecordQueryCUInputPort(
        BatteryManagementRecordQueryRepository recordQueryRepository,
        PersonEvaluatedDetailsQueryRepository detailsQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort
    ) {
        return new BatteryManagementRecordQueryService(recordQueryRepository, detailsQueryRepository, resultFormatterOutputPort);
    }

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
     * Crea una instancia de QuestionnaireManagementRecordQueryService.
     *
     * @param questionnaireManagementRecordQueryRepository
     * @param resultFormatterOutputPort
     * @return QuestionnaireManagementRecordQueryService
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
     * Crea una instancia de QuestionnaireResponseCommandService.
     * Implementa la lógica de negocio para manejar las respuestas a los cuestionarios, incluyendo validaciones,
     * reglas de negocio y orquestación de llamadas a los repositorios necesarios para procesar las respuestas.
     *
     * @param questionnaireManagementRecordQueryRepository
     * @param questionQueryRepository
     * @param answerOptionQueryRepository
     * @param questionnaireResponseQueryRepository
     * @param questionnaireResponseCommandRepository
     * @param resultFormatterOutputPort
     * @return QuestionnaireResponseCommandService
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
}
