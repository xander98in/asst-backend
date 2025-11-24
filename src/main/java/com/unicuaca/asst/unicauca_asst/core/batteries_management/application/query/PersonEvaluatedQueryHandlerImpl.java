package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedInformationListResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.PersonEvaluatedMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedQueryCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del manejador de consultas de persona.
 *
 * Delega la lógica de negocio al puerto de entrada y transforma
 * el modelo de dominio en un DTO para la respuesta.
 */
@RequiredArgsConstructor
@Component
public class PersonEvaluatedQueryHandlerImpl implements PersonEvaluatedQueryHandler {

    private final PersonEvaluatedQueryCUInputPort personEvaluatedQueryCUInputPort;
    private final PersonEvaluatedMapper personEvaluatedMapper;

    /**
     * Consulta una persona por su ID y devuelve una respuesta formateada.
     *
     * @param idPerson ID único de la persona
     * @return respuesta con los datos de la persona consultada
     */
    @Override
    public PersonEvaluatedResponseDTO getPersonEvaluatedById(Long idPerson) {
        PersonEvaluated person = personEvaluatedQueryCUInputPort.getPersonEvaluatedById(idPerson);
        return personEvaluatedMapper.toResponseDTO(person);
    }

    /**
     * Consulta personas evaluadas por tipo y número de identificación con paginación.
     *
     * @param abbreviation          la abreviatura del tipo de identificación
     * @param identificationNumber  el número de identificación
     * @param page                  el número de página (0-indexado)
     * @param size                  la cantidad de registros por página
     * @return una {@link Page} que contiene una lista paginada de {@link PersonEvaluatedInformationListResponseDTO}
     *         que coinciden con los criterios de búsqueda, o un mensaje de error si no se encuentran resultados.
     */
    @Override
    public Page<PersonEvaluatedInformationListResponseDTO> queryByIdentity(String abbreviation, String identificationNumber, Integer page, Integer size) {
        Page<PersonEvaluated> personEvaluatedPage = personEvaluatedQueryCUInputPort.queryByIdentity(abbreviation, identificationNumber, page, size);
        List<PersonEvaluatedInformationListResponseDTO> dtoList = personEvaluatedPage.stream()
            .map(personEvaluatedMapper::toInformationListResponseDTO)
            .toList();
        return new PageImpl<>(dtoList, personEvaluatedPage.getPageable(), personEvaluatedPage.getTotalElements());
    }

}
