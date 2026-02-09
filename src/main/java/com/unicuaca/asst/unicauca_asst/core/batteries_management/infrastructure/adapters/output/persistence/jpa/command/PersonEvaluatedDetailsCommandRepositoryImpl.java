package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.command;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedDetailsCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEvaluatedDetailsEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.PersonEvaluatedDetailsSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.PersonEvaluatedDetailsPersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida para operaciones de persistencia
 * relacionadas con los detalles de una persona evaluada (PersonEvaluatedDetails).
 *
 * <p>Esta clase utiliza Spring Data JPA para interactuar con la base de datos,
 * implementando los métodos definidos en la interfaz {@link PersonEvaluatedDetailsCommandRepository}.</p>
 */
@RequiredArgsConstructor
@Service
public class PersonEvaluatedDetailsCommandRepositoryImpl implements PersonEvaluatedDetailsCommandRepository {

    private final PersonEvaluatedDetailsSpringJpaRepository personEvaluatedDetailsJpaRepository;
    private final PersonEvaluatedDetailsPersistenceMapper personEvaluatedDetailsMapper;

    /** 
     * Guarda los detalles de una persona evaluada.
     *
     * @param personEvaluatedDetails Los detalles de la persona evaluada a guardar.
     * @return Un Optional que contiene los detalles guardados de la persona evaluada, o vacío si no se pudo guardar.
     */
    @Override
    public Optional<PersonEvaluatedDetails> savePersonEvaluatedDetails(PersonEvaluatedDetails personEvaluatedDetails) {
        PersonEvaluatedDetailsEntity entity = personEvaluatedDetailsMapper.toEntity(personEvaluatedDetails);
        PersonEvaluatedDetailsEntity saved = personEvaluatedDetailsJpaRepository.save(entity);
        return personEvaluatedDetailsJpaRepository.findByIdWithAll(saved.getId())
                .map(personEvaluatedDetailsMapper::toDomain);
    }

    /**
     * Actualiza los detalles de una persona evaluada.
     *
     * @param id ID de los detalles de la persona evaluada a actualizar.
     * @param personEvaluatedDetails Los nuevos detalles de la persona evaluada.
     * @return Un Optional que contiene los detalles actualizados de la persona evaluada, o vacío si no se pudo actualizar.
     */
    @Override
    public Optional<PersonEvaluatedDetails> updatePersonEvaluatedDetails(Long id, PersonEvaluatedDetails personEvaluatedDetails) {
        personEvaluatedDetails.setId(id);

        PersonEvaluatedDetailsEntity entity = personEvaluatedDetailsMapper.toEntity(personEvaluatedDetails);
        PersonEvaluatedDetailsEntity saved = personEvaluatedDetailsJpaRepository.save(entity);

        return personEvaluatedDetailsJpaRepository.findByIdWithAll(saved.getId())
            .map(personEvaluatedDetailsMapper::toDomain);
    }

    /**
     * Elimina los detalles de una persona evaluada por su ID.
     *
     * @param id ID del detalle a eliminar
     */
    @Override
    public void deleteById(Long id) {
        this.personEvaluatedDetailsJpaRepository.deleteById(id);
    }

}
