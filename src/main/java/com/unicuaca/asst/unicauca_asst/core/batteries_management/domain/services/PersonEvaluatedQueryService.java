package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedQueryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PersonEvaluatedQueryService implements PersonEvaluatedQueryCUInputPort {

    private final PersonEvaluatedQueryRepository personEvaluatedQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    @Override
    public PersonEvaluated getPersonEvaluatedById(Long id) {
        return personEvaluatedQueryRepository.getPersonEvaluatedById(id)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound("La persona con ID " + id + " no fue encontrada.");
                return null; // nunca se ejecuta, pero requerido por el compilador
            });
    }

}
