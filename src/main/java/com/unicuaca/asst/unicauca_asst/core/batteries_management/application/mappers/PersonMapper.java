package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import org.mapstruct.Mapper;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonResponseDTO toResponseDTO(Person person);

}
