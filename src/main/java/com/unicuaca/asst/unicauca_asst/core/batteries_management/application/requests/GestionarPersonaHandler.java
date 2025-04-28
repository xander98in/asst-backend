package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.requests;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.DTORequest.Respuesta;

public interface GestionarPersonaHandler {

    Respuesta consultarPersonPorId(Long idPersona);

}
