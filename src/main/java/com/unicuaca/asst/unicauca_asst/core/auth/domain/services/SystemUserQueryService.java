package com.unicuaca.asst.unicauca_asst.core.auth.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input.SystemUserQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.SystemUserQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la consulta de información de usuarios del sistema.
 *
 * <p>Proporciona capacidades de búsqueda por identificador único, correo electrónico
 * y listados paginados con soporte para términos de búsqueda general.</p>
 */
@RequiredArgsConstructor
public class SystemUserQueryService implements SystemUserQueryCUInputPort {

    private final SystemUserQueryRepository systemUserQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Consulta un usuario del sistema por su identificador único.
     *
     * @param id identificador único del usuario
     * @return el usuario encontrado
     */
    @Override
    public SystemUser getSystemUserById(Long id) {
        return systemUserQueryRepository.getSystemUserById(id)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.USER_NOT_FOUND,
                    "user.user.not_found",
                    id
                );
                return null;
            });
    }

    /**
     * Consulta un usuario del sistema por su correo electrónico.
     *
     * @param email correo electrónico del usuario
     * @return el usuario encontrado
     */
    @Override
    public SystemUser getSystemUserByEmail(String email) {
        return systemUserQueryRepository.getSystemUserByEmail(email)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.USER_NOT_FOUND,
                    "user.user.not_found",
                    email
                );
                return null;
            });
    }

    /**
     * Lista usuarios del sistema de forma paginada.
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @return una página de usuarios del sistema
     */
    @Override
    public Page<SystemUser> listPaginatedUsers(Integer page, Integer size) {
        return systemUserQueryRepository.findAllPaged(page, size, Sort.by("createdAt").descending());
    }

    /**
     * Lista usuarios del sistema de forma paginada filtrando por término de búsqueda.
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param searchTerm término de búsqueda
     * @return una página de usuarios del sistema
     */
    @Override
    public Page<SystemUser> listPaginatedWithSearchTerm(Integer page, Integer size, String searchTerm) {
        String normalizedTerm = normalizeTerm(searchTerm);
        return systemUserQueryRepository.findAllWithSearchTermPaged(normalizedTerm, page, size, Sort.by("createdAt").descending());
    }

    /**
     * Limpia y normaliza el término de búsqueda para evitar consultas inconsistentes.
     */
    private String normalizeTerm(String term) {
        if (term == null) return null;
        String normalized = term.trim();
        return normalized.isBlank() ? null : normalized;
    }
}
