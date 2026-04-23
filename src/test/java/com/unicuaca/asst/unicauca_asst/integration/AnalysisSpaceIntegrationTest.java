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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/seed-evaluator.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class AnalysisSpaceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    private static final String BASE_URL = "/asst/reports/analysis-spaces";

    @BeforeEach
    void setUp() {
        token = jwtService.generateAccessToken("profesional@unicauca.edu.co", Set.of("PROFESIONAL_ASST"));
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private Long createEvaluator() throws Exception {
        String json = """
            {
                "fullName": "Maria Fernanda Lopez",
                "identificationNumber": "1061845000",
                "profession": "Psicologa",
                "postgraduateDegree": "Especializacion en Salud Ocupacional",
                "professionalCardNumber": "TP-12345",
                "occupationalHealthLicense": "LIC-67890",
                "licenseIssueDate": "2024-01-15"
            }
            """;

        MvcResult result = mockMvc.perform(post("/asst/reports/evaluators")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }

    private Long createAnalysisSpace(Long evaluatorId) throws Exception {
        String json = """
            {
                "name": "Espacio de prueba",
                "evaluatorId": %d
            }
            """.formatted(evaluatorId);

        MvcResult result = performCreateSpace(json)
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }

    private ResultActions performCreateSpace(String json) throws Exception {
        return mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private ResultActions performDeleteSpace(Long spaceId) throws Exception {
        return mockMvc.perform(delete(BASE_URL + "/" + spaceId)
                .header("Authorization", "Bearer " + token));
    }

    // ==================================================================================
    // Creacion de espacio de analisis
    // ==================================================================================

    @Nested
    @DisplayName("Creacion de espacio de analisis")
    class CreateAnalysisSpace {

        @Test
        @DisplayName("Debe retornar 201 cuando los datos son validos")
        void should_return201_when_validDataProvided() throws Exception {
            Long evaluatorId = createEvaluator();

            String json = """
                {
                    "name": "Espacio de prueba",
                    "evaluatorId": %d
                }
                """.formatted(evaluatorId);

            performCreateSpace(json)
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.name").value("Espacio de prueba"));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando los campos obligatorios estan vacios")
        void should_return400_when_requiredFieldsEmpty() throws Exception {
            String json = """
                {
                    "name": "",
                    "evaluatorId": null
                }
                """;

            performCreateSpace(json)
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 404 cuando el evaluador no existe")
        void should_return404_when_evaluatorNotFound() throws Exception {
            String json = """
                {
                    "name": "Espacio de prueba",
                    "evaluatorId": 99999
                }
                """;

            performCreateSpace(json)
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe retornar 409 cuando el nombre del espacio ya existe")
        void should_return409_when_spaceNameAlreadyExists() throws Exception {
            Long evaluatorId = createEvaluator();

            String json = """
                {
                    "name": "Espacio duplicado",
                    "evaluatorId": %d
                }
                """.formatted(evaluatorId);

            performCreateSpace(json)
                    .andExpect(status().isCreated());

            performCreateSpace(json)
                    .andExpect(status().isConflict());
        }
    }

    // ==================================================================================
    // Eliminacion de espacio de analisis
    // ==================================================================================

    @Nested
    @DisplayName("Eliminacion de espacio de analisis")
    class DeleteAnalysisSpace {

        @Test
        @DisplayName("Debe retornar 200 cuando el espacio existe y pertenece al usuario")
        void should_return200_when_spaceExistsAndBelongsToUser() throws Exception {
            Long evaluatorId = createEvaluator();
            Long spaceId = createAnalysisSpace(evaluatorId);

            performDeleteSpace(spaceId)
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 404 cuando el espacio no existe")
        void should_return404_when_spaceNotFound() throws Exception {
            performDeleteSpace(99999L)
                    .andExpect(status().isNotFound());
        }
    }

    // ==================================================================================
    // Agregar y remover baterias (omitido)
    // ==================================================================================

    // Omitido: tests de addBatteries y removeBattery.
    // Requieren baterias en estado CLOSED, lo cual implica completar el flujo completo
    // de cuestionarios (intralaboral, extralaboral, estres). La complejidad de ese setup
    // excede el alcance de este test de integracion. Estos flujos estan validados por las
    // pruebas funcionales manuales CP_HE11 y CP_HE12.
}
