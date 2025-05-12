package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import org.springframework.stereotype.Service;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonaEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.PersonaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class PersonQueryRepositoryImpl implements PersonQueryRepository {

    private final PersonaRepository personaJpaRepository;

    @Override
    public Person getById(Long id) {
        PersonaEntity entity = personaJpaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + id));

        return new Person(
            entity.getId(),
            entity.getNumeroIdentificacion(),
            entity.getNombres(),
            entity.getApellidos(),
            entity.getAnioNacimiento(),
            entity.getTipoIdentificacion().getDescripcion(),
            entity.getSexo().getDescripcion()
        );
    }

}
