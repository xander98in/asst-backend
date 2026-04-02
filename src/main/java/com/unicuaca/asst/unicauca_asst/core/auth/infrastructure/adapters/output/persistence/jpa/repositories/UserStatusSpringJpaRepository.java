package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.entities.UserStatusEntity;

/**
 * Repositorio JPA para la entidad {@link UserStatusEntity}.
 *
 * <p>Define operaciones de acceso a datos para la tabla {@code estados_usuario}.</p>
 */
public interface UserStatusSpringJpaRepository extends JpaRepository<UserStatusEntity, Long> {

    /**
     * Busca un estado de usuario por su nombre.
     *
     * @param name nombre del estado
     * @return un {@link Optional} con el estado encontrado o vacío si no existe
     */
    Optional<UserStatusEntity> findByName(String name);
}
