package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command.PersonCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST que expone los endpoints relacionados con las operaciones de comando
 * sobre la entidad {@code Person}. Forma parte de la capa de entrada en la arquitectura hexagonal.
 * 
 * <p>Este controlador se encarga de recibir las solicitudes HTTP, delegar la lógica de negocio
 * al {@link PersonCommandHandler} y devolver las respuestas adecuadas.</p>
 * 
 * <p>La creación de personas es la funcionalidad principal actualmente expuesta.</p>
 */
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonCommandController {

    private final PersonCommandHandler personCommandHandler;

    /**
     * Endpoint para crear una nueva persona.
     *
     * @param dto Objeto con los datos necesarios para crear una persona.
     * @return {@code ResponseEntity} con el resultado de la operación, envuelto en un {@link ApiResponse}.
     *
     * <p>HTTP Status esperado: {@code 201 Created} si la operación fue exitosa.</p>
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PersonResponseDTO>> create(@Valid @RequestBody PersonCreateRequestDTO dto) {
        ApiResponse<PersonResponseDTO> response = personCommandHandler.createPerson(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
