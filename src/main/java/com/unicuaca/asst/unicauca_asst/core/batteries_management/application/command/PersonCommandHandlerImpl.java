package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import org.springframework.stereotype.Component;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.PersonMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonCommandCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del manejador de comandos para la entidad {@code Person}.
 *
 * Esta clase pertenece a la capa de aplicación y actúa como intermediario entre los controladores
 * (adaptadores de entrada) y los casos de uso definidos en el dominio. Su función es orquestar la creación
 * de personas transformando los datos de entrada (DTO) en objetos del dominio, y viceversa para la salida.
 *
 * <p>Hace uso de {@link PersonCommandCUInputPort} para delegar la lógica de negocio y de {@link PersonMapper}
 * para las conversiones entre DTO y modelo de dominio.</p>
 */
@RequiredArgsConstructor
@Component
public class PersonCommandHandlerImpl implements PersonCommandHandler {

    private final PersonCommandCUInputPort personCommandCUInputPort;
    private final PersonMapper personMapper;

    /**
     * Crea una nueva persona en el sistema utilizando los datos recibidos desde la capa de presentación.
     *
     * @param dto DTO con los datos necesarios para crear una persona
     * @return una respuesta exitosa con el DTO de la persona creada
     */
    @Override
    public ApiResponse<PersonResponseDTO> createPerson(PersonCreateRequestDTO dto) {
        Person domain = personMapper.toDomain(dto);
        Person created = personCommandCUInputPort.createPerson(domain);
        PersonResponseDTO response = personMapper.toResponseDTO(created);
        return ApiResponse.success("Persona creada exitosamente", response);
    }
}
