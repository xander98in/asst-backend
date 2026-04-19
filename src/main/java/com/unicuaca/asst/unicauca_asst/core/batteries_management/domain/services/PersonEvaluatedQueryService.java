package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Optional;

/**
 * Servicio de dominio para la consulta de información de personas evaluadas.
 * 
 * <p>Proporciona capacidades de búsqueda por identificador único, identidad (tipo y número) 
 * y listados paginados con soporte para términos de búsqueda general. Implementa i18n
 * para garantizar respuestas consistentes y profesionales.</p>
 */
@RequiredArgsConstructor
public class PersonEvaluatedQueryService implements PersonEvaluatedQueryCUInputPort {

    private final PersonEvaluatedQueryRepository personEvaluatedQueryRepository;
    private final CatalogQueryRepository catalogQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Consulta una persona evaluada por su identificador único.
     *
     * @param id identificador único del registro
     * @return la persona evaluada encontrada
     */
    @Override
    public PersonEvaluated getPersonEvaluatedById(Long id) {
        Optional<PersonEvaluated> personOpt = personEvaluatedQueryRepository.getPersonEvaluatedById(id);
        if (personOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.PERSON_NOT_FOUND,
                "user.person.id_not_found",
                id
            );
        }
        return personOpt.get();
    }

    /**
     * Busca personas evaluadas utilizando su documento de identidad.
     *
     * @param abbreviation abreviatura del tipo de documento (CC, TI, etc.)
     * @param identificationNumber número de documento
     * @param page número de página para la paginación
     * @param size tamaño de la página
     * @return página de resultados con las personas que coinciden con el criterio
     */
    @Override
    public Page<PersonEvaluated> queryByIdentity(String abbreviation, String identificationNumber, Integer page, Integer size) {
        Optional<IdentificationType> idTypeOpt = catalogQueryRepository.getIdTypeByAbbreviation(abbreviation);
        if (idTypeOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.PERSON_ID_TYPE_NOT_FOUND,
                "user.person.id_type_invalid",
                abbreviation
            );
        }
        IdentificationType identificationType = idTypeOpt.get();
        return personEvaluatedQueryRepository.queryByIdentity(identificationType, identificationNumber, page, size, Sort.by(Sort.Direction.DESC, "id"));
    }

    /**
     * Lista personas evaluadas de forma paginada.
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @return una página de personas evaluadas
     */
    @Override
    public Page<PersonEvaluated> listPaginatedPersons(Integer page, Integer size) {
        return personEvaluatedQueryRepository.findAllPaged(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    /**
     * Lista personas evaluadas de forma paginada filtrando por término de búsqueda (identificación, nombre o apellido).
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param searchTerm término de búsqueda
     * @return una página de personas evaluadas
     */
    @Override
    public Page<PersonEvaluated> listPaginatedWithSearchTerm(Integer page, Integer size, String searchTerm) {
        String normalizedTerm = normalizeTerm(searchTerm);
        return personEvaluatedQueryRepository.findAllWithSearchTermPaged(normalizedTerm, page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    /**
     * Limpia y normaliza el término de búsqueda para evitar consultas inconsistentes.
     *
     * @param term término de búsqueda ingresado por el usuario
     * @return el término normalizado (sin espacios al inicio o al final), o {@code null} si es nulo o blanco
     */
    private String normalizeTerm(String term) {
        if (term == null) return null;
        String normalized = term.trim();
        return normalized.isBlank() ? null : normalized;
    }
}
