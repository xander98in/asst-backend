package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.caseOfUses;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Persona;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.GestionarPersonaCUIntPort;

public class GestionarPersonaCUAdapter implements GestionarPersonaCUIntPort {

    @Override
    public Persona consultarPersonaPorId(Long idPersona) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consultarPersonaPorId'");
    }

    @Override
    public Persona crearPersona(Persona persona) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crearPersona'");
    }

    @Override
    public Persona actualizarPersona(Persona persona) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarPersona'");
    }

    @Override
    public Boolean eliminarPersona(Long idPersona) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarPersona'");
    }

    @Override
    public Boolean existePersonaPorId(Long idPersona) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existePersonaPorId'");
    }

}
