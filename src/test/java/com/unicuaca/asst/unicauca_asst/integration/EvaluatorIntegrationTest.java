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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config.JwtService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/seed-evaluator.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EvaluatorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    private static final String BASE_URL = "/asst/reports/evaluators";

    @BeforeEach
    void setUp() {
        token = jwtService.generateAccessToken("profesional@unicauca.edu.co", Set.of("PROFESIONAL_ASST"));
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private String validEvaluatorJson() {
        return """
            {
                "fullName": "ANA MARIA PEREZ LOPEZ",
                "identificationNumber": "1061234567",
                "profession": "Psicologa",
                "postgraduateDegree": "Esp. Seguridad y Salud en el Trabajo",
                "professionalCardNumber": "TP-12345",
                "occupationalHealthLicense": "LIC-98765",
                "licenseIssueDate": "2024-03-15"
            }
            """;
    }

    private ResultActions performPost(String json) throws Exception {
        return mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private ResultActions performPut(Long evaluatorId, String json) throws Exception {
        return mockMvc.perform(put(BASE_URL + "/" + evaluatorId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private ResultActions performDelete(Long evaluatorId) throws Exception {
        return mockMvc.perform(delete(BASE_URL + "/" + evaluatorId)
                .header("Authorization", "Bearer " + token));
    }

    private Long createEvaluator() throws Exception {
        MvcResult result = performPost(validEvaluatorJson())
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }

    // ==================================================================================
    // Creacion de evaluador
    // ==================================================================================

    @Nested
    @DisplayName("Creacion de evaluador")
    class CreateEvaluator {

        @Test
        @DisplayName("Debe retornar 201 cuando el request es valido")
        void should_return201_when_requestIsValid() throws Exception {
            performPost(validEvaluatorJson())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.fullName").value("ANA MARIA PEREZ LOPEZ"))
                    .andExpect(jsonPath("$.data.identificationNumber").value("1061234567"))
                    .andExpect(jsonPath("$.data.profession").value("Psicologa"))
                    .andExpect(jsonPath("$.data.professionalCardNumber").value("TP-12345"))
                    .andExpect(jsonPath("$.data.occupationalHealthLicense").value("LIC-98765"))
                    .andExpect(jsonPath("$.data.licenseIssueDate").value("2024-03-15"));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando los campos obligatorios estan vacios")
        void should_return400_when_requiredFieldsAreEmpty() throws Exception {
            String json = """
                {
                    "fullName": "",
                    "identificationNumber": "",
                    "profession": "",
                    "professionalCardNumber": "",
                    "occupationalHealthLicense": "",
                    "licenseIssueDate": null
                }
                """;

            performPost(json)
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando los campos exceden la longitud maxima")
        void should_return400_when_fieldsExceedMaxLength() throws Exception {
            String json = """
                {
                    "fullName": "%s",
                    "identificationNumber": "1061234567",
                    "profession": "Psicologa",
                    "professionalCardNumber": "TP-12345",
                    "occupationalHealthLicense": "LIC-98765",
                    "licenseIssueDate": "2024-03-15"
                }
                """.formatted("A".repeat(151));

            performPost(json)
                    .andExpect(status().isBadRequest());
        }
    }

    // ==================================================================================
    // Actualizacion de evaluador
    // ==================================================================================

    @Nested
    @DisplayName("Actualizacion de evaluador")
    class UpdateEvaluator {

        @Test
        @DisplayName("Debe retornar 200 cuando el request es valido")
        void should_return200_when_requestIsValid() throws Exception {
            Long evaluatorId = createEvaluator();

            String updatedJson = """
                {
                    "fullName": "CARLOS ANDRES MUNOZ RIVERA",
                    "identificationNumber": "1061234567",
                    "profession": "Psicologo",
                    "postgraduateDegree": "Maestria en Salud Ocupacional",
                    "professionalCardNumber": "TP-12345",
                    "occupationalHealthLicense": "LIC-98765",
                    "licenseIssueDate": "2024-06-20"
                }
                """;

            performPut(evaluatorId, updatedJson)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.fullName").value("CARLOS ANDRES MUNOZ RIVERA"))
                    .andExpect(jsonPath("$.data.profession").value("Psicologo"))
                    .andExpect(jsonPath("$.data.licenseIssueDate").value("2024-06-20"));
        }

        @Test
        @DisplayName("Debe retornar 404 cuando el evaluador no existe")
        void should_return404_when_evaluatorDoesNotExist() throws Exception {
            performPut(99999L, validEvaluatorJson())
                    .andExpect(status().isNotFound());
        }
    }

    // ==================================================================================
    // Eliminacion de evaluador
    // ==================================================================================

    @Nested
    @DisplayName("Eliminacion de evaluador")
    class DeleteEvaluator {

        @Test
        @DisplayName("Debe retornar 200 cuando el evaluador existe y pertenece al usuario")
        void should_return200_when_evaluatorExistsAndBelongsToUser() throws Exception {
            Long evaluatorId = createEvaluator();

            performDelete(evaluatorId)
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 404 cuando el evaluador no existe")
        void should_return404_when_evaluatorDoesNotExist() throws Exception {
            performDelete(99999L)
                    .andExpect(status().isNotFound());
        }
    }
}
