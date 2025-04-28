package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.requests.GestionarPersonaHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.DTORequest.Respuesta;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/personas")
@RequiredArgsConstructor
public class PersonCommandController {

    private final GestionarPersonaHandler gestionarPersonaHandler;

    @GetMapping("/consultar/{idPersona}")
    public ResponseEntity<Respuesta> consultarPersonaPorId(Long idPersona) {
        Respuesta respuesta = gestionarPersonaHandler.consultarPersonPorId(idPersona);
        return ResponseEntity.ok(respuesta);
    }

}
