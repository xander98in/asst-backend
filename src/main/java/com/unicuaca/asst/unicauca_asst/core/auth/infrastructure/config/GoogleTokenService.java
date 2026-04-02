package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.unicuaca.asst.unicauca_asst.common.exceptions.GoogleTokenException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import jakarta.annotation.PostConstruct;

/**
 * Servicio de infraestructura para la verificación de Google ID Tokens.
 *
 * <p>Utiliza la librería {@code google-api-client} para verificar la autenticidad
 * del token emitido por Google OAuth y extraer el correo electrónico del usuario.</p>
 *
 * <p>Valida además que el dominio del correo (campo {@code hd} del payload)
 * corresponda al dominio institucional permitido.</p>
 */
@Service
public class GoogleTokenService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${google.allowed-domain}")
    private String allowedDomain;

    private GoogleIdTokenVerifier verifier;

    /**
     * Inicializa el verificador de tokens de Google con el Client ID configurado.
     */
    @PostConstruct
    private void init() {
        this.verifier = new GoogleIdTokenVerifier.Builder(
            new NetHttpTransport(),
            GsonFactory.getDefaultInstance()
        )
        .setAudience(Collections.singletonList(googleClientId))
        .build();
    }

    /**
     * Verifica un Google ID Token y extrae el correo electrónico del usuario.
     *
     * @param googleIdToken el token de ID emitido por Google OAuth
     * @return el correo electrónico del usuario autenticado
     * @throws GoogleTokenException si el token es inválido, el dominio no es permitido
     *                              o si ocurre un error técnico durante la verificación
     */
    public String verifyAndExtractEmail(String googleIdToken) {
        try {
            GoogleIdToken idToken = verifier.verify(googleIdToken);

            if (idToken == null) {
                throw new GoogleTokenException(
                    ErrorCode.INVALID_GOOGLE_TOKEN.getCode(),
                    ErrorCode.INVALID_GOOGLE_TOKEN.getMessageKey(),
                    "user.auth.invalid_google_token"
                );
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            // Validar dominio institucional
            String hostedDomain = payload.getHostedDomain();
            if (hostedDomain == null || !allowedDomain.equals(hostedDomain)) {
                throw new GoogleTokenException(
                    ErrorCode.USER_DOMAIN_NOT_ALLOWED.getCode(),
                    ErrorCode.USER_DOMAIN_NOT_ALLOWED.getMessageKey(),
                    "user.user.domain_not_allowed",
                    new Object[]{hostedDomain != null ? hostedDomain : "dominio no institucional"}
                );
            }

            return payload.getEmail();

        } catch (GoogleTokenException e) {
            throw e;
        } catch (IOException | GeneralSecurityException e) {
            // Token malformado o inválido — error técnico de verificación
            String detail = e.getMessage() != null
                ? e.getMessage()
                : e.getClass().getSimpleName();
            throw new GoogleTokenException(
                ErrorCode.GOOGLE_TOKEN_VERIFICATION_FAILED.getCode(),
                ErrorCode.GOOGLE_TOKEN_VERIFICATION_FAILED.getMessageKey(),
                "user.auth.google_token_failed",
                new Object[]{detail}
            );
        } catch (Exception e) {
            // Cualquier otro error inesperado
            String detail = e.getMessage() != null
                ? e.getMessage()
                : e.getClass().getSimpleName();
            throw new GoogleTokenException(
                ErrorCode.GOOGLE_TOKEN_VERIFICATION_FAILED.getCode(),
                ErrorCode.GOOGLE_TOKEN_VERIFICATION_FAILED.getMessageKey(),
                "user.auth.google_token_failed",
                new Object[]{detail}
            );
        }
    }
}
