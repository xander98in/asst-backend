package com.unicuaca.asst.unicauca_asst.security;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config.JwtService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EndpointAccessControlTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    private String tokenAdmin;
    private String tokenProfesional;

    @BeforeEach
    void setUp() {
        tokenAdmin = jwtService.generateAccessToken("admin@unicauca.edu.co", Set.of("ADMIN"));
        tokenProfesional = jwtService.generateAccessToken("profesional@unicauca.edu.co", Set.of("PROFESIONAL_ASST"));
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private ResultActions performWithoutToken(MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

    private ResultActions performWithToken(MockHttpServletRequestBuilder request, String token) throws Exception {
        return mockMvc.perform(request.header("Authorization", "Bearer " + token));
    }

    private void assertAccessAllowed(ResultActions result) throws Exception {
        int httpStatus = result.andReturn().getResponse().getStatus();
        assertThat(httpStatus).isNotIn(401, 403);
    }

    // ==================================================================================
    // AuthSecurityRules — /auth/**
    // ==================================================================================

    @Nested
    @DisplayName("AuthSecurityRules — /auth/**")
    class AuthEndpoints {

        @Test
        @DisplayName("Debe permitir acceso a login sin token")
        void should_allowAccess_when_loginWithoutToken() throws Exception {
            // Act
            ResultActions result = performWithoutToken(
                    post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("{}"));

            // Assert
            assertAccessAllowed(result);
        }

        @Test
        @DisplayName("Debe permitir acceso a refresh sin token")
        void should_allowAccess_when_refreshWithoutToken() throws Exception {
            // Act
            ResultActions result = performWithoutToken(
                    post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content("{}"));

            // Assert
            assertAccessAllowed(result);
        }

        @Test
        @DisplayName("Debe retornar 401 en logout sin token")
        void should_return401_when_logoutWithoutToken() throws Exception {
            // Act & Assert
            performWithoutToken(post("/auth/logout"))
                    .andExpect(status().isUnauthorized());
        }
    }

    // ==================================================================================
    // AuthUserSecurityRules — /asst/users/**
    // ==================================================================================

    @Nested
    @DisplayName("AuthUserSecurityRules — /asst/users/**")
    class UserEndpoints {

        @Test
        @DisplayName("Debe retornar 401 al consultar usuarios sin token")
        void should_return401_when_getUsersWithoutToken() throws Exception {
            // Act & Assert
            performWithoutToken(get("/asst/users/list-paged?page=0&size=10"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe retornar 403 cuando profesional consulta lista de usuarios")
        void should_return403_when_profesionalAccessesUsersList() throws Exception {
            // Act & Assert
            performWithToken(get("/asst/users/list-paged?page=0&size=10"), tokenProfesional)
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Debe permitir acceso cuando admin consulta lista de usuarios")
        void should_allowAccess_when_adminAccessesUsersList() throws Exception {
            // Act
            ResultActions result = performWithToken(
                    get("/asst/users/list-paged?page=0&size=10"), tokenAdmin);

            // Assert
            assertAccessAllowed(result);
        }

        @Test
        @DisplayName("Debe retornar 403 cuando profesional crea usuario")
        void should_return403_when_profesionalCreatesUser() throws Exception {
            // Act & Assert
            performWithToken(
                    post("/asst/users").contentType(MediaType.APPLICATION_JSON).content("{}"),
                    tokenProfesional)
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Debe permitir acceso cuando admin crea usuario")
        void should_allowAccess_when_adminCreatesUser() throws Exception {
            // Act
            ResultActions result = performWithToken(
                    post("/asst/users").contentType(MediaType.APPLICATION_JSON).content("{}"),
                    tokenAdmin);

            // Assert
            assertAccessAllowed(result);
        }

        @Test
        @DisplayName("Debe retornar 403 cuando profesional elimina usuario")
        void should_return403_when_profesionalDeletesUser() throws Exception {
            // Act & Assert
            performWithToken(delete("/asst/users/1"), tokenProfesional)
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Debe permitir acceso a /me para cualquier usuario autenticado")
        void should_allowAccess_when_anyAuthenticatedAccessesMe() throws Exception {
            // Act
            ResultActions result = performWithToken(get("/asst/users/me"), tokenProfesional);

            // Assert
            assertAccessAllowed(result);
        }
    }

    // ==================================================================================
    // CatalogSecurityRules — /asst/catalog/**
    // ==================================================================================

    @Nested
    @DisplayName("CatalogSecurityRules — /asst/catalog/**")
    class CatalogEndpoints {

        @Test
        @DisplayName("Debe retornar 401 al consultar catálogo sin token")
        void should_return401_when_catalogWithoutToken() throws Exception {
            // Act & Assert
            performWithoutToken(get("/asst/catalog/id-types"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe permitir acceso cuando admin consulta catálogo")
        void should_allowAccess_when_adminAccessesCatalog() throws Exception {
            // Act
            ResultActions result = performWithToken(get("/asst/catalog/id-types"), tokenAdmin);

            // Assert
            assertAccessAllowed(result);
        }

        @Test
        @DisplayName("Debe permitir acceso cuando profesional consulta catálogo")
        void should_allowAccess_when_profesionalAccessesCatalog() throws Exception {
            // Act
            ResultActions result = performWithToken(get("/asst/catalog/id-types"), tokenProfesional);

            // Assert
            assertAccessAllowed(result);
        }
    }

    // ==================================================================================
    // BatteriesSecurityRules — /asst/person-evaluated/**
    // ==================================================================================

    @Nested
    @DisplayName("BatteriesSecurityRules — /asst/person-evaluated/**")
    class BatteriesEndpoints {

        @Test
        @DisplayName("Debe retornar 401 al consultar baterías sin token")
        void should_return401_when_batteriesWithoutToken() throws Exception {
            // Act & Assert
            performWithoutToken(get("/asst/person-evaluated/list-paged?page=0&size=10"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe permitir acceso cuando profesional consulta baterías")
        void should_allowAccess_when_profesionalAccessesBatteries() throws Exception {
            // Act
            ResultActions result = performWithToken(
                    get("/asst/person-evaluated/list-paged?page=0&size=10"), tokenProfesional);

            // Assert
            assertAccessAllowed(result);
        }

        @Test
        @DisplayName("Debe permitir acceso cuando admin consulta baterías")
        void should_allowAccess_when_adminAccessesBatteries() throws Exception {
            // Act
            ResultActions result = performWithToken(
                    get("/asst/person-evaluated/list-paged?page=0&size=10"), tokenAdmin);

            // Assert
            assertAccessAllowed(result);
        }
    }

    // ==================================================================================
    // ReportsSecurityRules — /asst/reports/**
    // ==================================================================================

    @Nested
    @DisplayName("ReportsSecurityRules — /asst/reports/**")
    class ReportsEndpoints {

        @Test
        @DisplayName("Debe retornar 401 al consultar reportes sin token")
        void should_return401_when_reportsWithoutToken() throws Exception {
            // Act & Assert
            performWithoutToken(get("/asst/reports/evaluators"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe permitir acceso cuando profesional consulta reportes")
        void should_allowAccess_when_profesionalAccessesReports() throws Exception {
            // Act
            ResultActions result = performWithToken(
                    get("/asst/reports/evaluators"), tokenProfesional);

            // Assert
            assertAccessAllowed(result);
        }

        @Test
        @DisplayName("Debe permitir acceso cuando admin consulta reportes")
        void should_allowAccess_when_adminAccessesReports() throws Exception {
            // Act
            ResultActions result = performWithToken(
                    get("/asst/reports/evaluators"), tokenAdmin);

            // Assert
            assertAccessAllowed(result);
        }
    }

    // ==================================================================================
    // DefaultSecurityConfig — rutas no mapeadas
    // ==================================================================================

    @Nested
    @DisplayName("DefaultSecurityConfig — rutas no mapeadas")
    class DefaultSecurityEndpoints {

        @Test
        @DisplayName("Debe retornar 401 en ruta desconocida sin token")
        void should_return401_when_unknownRouteWithoutToken() throws Exception {
            // Act & Assert
            performWithoutToken(get("/asst/unknown-route"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe permitir acceso a ruta desconocida cuando está autenticado")
        void should_allowAccess_when_authenticatedAccessesUnknownRoute() throws Exception {
            // Act
            ResultActions result = performWithToken(get("/asst/unknown-route"), tokenAdmin);

            // Assert
            assertAccessAllowed(result);
        }
    }
}
