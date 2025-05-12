package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponseDTO {
    private Long id;
    private String numeroIdentificacion;
    private String nombres;
    private String apellidos;
    private Integer anioNacimiento;
    private String tipoIdentificacion;
    private String sexo;
}
