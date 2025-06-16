package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.PersonEvaluatedRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.PersonEvaluatedPersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida {@link PersonEvaluatedQueryRepository} para la obtención de datos de personas evaluadas.
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
public class PersonEvaluatedQueryRepositoryImpl implements PersonEvaluatedQueryRepository {

    private final PersonEvaluatedRepository personEvaluatedJpaRepository;
    private final PersonEvaluatedPersistenceMapper personEvaluatedBDMapper;

    /**
     * Busca una persona evaluada por su identificador único y la transforma a su representación de dominio.
     *
     * @param id identificador único de la persona
     * @return un {@link Optional} con la persona encontrada o vacío si no existe
     */
    @Override
    public Optional<PersonEvaluated> getPersonEvaluatedById(Long id) {
        return personEvaluatedJpaRepository.findById(id)
            .map(personEvaluatedBDMapper::toDomain);
    }

    /**
     * Verifica si existe una persona evaluada registrada con el ID proporcionado.
     *
     * @param id identificador único de la persona
     * @return true si existe, false en caso contrario
     */
    @Override
    public boolean existsById(Long id) {
        return personEvaluatedJpaRepository.existsById(id);
    }

    /**
     * Verifica si existe una persona evaluada en el sistema con un tipo y número de identificación específicos.
     *
     * @param identificationTypeId ID del tipo de identificación (ej. cédula, pasaporte)
     * @param identificationNumber número de identificación asociado a la persona
     * @return true si existe una persona con esos datos, false en caso contrario
     */
    @Override
    public boolean existsByIdentification(Long identificationTypeId, String identificationNumber) {
        return personEvaluatedJpaRepository.existsByIdentificationTypeIdAndIdentificationNumber(identificationTypeId, identificationNumber);
    }

    /**
     * Verifica si ya existe una persona evaluada registrada con un correo electrónico específico.
     *
     * @param email dirección de correo electrónico a validar
     * @return {@code true} si el correo ya está registrado, {@code false} si no
     */
    @Override
    public boolean existsByEmail(String email) {
        return personEvaluatedJpaRepository.existsByEmail(email);
    }

    /**
     * Verifica si el correo ya está asignado a una persona distinta del ID dado.
     * 
     * @param email correo a verificar
     * @param id ID de la persona actual
     * @return true si el correo pertenece a otra persona, false en caso contrario
     */
    @Override
    public boolean isEmailAssignedToDifferentPerson(String email, Long id) {
        return personEvaluatedJpaRepository.existsByEmailAndIdNot(email, id);
    }
}
