package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.command;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories.IdentificationTypeSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEvaluatedEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.PersonEvaluatedSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.PersonEvaluatedPersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida {@link PersonEvaluatedCommandRepository} para operaciones de escritura.
 *
 * Esta clase actúa como adaptador de salida para guardar entidades de persona en la base de datos.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class PersonCommandRepositoryImpl implements PersonEvaluatedCommandRepository {

    private final PersonEvaluatedSpringJpaRepository personEvaluatedJpaRepository;
    private final PersonEvaluatedPersistenceMapper personEvaluatedDBMapper;
    private final IdentificationTypeSpringJpaRepository identificationTypeRepository;
    
    /**
     * Guarda una entidad {@link PersonEvaluated} en la base de datos y la retorna si fue persistida exitosamente.
     * Se realiza una consulta posterior para recuperar las relaciones cargadas completamente.
     *
     * @param person el objeto del dominio a persistir
     * @return un {@link Optional} con el objeto persistido o vacío si algo falla
     */
    @Override
    public Optional<PersonEvaluated> savePersonEvaluated(PersonEvaluated person) {
        PersonEvaluatedEntity entity = personEvaluatedDBMapper.toEntity(person);
        PersonEvaluatedEntity saved = personEvaluatedJpaRepository.save(entity);
        return personEvaluatedJpaRepository.findWithRelationsById(saved.getId())
            .map(personEvaluatedDBMapper::toDomain);
    }

    /**
     * Implementación de la actualización de persona evaluada.
     * 
     * Carga la entidad, modifica solo los campos permitidos y guarda los cambios.
     *
     * @param personEvaluated objeto de dominio con datos nuevos
     * @return objeto de dominio actualizado
     */
    @Override
    public Optional<PersonEvaluated> updatePersonEvaluated(PersonEvaluated personEvaluated) {
        Optional<PersonEvaluatedEntity> entityOpt = personEvaluatedJpaRepository.findById(personEvaluated.getId());

        if (entityOpt.isEmpty()) {
            return Optional.empty();
        }

        PersonEvaluatedEntity entity = entityOpt.get();
        // Solo se actualizan campos permitidos
        entity.setIdentificationType(identificationTypeRepository.getReferenceById(personEvaluated.getIdentificationType().getId()));
        entity.setIdentificationNumber(personEvaluated.getIdentificationNumber());
        entity.setFirstName(personEvaluated.getFirstName());
        entity.setLastName(personEvaluated.getLastName());
        entity.setBirthYear(personEvaluated.getBirthYear());
        entity.setEmail(personEvaluated.getEmail());

        PersonEvaluatedEntity saved = personEvaluatedJpaRepository.save(entity);
        return Optional.of(personEvaluatedDBMapper.toDomain(saved));
    }
}
