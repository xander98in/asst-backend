package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.requests;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.GestionarPersonaCUIntPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.DTORequest.Respuesta;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GestionarPersonaHandlerImpl implements GestionarPersonaHandler {

    private final GestionarPersonaCUIntPort gestionarPersonaCUIntPort;

    @Override
    public Respuesta consultarPersonPorId(Long idPersona) {
        // Implementación del método para consultar persona por ID
        // Aquí puedes agregar la lógica necesaria para realizar la consulta
        return null; // Retorna el resultado de la consulta
    }

}
