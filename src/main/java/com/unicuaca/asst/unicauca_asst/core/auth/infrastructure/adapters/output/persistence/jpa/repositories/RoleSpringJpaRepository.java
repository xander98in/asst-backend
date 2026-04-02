package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.entities.RoleEntity;

/**
 * Repositorio JPA para la entidad {@link RoleEntity}.
 *
 * <p>Define operaciones de acceso a datos para la tabla {@code roles}.</p>
 */
public interface RoleSpringJpaRepository extends JpaRepository<RoleEntity, Long> {

    /**
     * Busca un rol por su clave técnica.
     *
     * @param keyName clave técnica del rol
     * @return un {@link Optional} con el rol encontrado o vacío si no existe
     */
    Optional<RoleEntity> findByKeyName(String keyName);
}
