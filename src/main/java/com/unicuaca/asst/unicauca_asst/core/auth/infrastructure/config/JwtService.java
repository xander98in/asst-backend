package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.unicuaca.asst.unicauca_asst.common.exceptions.InvalidJwtException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

/**
 * Servicio de infraestructura para la generación y validación de JSON Web Tokens (JWT)
 * propios de la aplicación.
 *
 * <p>Genera dos tipos de tokens:</p>
 * <ul>
 *   <li><b>Access Token:</b> contiene email, roles y tipo "ACCESS". Expiración corta (1h por defecto).</li>
 *   <li><b>Refresh Token:</b> contiene email y tipo "REFRESH". Expiración larga (24h por defecto).</li>
 * </ul>
 *
 * <p>Utiliza la librería jjwt 0.12.6 con algoritmo HS512 para la firma de tokens.</p>
 */
@Service
public class JwtService {

    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_TYPE = "type";
    private static final String TOKEN_TYPE_ACCESS = "ACCESS";
    private static final String TOKEN_TYPE_REFRESH = "REFRESH";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    /**
     * Genera un access token JWT con el email del usuario y sus roles.
     *
     * @param email correo electrónico del usuario (subject del token)
     * @param roles conjunto de claves técnicas de los roles asignados al usuario
     * @return el access token firmado en formato compacto
     */
    public String generateAccessToken(String email, Set<String> roles) {
        return Jwts.builder()
            .subject(email)
            .claim(CLAIM_ROLES, roles)
            .claim(CLAIM_TYPE, TOKEN_TYPE_ACCESS)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
            .signWith(getSigningKey(), Jwts.SIG.HS512)
            .compact();
    }

    /**
     * Genera un refresh token JWT con el email del usuario.
     *
     * @param email correo electrónico del usuario (subject del token)
     * @return el refresh token firmado en formato compacto
     */
    public String generateRefreshToken(String email) {
        return Jwts.builder()
            .subject(email)
            .claim(CLAIM_TYPE, TOKEN_TYPE_REFRESH)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
            .signWith(getSigningKey(), Jwts.SIG.HS512)
            .compact();
    }

    /**
     * Extrae el email (subject) del token.
     *
     * @param token el JWT en formato compacto
     * @return el email del usuario contenido en el subject
     * @throws InvalidJwtException si el token es inválido o no puede ser parseado
     */
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extrae el conjunto de roles del token.
     *
     * @param token el JWT en formato compacto
     * @return conjunto de claves técnicas de roles, o vacío si no contiene roles
     */
    @SuppressWarnings("unchecked")
    public Set<String> extractRoles(String token) {
        Claims claims = parseClaims(token);
        List<String> roles = claims.get(CLAIM_ROLES, List.class);
        return roles != null ? new HashSet<>(roles) : new HashSet<>();
    }

    /**
     * Extrae el tipo de token (ACCESS o REFRESH) del claim {@code type}.
     *
     * @param token el JWT en formato compacto
     * @return el tipo de token ("ACCESS" o "REFRESH")
     */
    public String extractTokenType(String token) {
        return parseClaims(token).get(CLAIM_TYPE, String.class);
    }

    /**
     * Valida que el token sea un access token válido.
     *
     * <p>Verifica la firma, expiración y que el tipo sea "ACCESS".</p>
     *
     * @param token el JWT en formato compacto
     * @throws InvalidJwtException si el token ha expirado, tiene firma inválida,
     *                             está malformado o no es de tipo ACCESS
     */
    public void validateAccessToken(String token) {
        Claims claims = parseClaims(token);

        String tokenType = claims.get(CLAIM_TYPE, String.class);
        if (!TOKEN_TYPE_ACCESS.equals(tokenType)) {
            throw new InvalidJwtException(
                ErrorCode.JWT_INVALID_SIGNATURE.getCode(),
                ErrorCode.JWT_INVALID_SIGNATURE.getMessageKey(),
                "user.auth.jwt_invalid_signature"
            );
        }
    }

    /**
     * Valida que el token sea un refresh token válido.
     *
     * <p>Verifica la firma, expiración y que el tipo sea "REFRESH".</p>
     *
     * @param token el JWT en formato compacto
     * @throws InvalidJwtException si el token ha expirado, es inválido
     *                             o no es de tipo REFRESH
     */
    public void validateRefreshToken(String token) {
        Claims claims = parseClaimsForRefreshToken(token);

        String tokenType = claims.get(CLAIM_TYPE, String.class);
        if (!TOKEN_TYPE_REFRESH.equals(tokenType)) {
            throw new InvalidJwtException(
                ErrorCode.REFRESH_TOKEN_INVALID.getCode(),
                ErrorCode.REFRESH_TOKEN_INVALID.getMessageKey(),
                "user.auth.refresh_token_invalid"
            );
        }
    }

    // ── Métodos privados ─────────────────────────────────────────────────

    /**
     * Parsea y valida las claims del token, mapeando excepciones de jjwt
     * a {@link InvalidJwtException} con códigos de error de access token.
     *
     * @param token el JWT en formato compacto
     * @return las claims del token
     * @throws InvalidJwtException si el token es inválido
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new InvalidJwtException(
                ErrorCode.JWT_EXPIRED.getCode(),
                ErrorCode.JWT_EXPIRED.getMessageKey(),
                "user.auth.jwt_expired"
            );
        } catch (SignatureException e) {
            throw new InvalidJwtException(
                ErrorCode.JWT_INVALID_SIGNATURE.getCode(),
                ErrorCode.JWT_INVALID_SIGNATURE.getMessageKey(),
                "user.auth.jwt_invalid_signature"
            );
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new InvalidJwtException(
                ErrorCode.JWT_MALFORMED.getCode(),
                ErrorCode.JWT_MALFORMED.getMessageKey(),
                "user.auth.jwt_malformed"
            );
        }
    }

    /**
     * Parsea claims con errores mapeados específicamente para refresh tokens.
     *
     * @param token el JWT en formato compacto
     * @return las claims del token
     * @throws InvalidJwtException con códigos de error de refresh token
     */
    private Claims parseClaimsForRefreshToken(String token) {
        try {
            return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new InvalidJwtException(
                ErrorCode.REFRESH_TOKEN_EXPIRED.getCode(),
                ErrorCode.REFRESH_TOKEN_EXPIRED.getMessageKey(),
                "user.auth.refresh_token_expired"
            );
        } catch (Exception e) {
            throw new InvalidJwtException(
                ErrorCode.REFRESH_TOKEN_INVALID.getCode(),
                ErrorCode.REFRESH_TOKEN_INVALID.getMessageKey(),
                "user.auth.refresh_token_invalid"
            );
        }
    }

    /**
     * Construye la clave de firma HMAC-SHA512 a partir del secret en Base64.
     *
     * @return la clave secreta para firmar y verificar tokens
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
