package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.PersonRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.PersonPersistenceMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida {@link PersonQueryRepository} para la obtención de datos de personas.
 *
 * Esta clase actúa como adaptador de salida en la arquitectura hexagonal y utiliza un repositorio JPA
 * para acceder a la base de datos, junto con un mapper que convierte entre entidades persistentes
 * y modelos del dominio.
 *
 * <p>Las operaciones definidas aquí son de solo lectura (consultas), y no incluyen lógica de negocio.</p>
 */
@RequiredArgsConstructor
@Service
@Transactional
public class PersonQueryRepositoryImpl implements PersonQueryRepository {

    /**
     * Repositorio JPA para acceder a la tabla "personas".
     */
    private final PersonRepository personJpaRepository;

    /**
     * Mapper para transformar entre {@link PersonEntity} y {@link Person}.
     */
    private final PersonPersistenceMapper personBDMapper;

    /**
     * Busca una persona por su identificador único y la transforma a su representación de dominio.
     *
     * @param id identificador único de la persona
     * @return un {@link Optional} con la persona encontrada o vacío si no existe
     */
    @Override
    public Optional<Person> getPersonById(Long id) {
        return personJpaRepository.findById(id)
            .map(personBDMapper::toDomain);
    }

    /**
     * Verifica si existe una persona registrada con el ID proporcionado.
     *
     * @param id identificador único de la persona
     * @return true si existe, false en caso contrario
     */
    @Override
    public boolean existsById(Long id) {
        return personJpaRepository.existsById(id);
    }
}
