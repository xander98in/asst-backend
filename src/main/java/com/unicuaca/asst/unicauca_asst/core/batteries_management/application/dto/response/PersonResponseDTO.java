package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponseDTO {

    private Long id;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private String identificationType;
    private String gender;
    
}
