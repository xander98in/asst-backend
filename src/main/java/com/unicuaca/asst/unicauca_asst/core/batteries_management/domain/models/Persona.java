package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.Data;

@Data
public class Persona {

    private Long id;
    private TipoIdentificacion tipoIdentificacion;
    private String numeroIdentificacion;
    private String nombres;
    private String apellidos;
    private Sexo sexo;
    private String fechaNacimiento;

    public Persona(TipoIdentificacion tipoIdentificacion, String numeroIdentificacion, String nombres, String apellidos  , Sexo sexo, String fechaNacimiento) {
        this.tipoIdentificacion = tipoIdentificacion;        
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
    }
}
