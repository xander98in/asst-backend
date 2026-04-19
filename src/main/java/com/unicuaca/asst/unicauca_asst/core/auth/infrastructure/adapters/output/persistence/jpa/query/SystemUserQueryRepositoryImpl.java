package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.query;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.SystemUserQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.repositories.SystemUserSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.mappers.SystemUserPersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida {@link SystemUserQueryRepository} para operaciones de consulta.
 *
 * <p>Actúa como adaptador de salida en la arquitectura hexagonal, delegando las consultas
 * al repositorio JPA y convirtiendo las entidades a modelos de dominio.</p>
 */
@RequiredArgsConstructor
@Service
public class SystemUserQueryRepositoryImpl implements SystemUserQueryRepository {

    private final SystemUserSpringJpaRepository systemUserJpaRepository;
    private final SystemUserPersistenceMapper systemUserPersistenceMapper;

    /**
     * Consulta un usuario por su ID con todas sus relaciones cargadas.
     *
     * @param id identificador del usuario
     * @return un {@link Optional} con el usuario o vacío si no existe
     */
    @Override
    public Optional<SystemUser> getSystemUserById(Long id) {
        return systemUserJpaRepository.findWithRelationsById(id)
            .map(systemUserPersistenceMapper::toDomain);
    }

    /**
     * Consulta un usuario por su correo electrónico con todas sus relaciones cargadas.
     *
     * @param email correo electrónico del usuario
     * @return un {@link Optional} con el usuario o vacío si no existe
     */
    @Override
    public Optional<SystemUser> getSystemUserByEmail(String email) {
        return systemUserJpaRepository.findWithRelationsByEmail(email)
            .map(systemUserPersistenceMapper::toDomain);
    }

    /**
     * Verifica si existe un usuario con el correo electrónico proporcionado.
     *
     * @param email correo electrónico a verificar
     * @return {@code true} si ya existe un usuario con ese correo
     */
    @Override
    public boolean existsByEmail(String email) {
        return systemUserJpaRepository.existsByEmail(email);
    }

    /**
     * Verifica si existe un usuario con el ID proporcionado.
     *
     * @param id identificador del usuario
     * @return {@code true} si el usuario existe
     */
    @Override
    public boolean existsById(Long id) {
        return systemUserJpaRepository.existsById(id);
    }

    /**
     * Verifica si el correo ya está asignado a un usuario distinto del ID dado.
     *
     * @param email correo a verificar
     * @param id    ID del usuario actual
     * @return {@code true} si el correo pertenece a otro usuario
     */
    @Override
    public boolean isEmailAssignedToDifferentUser(String email, Long id) {
        return systemUserJpaRepository.existsByEmailAndIdNot(email, id);
    }

    /**
     * Verifica si existe un usuario con el nombre de usuario proporcionado.
     *
     * @param username nombre de usuario a verificar
     * @return {@code true} si ya hay un usuario registrado con ese username, {@code false} si no
     */
    @Override
    public boolean existsByUsername(String username) {
        return systemUserJpaRepository.existsByUsername(username);
    }

    /**
     * Verifica si el nombre de usuario ya está asignado a un usuario distinto del ID dado.
     *
     * @param username nombre de usuario a verificar
     * @param id       ID del usuario actual
     * @return {@code true} si el username pertenece a otro usuario, {@code false} en caso contrario
     */
    @Override
    public boolean isUsernameAssignedToDifferentUser(String username, Long id) {
        return systemUserJpaRepository.existsByUsernameAndIdNot(username, id);
    }

    /**
     * Lista todos los usuarios de forma paginada con la relación status cargada.
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param sort criterio de ordenamiento
     * @return una página de usuarios del sistema
     */
    @Override
    public Page<SystemUser> findAllPaged(Integer page, Integer size, Sort sort) {
        return systemUserJpaRepository.findAllWithRelations(PageRequest.of(page, size, sort))
            .map(systemUserPersistenceMapper::toDomain);
    }

    /**
     * Lista usuarios de forma paginada filtrando por término de búsqueda.
     *
     * @param searchTerm término de búsqueda
     * @param page       número de página (0-indexado)
     * @param size       cantidad de registros por página
     * @param sort       criterio de ordenamiento
     * @return una página de usuarios filtrados
     */
    @Override
    public Page<SystemUser> findAllWithSearchTermPaged(String searchTerm, Integer page, Integer size, Sort sort) {
        return systemUserJpaRepository.findAllWithSearchTerm(searchTerm, PageRequest.of(page, size, sort))
            .map(systemUserPersistenceMapper::toDomain);
    }
}
