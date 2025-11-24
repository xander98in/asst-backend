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

    @Bean
    PersonEvaluatedCommandService personEvaluatedCommandService(PersonEvaluatedCommandRepository personEvaluatedCommandRepository,
                                                                PersonEvaluatedQueryRepository personEvaluatedQueryRepository, CatalogQueryRepository catalogQueryRepository,
                                                                ResultFormatterOutputPort resultFormatterOutputPort) {
        return new PersonEvaluatedCommandService(personEvaluatedCommandRepository, personEvaluatedQueryRepository, catalogQueryRepository, resultFormatterOutputPort);
    }

    @Bean
    BatteryManagementRecordCommandService batteryManagementRecordCommandService(
        BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository,
        BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository,
        PersonEvaluatedQueryRepository personEvaluatedQueryRepository,
        ResultFormatterOutputPort resultFormatterOutputPort) {
        return new BatteryManagementRecordCommandService(batteryManagementRecordCommandRepository,
            batteryManagementRecordQueryRepository, personEvaluatedQueryRepository, resultFormatterOutputPort);
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
        ResultFormatterOutputPort resultFormatterOutputPort
        ) {
        return new PersonEvaluatedDetailsCommandService(
            catalogQueryRepository,
            batteryManagementRecordQueryRepository,
            personEvaluatedDetailsCommandRepository,
            personEvaluatedDetailsQueryRepository,
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
        ResultFormatterOutputPort resultFormatterOutputPort) {
        return new QuestionQueryService(
            questionQueryRepository,
            resultFormatterOutputPort
        );
    }
}
