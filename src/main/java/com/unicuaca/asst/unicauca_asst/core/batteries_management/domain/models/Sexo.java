package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.Data;

@Data
public class Sexo {

    private Long id;
    private String descripcion;

    public Sexo(String descripcion) {
        this.descripcion = descripcion;
    }
}
