package com.unicuaca.asst.unicauca_asst.security;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.unicuaca.asst.unicauca_asst.common.exceptions.InvalidJwtException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config.JwtService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private static final String TEST_SECRET = "dGVzdC1zZWNyZXQta2V5LWZvci11bml0LXRlc3RzLW9ubHktZG8tbm90LXVzZS1pbi1wcm9kdWN0aW9uLWVudmlyb25tZW50";
    private static final String OTHER_SECRET = "b3RoZXItc2VjcmV0LWtleS1mb3ItdGVzdGluZy1pbnZhbGlkLXNpZ25hdHVyZS12ZXJpZmljYXRpb24tdGVzdHM=";
    private static final String TEST_EMAIL = "test@unicauca.edu.co";
    private static final Set<String> TEST_ROLES = Set.of("ADMIN", "PROFESIONAL_ASST");
    private static final long ACCESS_EXPIRATION = 3600000L;
    private static final long REFRESH_EXPIRATION = 86400000L;

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = createJwtService(TEST_SECRET, ACCESS_EXPIRATION, REFRESH_EXPIRATION);
    }

    // ==================================================================================
    // generateAccessToken
    // ==================================================================================

    @Nested
    @DisplayName("generateAccessToken")
    class GenerateAccessToken {

        @Test
        @DisplayName("Debe generar un token válido cuando se provee email y roles")
        void should_generateValidToken_when_emailAndRolesProvided() {
            // Act
            String token = jwtService.generateAccessToken(TEST_EMAIL, TEST_ROLES);

            // Assert
            assertThat(token).isNotNull().isNotEmpty();
        }

        @Test
        @DisplayName("Debe contener el email correcto cuando se genera el token")
        void should_containCorrectEmail_when_tokenGenerated() {
            // Act
            String token = jwtService.generateAccessToken(TEST_EMAIL, TEST_ROLES);

            // Assert
            assertThat(jwtService.extractEmail(token)).isEqualTo(TEST_EMAIL);
        }

        @Test
        @DisplayName("Debe contener los roles correctos cuando se genera el token")
        void should_containCorrectRoles_when_tokenGenerated() {
            // Act
            String token = jwtService.generateAccessToken(TEST_EMAIL, TEST_ROLES);

            // Assert
            assertThat(jwtService.extractRoles(token)).containsExactlyInAnyOrderElementsOf(TEST_ROLES);
        }

        @Test
        @DisplayName("Debe contener tipo ACCESS cuando se genera el token")
        void should_containAccessTokenType_when_tokenGenerated() {
            // Act
            String token = jwtService.generateAccessToken(TEST_EMAIL, TEST_ROLES);

            // Assert
            assertThat(jwtService.extractTokenType(token)).isEqualTo("ACCESS");
        }
    }

    // ==================================================================================
    // generateRefreshToken
    // ==================================================================================

    @Nested
    @DisplayName("generateRefreshToken")
    class GenerateRefreshToken {

        @Test
        @DisplayName("Debe generar un token válido cuando se provee email")
        void should_generateValidToken_when_emailProvided() {
            // Act
            String token = jwtService.generateRefreshToken(TEST_EMAIL);

            // Assert
            assertThat(token).isNotNull().isNotEmpty();
        }

        @Test
        @DisplayName("Debe contener el email correcto cuando se genera el refresh token")
        void should_containCorrectEmail_when_refreshTokenGenerated() {
            // Act
            String token = jwtService.generateRefreshToken(TEST_EMAIL);

            // Assert
            assertThat(jwtService.extractEmail(token)).isEqualTo(TEST_EMAIL);
        }

        @Test
        @DisplayName("Debe contener tipo REFRESH cuando se genera el token")
        void should_containRefreshTokenType_when_refreshTokenGenerated() {
            // Act
            String token = jwtService.generateRefreshToken(TEST_EMAIL);

            // Assert
            assertThat(jwtService.extractTokenType(token)).isEqualTo("REFRESH");
        }
    }

    // ==================================================================================
    // validateAccessToken
    // ==================================================================================

    @Nested
    @DisplayName("validateAccessToken")
    class ValidateAccessToken {

        @Test
        @DisplayName("No debe lanzar excepción cuando el access token es válido")
        void should_notThrowException_when_validAccessToken() {
            // Arrange
            String token = jwtService.generateAccessToken(TEST_EMAIL, TEST_ROLES);

            // Act & Assert
            assertThatCode(() -> jwtService.validateAccessToken(token))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Debe lanzar InvalidJwtException cuando el token ha expirado")
        void should_throwInvalidJwtException_when_tokenIsExpired() {
            // Arrange
            JwtService expiredService = createJwtService(TEST_SECRET, 0L, REFRESH_EXPIRATION);
            String token = expiredService.generateAccessToken(TEST_EMAIL, TEST_ROLES);

            // Act & Assert
            assertThatThrownBy(() -> jwtService.validateAccessToken(token))
                    .isInstanceOf(InvalidJwtException.class)
                    .extracting("code")
                    .isEqualTo(ErrorCode.JWT_EXPIRED.getCode());
        }

        @Test
        @DisplayName("Debe lanzar InvalidJwtException cuando la firma es inválida")
        void should_throwInvalidJwtException_when_signatureIsInvalid() {
            // Arrange
            JwtService otherService = createJwtService(OTHER_SECRET, ACCESS_EXPIRATION, REFRESH_EXPIRATION);
            String token = otherService.generateAccessToken(TEST_EMAIL, TEST_ROLES);

            // Act & Assert
            assertThatThrownBy(() -> jwtService.validateAccessToken(token))
                    .isInstanceOf(InvalidJwtException.class)
                    .extracting("code")
                    .isEqualTo(ErrorCode.JWT_INVALID_SIGNATURE.getCode());
        }

        @Test
        @DisplayName("Debe lanzar InvalidJwtException cuando el token está malformado")
        void should_throwInvalidJwtException_when_tokenIsMalformed() {
            // Act & Assert
            assertThatThrownBy(() -> jwtService.validateAccessToken("not.a.valid.jwt"))
                    .isInstanceOf(InvalidJwtException.class)
                    .extracting("code")
                    .isEqualTo(ErrorCode.JWT_MALFORMED.getCode());
        }

        @Test
        @DisplayName("Debe lanzar InvalidJwtException cuando se usa refresh token como access")
        void should_throwInvalidJwtException_when_refreshTokenUsedAsAccess() {
            // Arrange
            String refreshToken = jwtService.generateRefreshToken(TEST_EMAIL);

            // Act & Assert
            assertThatThrownBy(() -> jwtService.validateAccessToken(refreshToken))
                    .isInstanceOf(InvalidJwtException.class)
                    .extracting("code")
                    .isEqualTo(ErrorCode.JWT_INVALID_SIGNATURE.getCode());
        }
    }

    // ==================================================================================
    // validateRefreshToken
    // ==================================================================================

    @Nested
    @DisplayName("validateRefreshToken")
    class ValidateRefreshToken {

        @Test
        @DisplayName("No debe lanzar excepción cuando el refresh token es válido")
        void should_notThrowException_when_validRefreshToken() {
            // Arrange
            String token = jwtService.generateRefreshToken(TEST_EMAIL);

            // Act & Assert
            assertThatCode(() -> jwtService.validateRefreshToken(token))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Debe lanzar InvalidJwtException cuando el refresh token ha expirado")
        void should_throwInvalidJwtException_when_refreshTokenExpired() {
            // Arrange
            JwtService expiredService = createJwtService(TEST_SECRET, ACCESS_EXPIRATION, 0L);
            String token = expiredService.generateRefreshToken(TEST_EMAIL);

            // Act & Assert
            assertThatThrownBy(() -> jwtService.validateRefreshToken(token))
                    .isInstanceOf(InvalidJwtException.class)
                    .extracting("code")
                    .isEqualTo(ErrorCode.REFRESH_TOKEN_EXPIRED.getCode());
        }

        @Test
        @DisplayName("Debe lanzar InvalidJwtException cuando se usa access token como refresh")
        void should_throwInvalidJwtException_when_accessTokenUsedAsRefresh() {
            // Arrange
            String accessToken = jwtService.generateAccessToken(TEST_EMAIL, TEST_ROLES);

            // Act & Assert
            assertThatThrownBy(() -> jwtService.validateRefreshToken(accessToken))
                    .isInstanceOf(InvalidJwtException.class)
                    .extracting("code")
                    .isEqualTo(ErrorCode.REFRESH_TOKEN_INVALID.getCode());
        }
    }

    // ==================================================================================
    // extractEmail
    // ==================================================================================

    @Nested
    @DisplayName("extractEmail")
    class ExtractEmail {

        @Test
        @DisplayName("Debe retornar el email cuando el access token es válido")
        void should_returnEmail_when_validAccessToken() {
            // Arrange
            String token = jwtService.generateAccessToken(TEST_EMAIL, TEST_ROLES);

            // Act & Assert
            assertThat(jwtService.extractEmail(token)).isEqualTo(TEST_EMAIL);
        }

        @Test
        @DisplayName("Debe retornar el email cuando el refresh token es válido")
        void should_returnEmail_when_validRefreshToken() {
            // Arrange
            String token = jwtService.generateRefreshToken(TEST_EMAIL);

            // Act & Assert
            assertThat(jwtService.extractEmail(token)).isEqualTo(TEST_EMAIL);
        }
    }

    // ==================================================================================
    // extractRoles
    // ==================================================================================

    @Nested
    @DisplayName("extractRoles")
    class ExtractRoles {

        @Test
        @DisplayName("Debe retornar los roles cuando el access token tiene roles")
        void should_returnRoles_when_accessTokenHasRoles() {
            // Arrange
            String token = jwtService.generateAccessToken(TEST_EMAIL, TEST_ROLES);

            // Act & Assert
            assertThat(jwtService.extractRoles(token)).containsExactlyInAnyOrderElementsOf(TEST_ROLES);
        }

        @Test
        @DisplayName("Debe retornar set vacío cuando el refresh token no tiene roles")
        void should_returnEmptySet_when_refreshTokenHasNoRoles() {
            // Arrange
            String token = jwtService.generateRefreshToken(TEST_EMAIL);

            // Act & Assert
            assertThat(jwtService.extractRoles(token)).isEmpty();
        }
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private JwtService createJwtService(String secret, long accessExp, long refreshExp) {
        JwtService service = new JwtService();
        ReflectionTestUtils.setField(service, "jwtSecret", secret);
        ReflectionTestUtils.setField(service, "accessTokenExpiration", accessExp);
        ReflectionTestUtils.setField(service, "refreshTokenExpiration", refreshExp);
        return service;
    }
}
