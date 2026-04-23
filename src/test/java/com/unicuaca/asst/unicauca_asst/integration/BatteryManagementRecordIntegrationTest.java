package com.unicuaca.asst.unicauca_asst.integration;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config.JwtService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/seed-battery-record.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BatteryManagementRecordIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() {
        token = jwtService.generateAccessToken("profesional@unicauca.edu.co", Set.of("PROFESIONAL_ASST"));
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private Long createPersonEvaluated() throws Exception {
        String json = """
            {
                "identificationType": "CC",
                "identificationNumber": "1061845213",
                "birthYear": 1990,
                "firstName": "CARLOS ANDRES",
                "lastName": "MUNOZ RIVERA",
                "email": "carlos.munoz@empresa.com"
            }
            """;

        MvcResult result = mockMvc.perform(post("/asst/person-evaluated")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }

    private Long createBatteryRecord(Long personEvaluatedId) throws Exception {
        MvcResult result = mockMvc.perform(post("/asst/battery-management-record/" + personEvaluatedId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }

    // ==================================================================================
    // Creación de registro de batería
    // ==================================================================================

    @Nested
    @DisplayName("Creación de registro de batería")
    class CreateBatteryRecord {

        @Test
        @DisplayName("Debe retornar 201 cuando la persona existe y no tiene registro")
        void should_return201_when_personExistsAndHasNoRecord() throws Exception {
            // Arrange
            Long personId = createPersonEvaluated();

            // Act & Assert
            mockMvc.perform(post("/asst/battery-management-record/" + personId)
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.status").value("Creado"));
        }

        @Test
        @DisplayName("Debe retornar 404 cuando la persona no existe")
        void should_return404_when_personDoesNotExist() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/asst/battery-management-record/99999")
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe retornar 409 cuando la persona ya tiene un registro activo")
        void should_return409_when_personAlreadyHasRecord() throws Exception {
            // Arrange
            Long personId = createPersonEvaluated();
            createBatteryRecord(personId);

            // Act & Assert
            mockMvc.perform(post("/asst/battery-management-record/" + personId)
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isConflict());
        }
    }

    // ==================================================================================
    // Eliminación de registro de batería
    // ==================================================================================

    @Nested
    @DisplayName("Eliminación de registro de batería")
    class DeleteBatteryRecord {

        @Test
        @DisplayName("Debe retornar 200 cuando el registro existe y su estado es Creado")
        void should_return200_when_recordExistsAndStatusIsCreated() throws Exception {
            // Arrange
            Long personId = createPersonEvaluated();
            Long recordId = createBatteryRecord(personId);

            // Act & Assert
            mockMvc.perform(delete("/asst/battery-management-record/" + recordId)
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());
        }

        // Omitido: should_return400_when_recordStatusIsNotCreated
        // Requiere un registro en estado IN_PROCESSING, lo cual implica crear y avanzar
        // cuestionarios. La complejidad de ese setup excede el alcance de este test de integración.

        @Test
        @DisplayName("Debe retornar 404 cuando el registro no existe")
        void should_return404_when_recordDoesNotExist() throws Exception {
            // Act & Assert
            mockMvc.perform(delete("/asst/battery-management-record/99999")
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound());
        }
    }

    // ==================================================================================
    // Cierre de registro de batería
    // ==================================================================================

    @Nested
    @DisplayName("Cierre de registro de batería")
    class CloseBatteryRecord {

        // Omitido: should_return200_when_recordStatusIsCompleted
        // El cierre exitoso requiere crear y completar los 3 cuestionarios (intralaboral,
        // extralaboral, estres) con todas sus respuestas. Validado con pruebas funcionales
        // manuales (CP_HE02).

        @Test
        @DisplayName("Debe retornar 400 cuando el estado del registro no es Diligenciado")
        void should_return400_when_recordStatusIsNotCompleted() throws Exception {
            // Arrange
            Long personId = createPersonEvaluated();
            Long recordId = createBatteryRecord(personId);

            // Act & Assert
            mockMvc.perform(patch("/asst/battery-management-record/close/" + recordId)
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 404 cuando el registro no existe para cerrar")
        void should_return404_when_recordDoesNotExistForClose() throws Exception {
            // Act & Assert
            mockMvc.perform(patch("/asst/battery-management-record/close/99999")
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound());
        }
    }
}
