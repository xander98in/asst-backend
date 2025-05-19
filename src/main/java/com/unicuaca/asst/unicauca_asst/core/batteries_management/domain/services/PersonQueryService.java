package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonQueryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PersonQueryService implements PersonQueryCUInputPort {

    private final PersonQueryRepository personQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    @Override
    public Person getPersonById(Long id) {
        return personQueryRepository.getPersonById(id)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound("La persona con ID " + id + " no fue encontrada.");
                return null; // nunca se ejecuta, pero requerido por el compilador
            });
    }

}
