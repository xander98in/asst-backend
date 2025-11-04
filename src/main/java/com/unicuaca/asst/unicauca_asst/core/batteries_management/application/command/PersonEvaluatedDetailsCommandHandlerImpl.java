package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedDetailsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del manejador de comandos para PersonEvaluatedDetails.
 *
 * Nota: Esta clase es una "impresión" sin lógica de negocio aún.
 * Solo deja trazas y marca el método como no implementado.
 */
@RequiredArgsConstructor
@Component
@Transactional
public class PersonEvaluatedDetailsCommandHandlerImpl implements PersonEvaluatedDetailsCommandHandler {

    @Override
    public PersonEvaluatedDetailsResponseDTO createPersonEvaluatedDetails(PersonEvaluatedDetailsCreateRequestDTO dto) {
        System.out.println("Creando person evaluated details");
        System.out.println("Datos recibidos: " + dto);
        return null;
    }
}
