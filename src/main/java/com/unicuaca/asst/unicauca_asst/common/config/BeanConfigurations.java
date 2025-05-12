package com.unicuaca.asst.unicauca_asst.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.PersonMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query.PersonQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query.PersonQueryHandlerImpl;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services.PersonQueryService;

@Configuration
public class BeanConfigurations {

    @Bean
    PersonQueryService personQueryService(PersonQueryRepository personQueryRepository) {
        return new PersonQueryService(personQueryRepository);
    }

    @Bean
    PersonQueryHandler personQueryHandler(PersonQueryCUInputPort personQueryCUInputPort,
                                        PersonMapper personMapper) {
        return new PersonQueryHandlerImpl(personQueryCUInputPort, personMapper);
    }

}
