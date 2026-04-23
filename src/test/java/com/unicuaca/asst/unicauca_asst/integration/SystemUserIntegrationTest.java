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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/seed-system-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class SystemUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    private static final String BASE_URL = "/asst/users";

    @BeforeEach
    void setUp() {
        token = jwtService.generateAccessToken("admin@unicauca.edu.co", Set.of("ADMIN"));
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private String buildValidCreateJson() {
        return """
            {
                "email": "nuevo.usuario@unicauca.edu.co",
                "username": "nuevo_usuario",
                "fullName": "Nuevo Usuario Test",
                "roleIds": [1]
            }
            """;
    }

    private ResultActions performPost(String json) throws Exception {
        return mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private ResultActions performPut(Long id, String json) throws Exception {
        return mockMvc.perform(put(BASE_URL + "/" + id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private ResultActions performPatchStatus(Long id, String json) throws Exception {
        return mockMvc.perform(patch(BASE_URL + "/" + id + "/status")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private ResultActions performDelete(Long id) throws Exception {
        return mockMvc.perform(delete(BASE_URL + "/" + id)
                .header("Authorization", "Bearer " + token));
    }

    private Long createUser() throws Exception {
        MvcResult result = performPost(buildValidCreateJson())
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }

    // ==================================================================================
    // Creacion de usuario
    // ==================================================================================

    @Nested
    @DisplayName("Creacion de usuario")
    class CreateUser {

        @Test
        @DisplayName("Debe retornar 201 cuando los datos son validos")
        void should_return201_when_validDataProvided() throws Exception {
            performPost(buildValidCreateJson())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.email").value("nuevo.usuario@unicauca.edu.co"));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando los campos obligatorios estan vacios")
        void should_return400_when_requiredFieldsEmpty() throws Exception {
            String json = """
                {
                    "email": "",
                    "username": "",
                    "fullName": "",
                    "roleIds": []
                }
                """;

            performPost(json)
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el email no es del dominio unicauca.edu.co")
        void should_return400_when_emailNotUnicaucaDomain() throws Exception {
            String json = """
                {
                    "email": "usuario@gmail.com",
                    "username": "nuevo_usuario",
                    "fullName": "Nuevo Usuario Test",
                    "roleIds": [1]
                }
                """;

            performPost(json)
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 409 cuando el email ya existe")
        void should_return409_when_emailAlreadyExists() throws Exception {
            performPost(buildValidCreateJson())
                    .andExpect(status().isCreated());

            performPost(buildValidCreateJson())
                    .andExpect(status().isConflict());
        }
    }

    // ==================================================================================
    // Actualizacion de usuario
    // ==================================================================================

    @Nested
    @DisplayName("Actualizacion de usuario")
    class UpdateUser {

        @Test
        @DisplayName("Debe retornar 200 cuando los datos de actualizacion son validos")
        void should_return200_when_validUpdateData() throws Exception {
            Long userId = createUser();

            String updateJson = """
                {
                    "email": "nuevo.usuario@unicauca.edu.co",
                    "username": "nuevo_usuario",
                    "fullName": "Usuario Actualizado",
                    "roleIds": [1]
                }
                """;

            performPut(userId, updateJson)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.fullName").value("Usuario Actualizado"));
        }

        @Test
        @DisplayName("Debe retornar 404 cuando el usuario no existe")
        void should_return404_when_userNotFound() throws Exception {
            String updateJson = """
                {
                    "email": "cualquier.email@unicauca.edu.co",
                    "username": "cualquier_username",
                    "fullName": "Cualquier Nombre",
                    "roleIds": [1]
                }
                """;

            performPut(99999L, updateJson)
                    .andExpect(status().isNotFound());
        }
    }

    // ==================================================================================
    // Cambio de estado
    // ==================================================================================

    @Nested
    @DisplayName("Cambio de estado")
    class ChangeStatus {

        @Test
        @DisplayName("Debe retornar 200 cuando el estado cambia a Inactivo")
        void should_return200_when_statusChangedToInactivo() throws Exception {
            Long userId = createUser();

            String json = """
                {
                    "statusName": "Inactivo"
                }
                """;

            performPatchStatus(userId, json)
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el nuevo estado es igual al actual")
        void should_return400_when_statusIsSameAsCurrent() throws Exception {
            Long userId = createUser();

            String json = """
                {
                    "statusName": "Activo"
                }
                """;

            performPatchStatus(userId, json)
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 404 cuando el usuario no existe para cambio de estado")
        void should_return404_when_userNotFoundForStatusChange() throws Exception {
            String json = """
                {
                    "statusName": "Inactivo"
                }
                """;

            performPatchStatus(99999L, json)
                    .andExpect(status().isNotFound());
        }
    }

    // ==================================================================================
    // Eliminacion de usuario
    // ==================================================================================

    @Nested
    @DisplayName("Eliminacion de usuario")
    class DeleteUser {

        @Test
        @DisplayName("Debe retornar 200 cuando el usuario existe")
        void should_return200_when_userExists() throws Exception {
            Long userId = createUser();

            performDelete(userId)
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 404 cuando el usuario no existe para eliminacion")
        void should_return404_when_userNotFoundForDelete() throws Exception {
            performDelete(99999L)
                    .andExpect(status().isNotFound());
        }
    }
}
