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

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/seed-queries.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class PaginatedQueriesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private String tokenAdmin;
    private String tokenProfesional;

    @BeforeEach
    void setUp() {
        tokenAdmin = jwtService.generateAccessToken("admin@unicauca.edu.co", Set.of("ADMIN"));
        tokenProfesional = jwtService.generateAccessToken("admin@unicauca.edu.co", Set.of("ADMIN", "PROFESIONAL_ASST"));
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private Long createPersonEvaluated() throws Exception {
        return createPersonEvaluated("1061845213", "CARLOS ANDRES", "MUNOZ RIVERA", "carlos.munoz@empresa.com");
    }

    private Long createPersonEvaluated(String identificationNumber, String firstName, String lastName, String email) throws Exception {
        String json = String.format("""
            {
                "identificationType": "CC",
                "identificationNumber": "%s",
                "birthYear": 1990,
                "firstName": "%s",
                "lastName": "%s",
                "email": "%s"
            }
            """, identificationNumber, firstName, lastName, email);

        MvcResult result = mockMvc.perform(post("/asst/person-evaluated")
                .header("Authorization", "Bearer " + tokenProfesional)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }

    private Long createBatteryRecord(Long personId) throws Exception {
        MvcResult result = mockMvc.perform(post("/asst/battery-management-record/" + personId)
                .header("Authorization", "Bearer " + tokenProfesional))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }

    private Long createUser() throws Exception {
        String json = """
            {
                "email": "consulta.usuario@unicauca.edu.co",
                "username": "consulta_usuario",
                "fullName": "Consulta Usuario Test",
                "roleIds": [1]
            }
            """;

        MvcResult result = mockMvc.perform(post("/asst/users")
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }

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
                .header("Authorization", "Bearer " + tokenProfesional)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }

    // ==================================================================================
    // Consultas de personas evaluadas
    // ==================================================================================

    @Nested
    @DisplayName("Consultas de personas evaluadas")
    class PersonEvaluatedQueries {

        @Test
        @DisplayName("Debe retornar resultados paginados al listar personas evaluadas")
        void should_returnPagedResults_when_listPersonsEvaluated() throws Exception {
            createPersonEvaluated();
            createPersonEvaluated("9876543210", "MARIA JOSE", "GARCIA LOPEZ", "otra.persona@empresa.com");

            mockMvc.perform(get("/asst/person-evaluated/list-paged")
                    .header("Authorization", "Bearer " + tokenProfesional)
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content.length()", greaterThanOrEqualTo(2)));
        }

        @Test
        @DisplayName("Debe retornar resultados filtrados al proveer searchTerm")
        void should_returnFilteredResults_when_searchTermProvided() throws Exception {
            createPersonEvaluated();

            mockMvc.perform(get("/asst/person-evaluated/list-paged-filtered")
                    .header("Authorization", "Bearer " + tokenProfesional)
                    .param("page", "0")
                    .param("size", "10")
                    .param("searchTerm", "CARLOS"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content.length()", greaterThanOrEqualTo(1)));
        }

        @Test
        @DisplayName("Debe retornar pagina vacia cuando no hay datos en esa pagina")
        void should_returnEmptyPage_when_noDataExists() throws Exception {
            mockMvc.perform(get("/asst/person-evaluated/list-paged")
                    .header("Authorization", "Bearer " + tokenProfesional)
                    .param("page", "99")
                    .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content.length()").value(0));
        }
    }

    // ==================================================================================
    // Consultas de registros de bateria
    // ==================================================================================

    @Nested
    @DisplayName("Consultas de registros de bateria")
    class BatteryRecordQueries {

        @Test
        @DisplayName("Debe retornar resultados paginados al listar registros de bateria")
        void should_returnPagedResults_when_listBatteryRecords() throws Exception {
            Long personId = createPersonEvaluated();
            createBatteryRecord(personId);

            mockMvc.perform(get("/asst/battery-management-record/list-paged")
                    .header("Authorization", "Bearer " + tokenProfesional)
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar resultados al filtrar por searchTerm")
        void should_returnResults_when_filteredBySearchTerm() throws Exception {
            Long personId = createPersonEvaluated();
            createBatteryRecord(personId);

            mockMvc.perform(get("/asst/battery-management-record/list-paged-filtered")
                    .header("Authorization", "Bearer " + tokenProfesional)
                    .param("page", "0")
                    .param("size", "10")
                    .param("searchTerm", "1061"))
                    .andExpect(status().isOk());
        }
    }

    // ==================================================================================
    // Consultas de usuarios del sistema
    // ==================================================================================

    @Nested
    @DisplayName("Consultas de usuarios del sistema")
    class SystemUserQueries {

        @Test
        @DisplayName("Debe retornar resultados paginados cuando admin lista usuarios")
        void should_returnPagedResults_when_adminListsUsers() throws Exception {
            mockMvc.perform(get("/asst/users/list-paged")
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content.length()", greaterThanOrEqualTo(1)));
        }

        @Test
        @DisplayName("Debe retornar resultados filtrados cuando admin busca usuarios")
        void should_returnFilteredResults_when_adminSearchesUsers() throws Exception {
            mockMvc.perform(get("/asst/users/list-paged-filtered")
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .param("page", "0")
                    .param("size", "10")
                    .param("searchTerm", "admin"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar el usuario actual al llamar endpoint /me")
        void should_returnCurrentUser_when_meEndpointCalled() throws Exception {
            mockMvc.perform(get("/asst/users/me")
                    .header("Authorization", "Bearer " + tokenAdmin))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.email").value("admin@unicauca.edu.co"));
        }
    }

    // ==================================================================================
    // Consultas de evaluadores
    // ==================================================================================

    @Nested
    @DisplayName("Consultas de evaluadores")
    class EvaluatorQueries {

        @Test
        @DisplayName("Debe retornar resultados paginados al listar evaluadores")
        void should_returnPagedResults_when_listEvaluators() throws Exception {
            createEvaluator();

            mockMvc.perform(get("/asst/reports/evaluators/list-paged")
                    .header("Authorization", "Bearer " + tokenProfesional)
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar lista de evaluadores al obtener todos")
        void should_returnEvaluatorList_when_getAll() throws Exception {
            createEvaluator();

            mockMvc.perform(get("/asst/reports/evaluators")
                    .header("Authorization", "Bearer " + tokenProfesional))
                    .andExpect(status().isOk());
        }
    }
}
