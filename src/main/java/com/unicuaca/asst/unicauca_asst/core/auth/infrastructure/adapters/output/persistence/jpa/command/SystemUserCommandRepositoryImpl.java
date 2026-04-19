package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.command;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.SystemUserCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.entities.RoleEntity;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.entities.SystemUserEntity;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.repositories.RoleSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.repositories.SystemUserSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.repositories.UserStatusSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.mappers.SystemUserPersistenceMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.PersonEvaluatedSpringJpaRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida {@link SystemUserCommandRepository} para operaciones de escritura.
 *
 * <p>Actúa como adaptador de salida para persistir usuarios del sistema en la base de datos.
 * Las relaciones (status, roles, personEvaluated) se gestionan manualmente mediante
 * referencias JPA ({@code getReferenceById}).</p>
 */
@RequiredArgsConstructor
@Service
public class SystemUserCommandRepositoryImpl implements SystemUserCommandRepository {

    private final SystemUserSpringJpaRepository systemUserJpaRepository;
    private final RoleSpringJpaRepository roleJpaRepository;
    private final UserStatusSpringJpaRepository userStatusJpaRepository;
    private final PersonEvaluatedSpringJpaRepository personEvaluatedJpaRepository;
    private final SystemUserPersistenceMapper systemUserPersistenceMapper;

    /**
     * Persiste un nuevo usuario del sistema.
     *
     * <p>Convierte el modelo de dominio a entidad, asigna referencias JPA
     * para status, roles y personEvaluated, y guarda.</p>
     *
     * @param systemUser objeto de dominio a persistir
     * @return el usuario persistido con relaciones cargadas
     */
    @Override
    public Optional<SystemUser> saveSystemUser(SystemUser systemUser) {
        SystemUserEntity entity = systemUserPersistenceMapper.toEntity(systemUser);

        // Asignar referencia de estado
        if (systemUser.getStatus() != null) {
            entity.setStatus(userStatusJpaRepository.getReferenceById(systemUser.getStatus().getId()));
        }

        // Asignar referencias de roles
        if (systemUser.getRoles() != null && !systemUser.getRoles().isEmpty()) {
            Set<RoleEntity> roleEntities = systemUser.getRoles().stream()
                .map(role -> roleJpaRepository.getReferenceById(role.getId()))
                .collect(Collectors.toSet());
            entity.setRoles(roleEntities);
        }

        // Asignar referencia de persona evaluada
        if (systemUser.getPersonEvaluatedId() != null) {
            entity.setPersonEvaluated(personEvaluatedJpaRepository.getReferenceById(systemUser.getPersonEvaluatedId()));
        }

        SystemUserEntity saved = systemUserJpaRepository.save(entity);
        return systemUserJpaRepository.findWithRelationsById(saved.getId())
            .map(systemUserPersistenceMapper::toDomain);
    }

    /**
     * Actualiza un usuario del sistema existente.
     *
     * <p>Carga la entidad, modifica solo los campos proporcionados y guarda.</p>
     *
     * @param systemUser objeto de dominio con datos actualizados
     * @return el usuario actualizado con relaciones cargadas
     */
    @Override
    public Optional<SystemUser> updateSystemUser(SystemUser systemUser) {
        Optional<SystemUserEntity> entityOpt = systemUserJpaRepository.findById(systemUser.getId());

        if (entityOpt.isEmpty()) {
            return Optional.empty();
        }

        SystemUserEntity entity = entityOpt.get();

        // Actualizar campos básicos (solo si están presentes)
        if (systemUser.getEmail() != null) {
            entity.setEmail(systemUser.getEmail());
        }
        if (systemUser.getUsername() != null) {
            entity.setUsername(systemUser.getUsername());
        }
        if (systemUser.getFullName() != null) {
            entity.setFullName(systemUser.getFullName());
        }

        // Actualizar estado (presente en flujo de cambio de estado)
        if (systemUser.getStatus() != null) {
            entity.setStatus(userStatusJpaRepository.getReferenceById(systemUser.getStatus().getId()));
        }

        // Actualizar roles (presente en flujo de actualización)
        if (systemUser.getRoles() != null && !systemUser.getRoles().isEmpty()) {
            Set<RoleEntity> roleEntities = systemUser.getRoles().stream()
                .map(role -> roleJpaRepository.getReferenceById(role.getId()))
                .collect(Collectors.toSet());
            entity.setRoles(roleEntities);
        }

        // Actualizar referencia de persona evaluada
        if (systemUser.getPersonEvaluatedId() != null) {
            entity.setPersonEvaluated(personEvaluatedJpaRepository.getReferenceById(systemUser.getPersonEvaluatedId()));
        }

        SystemUserEntity saved = systemUserJpaRepository.save(entity);
        return systemUserJpaRepository.findWithRelationsById(saved.getId())
            .map(systemUserPersistenceMapper::toDomain);
    }

    /**
     * Elimina un usuario del sistema por su ID.
     *
     * @param id identificador del usuario a eliminar
     */
    @Override
    public void deleteSystemUserById(Long id) {
        systemUserJpaRepository.deleteById(id);
    }
}
