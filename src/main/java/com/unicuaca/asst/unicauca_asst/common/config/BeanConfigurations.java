package com.unicuaca.asst.unicauca_asst.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services.PersonQueryService;

@Configuration
public class BeanConfigurations {

    @Bean
    PersonQueryService personQueryService(PersonQueryRepository personQueryRepository, ResultFormatterOutputPort resultFormatterOutputPort) {
        return new PersonQueryService(personQueryRepository, resultFormatterOutputPort);
    }

}
