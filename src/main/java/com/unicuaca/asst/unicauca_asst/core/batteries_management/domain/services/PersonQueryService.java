package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonQueryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PersonQueryService implements PersonQueryCUInputPort {

    /**
     * Repositorio de consulta de personas.
     */
    private final PersonQueryRepository personQueryRepository;

    @Override
    public Person getById(Long id) {
        return personQueryRepository.getById(id);
    }

}
