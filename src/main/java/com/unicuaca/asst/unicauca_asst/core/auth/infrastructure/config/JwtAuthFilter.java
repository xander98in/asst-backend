package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicuaca.asst.unicauca_asst.common.exceptions.InvalidJwtException;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.config.SecurityResponseWriter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Filtro de autenticación JWT que intercepta cada petición HTTP.
 *
 * <p>Extrae el token del header {@code Authorization} (formato {@code Bearer <token>}),
 * lo valida mediante {@link JwtService} y registra al usuario autenticado en el
 * {@link SecurityContextHolder} de Spring Security.</p>
 *
 * <p>Si el token es inválido, escribe la respuesta de error directamente en el
 * {@link HttpServletResponse} usando {@link SecurityResponseWriter}, ya que los filtros
 * no pasan por {@code @RestControllerAdvice}.</p>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    /**
     * Filtra cada petición HTTP para validar el JWT y autenticar al usuario.
     *
     * @param request     solicitud HTTP entrante
     * @param response    respuesta HTTP
     * @param filterChain cadena de filtros de Spring Security
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException      si ocurre un error de E/S
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        // Si no hay header o no es Bearer, dejar pasar sin autenticar
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        // Validar el access token
        try {
            jwtService.validateAccessToken(token);
        } catch (InvalidJwtException e) {
            SecurityResponseWriter.writeErrorResponse(
                request,
                response,
                objectMapper,
                messageSource,
                HttpServletResponse.SC_UNAUTHORIZED,
                e.getCode(),
                e.getMessage(),
                e.getUserMessage()
            );
            return;
        }

        // Extraer claims y registrar autenticación
        String email = jwtService.extractEmail(token);
        Set<String> roles = jwtService.extractRoles(token);

        List<GrantedAuthority> authorities = roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(email, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
