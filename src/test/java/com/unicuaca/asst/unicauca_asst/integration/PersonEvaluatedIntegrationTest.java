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
import com.fasterxml.jackson.databind.node.ObjectNode;
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
@Sql(scripts = "/seed-battery-record.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class PersonEvaluatedIntegrationTest {

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

    private String buildValidRequestJson() {
        return """
            {
                "identificationType": "CC",
                "identificationNumber": "1061845213",
                "birthYear": 1990,
                "firstName": "CARLOS ANDRES",
                "lastName": "MUNOZ RIVERA",
                "email": "carlos.munoz@empresa.com"
            }
            """;
    }

    private ResultActions performPost(String jsonBody) throws Exception {
        return mockMvc.perform(post("/asst/person-evaluated")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));
    }

    private String buildJsonWithField(String field, Object value) throws Exception {
        var node = objectMapper.readTree(buildValidRequestJson());
        ObjectNode mutable = objectMapper.createObjectNode();
        node.fields().forEachRemaining(entry -> mutable.set(entry.getKey(), entry.getValue()));
        if (value == null) {
            mutable.putNull(field);
        } else if (value instanceof String s) {
            mutable.put(field, s);
        } else if (value instanceof Integer i) {
            mutable.put(field, i);
        }
        return objectMapper.writeValueAsString(mutable);
    }

    private String buildValidUpdateJson() {
        return """
            {
                "identificationType": "CC",
                "identificationNumber": "1061845213",
                "birthYear": 1991,
                "firstName": "CARLOS EDUARDO",
                "lastName": "MUNOZ GOMEZ",
                "email": "carlos.actualizado@empresa.com"
            }
            """;
    }

    private ResultActions performPut(Long id, String jsonBody) throws Exception {
        return mockMvc.perform(put("/asst/person-evaluated/" + id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));
    }

    private ResultActions performDelete(Long id) throws Exception {
        return mockMvc.perform(delete("/asst/person-evaluated/" + id)
                .header("Authorization", "Bearer " + token));
    }

    private Long extractId(ResultActions result) throws Exception {
        MvcResult mvcResult = result.andReturn();
        JsonNode root = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }

    // ==================================================================================
    // Creación exitosa
    // ==================================================================================

    @Nested
    @DisplayName("Creación exitosa")
    class CreateSuccess {

        @Test
        @DisplayName("Debe retornar 201 cuando los datos son válidos")
        void should_return201_when_validDataProvided() throws Exception {
            // Act & Assert
            performPost(buildValidRequestJson())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.code").value("ASST-0001"))
                    .andExpect(jsonPath("$.data.firstName").value("CARLOS ANDRES"));
        }

        @Test
        @DisplayName("Debe retornar los datos completos de la persona creada")
        void should_returnPersonData_when_createdSuccessfully() throws Exception {
            // Act & Assert
            performPost(buildValidRequestJson())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.identificationNumber").value("1061845213"))
                    .andExpect(jsonPath("$.data.firstName").value("CARLOS ANDRES"))
                    .andExpect(jsonPath("$.data.lastName").value("MUNOZ RIVERA"))
                    .andExpect(jsonPath("$.data.birthYear").value(1990))
                    .andExpect(jsonPath("$.data.email").value("carlos.munoz@empresa.com"))
                    .andExpect(jsonPath("$.data.identificacionAbbreviation").value("CC"));
        }
    }

    // ==================================================================================
    // Validación FirstGroup — campos obligatorios vacíos
    // ==================================================================================

    @Nested
    @DisplayName("Validación FirstGroup — campos obligatorios vacíos")
    class ValidationFirstGroup {

        @Test
        @DisplayName("Debe retornar 400 cuando todos los campos están vacíos")
        void should_return400_when_allFieldsEmpty() throws Exception {
            // Arrange
            String json = """
                {
                    "identificationType": "",
                    "identificationNumber": "",
                    "birthYear": null,
                    "firstName": "",
                    "lastName": "",
                    "email": ""
                }
                """;

            // Act & Assert
            performPost(json)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("ASST-VAL-0000"));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando firstName está vacío")
        void should_return400_when_firstNameIsBlank() throws Exception {
            // Act & Assert
            performPost(buildJsonWithField("firstName", ""))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando email está vacío")
        void should_return400_when_emailIsBlank() throws Exception {
            // Act & Assert
            performPost(buildJsonWithField("email", ""))
                    .andExpect(status().isBadRequest());
        }
    }

    // ==================================================================================
    // Validación SecondGroup — formato y tamaño
    // ==================================================================================

    @Nested
    @DisplayName("Validación SecondGroup — formato y tamaño")
    class ValidationSecondGroup {

        @Test
        @DisplayName("Debe retornar 400 cuando identificationNumber es muy corto")
        void should_return400_when_identificationNumberTooShort() throws Exception {
            // Act & Assert
            performPost(buildJsonWithField("identificationNumber", "123"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando identificationNumber tiene caracteres especiales")
        void should_return400_when_identificationNumberHasSpecialChars() throws Exception {
            // Act & Assert
            performPost(buildJsonWithField("identificationNumber", "ABC@#$"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando email tiene formato inválido")
        void should_return400_when_emailHasInvalidFormat() throws Exception {
            // Act & Assert
            performPost(buildJsonWithField("email", "no-es-un-email"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando firstName excede longitud máxima")
        void should_return400_when_firstNameExceedsMaxLength() throws Exception {
            // Act & Assert
            performPost(buildJsonWithField("firstName", "A".repeat(81)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando birthYear es futuro")
        void should_return400_when_birthYearIsFuture() throws Exception {
            // Act & Assert
            performPost(buildJsonWithField("birthYear", 2030))
                    .andExpect(status().isBadRequest());
        }
    }

    // ==================================================================================
    // Duplicados
    // ==================================================================================

    @Nested
    @DisplayName("Duplicados")
    class Duplicates {

        @Test
        @DisplayName("Debe retornar 409 cuando la persona ya existe")
        void should_return409_when_personAlreadyExists() throws Exception {
            // Arrange — primera creación exitosa
            performPost(buildValidRequestJson())
                    .andExpect(status().isCreated());

            // Act & Assert — segunda creación debe fallar por duplicado
            performPost(buildValidRequestJson())
                    .andExpect(status().isConflict());
        }
    }

    // ==================================================================================
    // Actualización de persona evaluada
    // ==================================================================================

    @Nested
    @DisplayName("Actualización de persona evaluada")
    class UpdatePersonEvaluated {

        @Test
        @DisplayName("Debe retornar 200 cuando los datos de actualización son válidos")
        void should_return200_when_validUpdateData() throws Exception {
            // Arrange
            Long personId = extractId(performPost(buildValidRequestJson()).andExpect(status().isCreated()));

            // Act & Assert
            performPut(personId, buildValidUpdateJson())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.firstName").value("CARLOS EDUARDO"));
        }

        @Test
        @DisplayName("Debe retornar 404 cuando la persona no existe")
        void should_return404_when_personNotFoundForUpdate() throws Exception {
            // Act & Assert
            performPut(99999L, buildValidUpdateJson())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe retornar 409 cuando la identificación ya existe en otra persona")
        void should_return409_when_identificationExistsOnUpdate() throws Exception {
            // Arrange — persona 1
            performPost(buildValidRequestJson()).andExpect(status().isCreated());

            // Arrange — persona 2
            String person2Json = """
                {
                    "identificationType": "CC",
                    "identificationNumber": "9876543210",
                    "birthYear": 1990,
                    "firstName": "MARIA",
                    "lastName": "GARCIA",
                    "email": "otro@empresa.com"
                }
                """;
            Long person2Id = extractId(performPost(person2Json).andExpect(status().isCreated()));

            // Act & Assert — intentar cambiar identificationNumber de persona 2 al de persona 1
            String conflictUpdateJson = """
                {
                    "identificationType": "CC",
                    "identificationNumber": "1061845213",
                    "birthYear": 1990,
                    "firstName": "MARIA",
                    "lastName": "GARCIA",
                    "email": "otro@empresa.com"
                }
                """;

            performPut(person2Id, conflictUpdateJson)
                    .andExpect(status().isConflict());
        }
    }

    // ==================================================================================
    // Eliminación de persona evaluada
    // ==================================================================================

    @Nested
    @DisplayName("Eliminación de persona evaluada")
    class DeletePersonEvaluated {

        @Test
        @DisplayName("Debe retornar 200 cuando la persona existe y no tiene registros")
        void should_return200_when_personExistsAndHasNoRecords() throws Exception {
            // Arrange
            Long personId = extractId(performPost(buildValidRequestJson()).andExpect(status().isCreated()));

            // Act & Assert
            performDelete(personId)
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 404 cuando la persona no existe")
        void should_return404_when_personNotFoundForDelete() throws Exception {
            // Act & Assert
            performDelete(99999L)
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando la persona tiene registros de batería asociados")
        void should_return400_when_personHasBatteryRecords() throws Exception {
            // Arrange — crear persona
            Long personId = extractId(performPost(buildValidRequestJson()).andExpect(status().isCreated()));

            // Arrange — crear registro de batería para la persona
            mockMvc.perform(post("/asst/battery-management-record/" + personId)
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isCreated());

            // Act & Assert — intentar eliminar la persona con registros asociados
            performDelete(personId)
                    .andExpect(status().isBadRequest());
        }
    }
}
