package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Persona;

public interface GestionarPersonaCUIntPort {

    Persona consultarPersonaPorId(Long idPersona);

    Persona crearPersona(Persona persona);

    Persona actualizarPersona(Persona persona);

    Boolean eliminarPersona(Long idPersona);

    Boolean existePersonaPorId(Long idPersona);
}
