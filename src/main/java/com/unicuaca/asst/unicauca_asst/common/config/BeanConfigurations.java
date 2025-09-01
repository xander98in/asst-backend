package com.unicuaca.asst.unicauca_asst.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.domain.services.CatalogQueryService;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services.PersonEvaluatedCommandService;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services.PersonEvaluatedQueryService;

@Configuration
public class BeanConfigurations {

    @Bean
    CatalogQueryService catalogQueryService(CatalogQueryRepository catalogQueryRepository) {
        return new CatalogQueryService(catalogQueryRepository);
    }

    @Bean
    PersonEvaluatedQueryService personQueryService(PersonEvaluatedQueryRepository personQueryRepository, ResultFormatterOutputPort resultFormatterOutputPort,
        CatalogQueryRepository catalogQueryRepository) {
        return new PersonEvaluatedQueryService(personQueryRepository, catalogQueryRepository, resultFormatterOutputPort);
    }

    @Bean
    PersonEvaluatedCommandService personEvaluatedCommandService(PersonEvaluatedCommandRepository personEvaluatedCommandRepository, 
            PersonEvaluatedQueryRepository personEvaluatedQueryRepository, ResultFormatterOutputPort resultFormatterOutputPort) {
        return new PersonEvaluatedCommandService(personEvaluatedCommandRepository , personEvaluatedQueryRepository, resultFormatterOutputPort);
    }

}
