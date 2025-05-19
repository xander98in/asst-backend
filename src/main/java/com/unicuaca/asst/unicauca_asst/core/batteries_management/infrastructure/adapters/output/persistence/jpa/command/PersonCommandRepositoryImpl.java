package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.PersonRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.PersonPersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida {@link PersonCommandRepository} para operaciones de escritura.
 *
 * Esta clase actúa como adaptador de salida para guardar entidades de persona en la base de datos.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class PersonCommandRepositoryImpl implements PersonCommandRepository {

    private final PersonRepository personJpaRepository;
    private final PersonPersistenceMapper personDBMapper;

    /**
     * Guarda una entidad {@link Person} en la base de datos.
     *
     * @param person el objeto del dominio a persistir
     * @return el objeto persistido con su ID generado
     */
    @Override
    public Person savePerson(Person person) {
        PersonEntity entity = personDBMapper.toEntity(person);
        PersonEntity saved = personJpaRepository.save(entity);
        return personDBMapper.toDomain(saved);
    }
}
