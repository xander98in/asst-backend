package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command.PersonCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonCommandController {

    private final PersonCommandHandler personCommandHandler;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PersonResponseDTO>> create(@RequestBody PersonCreateRequestDTO dto) {
        ApiResponse<PersonResponseDTO> response = personCommandHandler.createPerson(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
