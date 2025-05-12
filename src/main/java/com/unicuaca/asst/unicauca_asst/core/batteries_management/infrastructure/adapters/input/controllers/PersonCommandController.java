package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query.PersonQueryHandler;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/personas")
@RequiredArgsConstructor
public class PersonCommandController {

    private final PersonQueryHandler personQueryHandler;

    /**
     * Endpoint para consultar la información de una persona por su identificador.
     *
     * @param idPerson identificador único de la persona a consultar.
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene los datos de la persona
     *         en un objeto {@link PersonResponseDTO}, o un mensaje de error si no se encuentra.
     */
    @GetMapping("/consultar/{idPersona}")
    public ResponseEntity<ApiResponse<PersonResponseDTO>> queryPersonById(@PathVariable Long idPersona) {
        ApiResponse<PersonResponseDTO> response = personQueryHandler.getPersonById(idPersona);
        return ResponseEntity.ok(response);
    }
}
