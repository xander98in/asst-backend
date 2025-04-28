package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.Data;

@Data
public class TipoIdentificacion {

    private Long id;
    private String descripcion;

    public TipoIdentificacion(String descripcion) {
        this.descripcion = descripcion;
    }

}
