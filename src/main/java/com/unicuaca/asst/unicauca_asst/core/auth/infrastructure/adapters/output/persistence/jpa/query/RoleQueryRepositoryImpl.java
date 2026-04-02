package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.query;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.Role;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.RoleQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.repositories.RoleSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.mappers.RolePersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida {@link RoleQueryRepository} para operaciones de consulta.
 *
 * <p>Actúa como adaptador de salida para recuperar información de roles
 * desde la base de datos.</p>
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RoleQueryRepositoryImpl implements RoleQueryRepository {

    private final RoleSpringJpaRepository roleJpaRepository;
    private final RolePersistenceMapper rolePersistenceMapper;

    /**
     * Consulta un rol por su clave técnica.
     *
     * @param keyName clave técnica del rol
     * @return un {@link Optional} con el rol encontrado o vacío si no existe
     */
    @Override
    public Optional<Role> getRoleByKeyName(String keyName) {
        return roleJpaRepository.findByKeyName(keyName)
            .map(rolePersistenceMapper::toDomain);
    }

    /**
     * Consulta un rol por su identificador único.
     *
     * @param id identificador del rol
     * @return un {@link Optional} con el rol encontrado o vacío si no existe
     */
    @Override
    public Optional<Role> getRoleById(Long id) {
        return roleJpaRepository.findById(id)
            .map(rolePersistenceMapper::toDomain);
    }

    /**
     * Lista todos los roles disponibles.
     *
     * @return lista de todos los roles
     */
    @Override
    public List<Role> getAllRoles() {
        return roleJpaRepository.findAll().stream()
            .map(rolePersistenceMapper::toDomain)
            .toList();
    }
}
