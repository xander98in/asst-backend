package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.query;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.UserStatus;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.UserStatusQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.repositories.UserStatusSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.mappers.UserStatusPersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida {@link UserStatusQueryRepository} para operaciones de consulta.
 *
 * <p>Actúa como adaptador de salida para recuperar información de estados de usuario
 * desde la base de datos.</p>
 */
@RequiredArgsConstructor
@Service
@Transactional
public class UserStatusQueryRepositoryImpl implements UserStatusQueryRepository {

    private final UserStatusSpringJpaRepository userStatusJpaRepository;
    private final UserStatusPersistenceMapper userStatusPersistenceMapper;

    /**
     * Consulta un estado de usuario por su nombre.
     *
     * @param name nombre del estado a buscar
     * @return un {@link Optional} con el estado encontrado o vacío si no existe
     */
    @Override
    public Optional<UserStatus> getUserStatusByName(String name) {
        return userStatusJpaRepository.findByName(name)
            .map(userStatusPersistenceMapper::toDomain);
    }

    /**
     * Consulta un estado de usuario por su identificador único.
     *
     * @param id identificador del estado
     * @return un {@link Optional} con el estado encontrado o vacío si no existe
     */
    @Override
    public Optional<UserStatus> getUserStatusById(Long id) {
        return userStatusJpaRepository.findById(id)
            .map(userStatusPersistenceMapper::toDomain);
    }

    /**
     * Lista todos los estados de usuario disponibles.
     *
     * @return lista de todos los estados de usuario
     */
    @Override
    public List<UserStatus> getAllUserStatuses() {
        return userStatusJpaRepository.findAll().stream()
            .map(userStatusPersistenceMapper::toDomain)
            .toList();
    }
}
