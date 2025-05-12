package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Person {

    private Long id;
    private String numeroIdentificacion;
    private String nombres;
    private String apellidos;
    private Integer anioNacimiento;
    private String tipoIdentificacion;
    private String sexo;

}
