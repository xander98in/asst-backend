package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del caso de uso para operaciones de comando relacionadas con {@link Person}.
 * 
 * Esta clase pertenece a la capa de dominio y orquesta la lógica de negocio para la creación de personas.
 * Valida reglas de negocio como la no duplicidad antes de persistir.
 */
@RequiredArgsConstructor
public class PersonCommandService implements PersonCommandCUInputPort {

    private final PersonCommandRepository personCommandRepository;
    private final PersonQueryRepository personQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Crea una nueva persona si no existe previamente en el sistema.
     *
     * @param person objeto de dominio con los datos de la persona a registrar
     * @return la persona registrada con ID asignado
     * @throws RuntimeException si ya existe una persona con el mismo número y tipo de identificación
     */
    @Override
    public Person createPerson(Person person) {
        if(personQueryRepository.existsByIdentification(person.getIdentificationType().getId(), person.getIdentificationNumber())) {
            this.resultFormatter.throwEntityAlreadyExists("La persona " + person.getFirstName() + " se encuentra registrada.");
        }
        return personCommandRepository.savePerson(person);
    }

}
