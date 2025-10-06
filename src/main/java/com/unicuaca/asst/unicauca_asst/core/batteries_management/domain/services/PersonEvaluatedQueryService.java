package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedQueryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PersonEvaluatedQueryService implements PersonEvaluatedQueryCUInputPort {

    private final PersonEvaluatedQueryRepository personEvaluatedQueryRepository;
    private final CatalogQueryRepository catalogQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    @Override
    public PersonEvaluated getPersonEvaluatedById(Long id) {
        return personEvaluatedQueryRepository.getPersonEvaluatedById(id)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La persona con ID " + id + " no fue encontrada.")
                );
                return null; // nunca se ejecuta, pero requerido por el compilador
            });
    }

    /**
     * Consulta una lista paginada de personas evaluadas por su identidad.
     *
     * @param abbreviation       la abreviatura del tipo de identificación
     * @param identificationNumber el número de identificación
     * @param page               el número de página
     * @param size               el tamaño de la página
     * @return una página de información de personas evaluadas
     */
    @Override
    public Page<PersonEvaluated> queryByIdentity(String abbreviation, String identificationNumber, Integer page, Integer size) {

        IdentificationType identificationType = catalogQueryRepository.getIdTypeByAbbreviation(abbreviation)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El tipo de identificación con abreviatura " + abbreviation + " no fue encontrado.")
                );
                return null;
            });
        return personEvaluatedQueryRepository.queryByIdentity(identificationType, identificationNumber, page, size, Sort.by(Sort.Direction.DESC, "id"));
    }

}
