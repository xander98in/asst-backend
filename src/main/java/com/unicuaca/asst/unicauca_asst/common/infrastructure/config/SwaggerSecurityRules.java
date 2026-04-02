package com.unicuaca.asst.unicauca_asst.common.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

/**
 * Reglas de seguridad para los recursos de documentación Swagger/OpenAPI.
 *
 * <p>Define el {@link SecurityFilterChain} con {@code @Order(0)} (máxima prioridad)
 * para las rutas de Swagger UI y los endpoints de API docs.
 * Permite acceso público sin autenticación a toda la documentación.</p>
 */
@Configuration
@RequiredArgsConstructor
public class SwaggerSecurityRules {

    private final JwtAuthFilter jwtAuthFilter;
    private final SecurityAuthenticationEntryPoint authenticationEntryPoint;
    private final SecurityAccessDeniedHandler accessDeniedHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * SecurityFilterChain para recursos públicos de documentación y desarrollo.
     *
     * <p>Permite acceso público a:</p>
     * <ul>
     *   <li>{@code /swagger-ui/**} — interfaz web de Swagger UI</li>
     *   <li>{@code /swagger-ui.html} — redirección de Swagger UI</li>
     *   <li>{@code /v3/api-docs/**} — especificación OpenAPI JSON</li>
     *   <li>{@code /v3/api-docs} — especificación OpenAPI JSON raíz</li>
     *   <li>{@code /swagger-resources/**} — recursos de Swagger</li>
     *   <li>{@code /webjars/**} — dependencias estáticas de Swagger UI</li>
     *   <li>{@code /test-login.html} — página de prueba de Google Login (solo desarrollo)</li>
     *   <li>{@code /static/**} — recursos estáticos servidos por Spring Boot</li>
     * </ul>
     *
     * @param http configuración de seguridad HTTP
     * @return la cadena de filtros configurada
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    @Order(0)
    public SecurityFilterChain swaggerSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher(
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/v3/api-docs",
                "/swagger-resources/**",
                "/webjars/**",
                "/test-login.html",
                "/static/**"
            )
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(formLogin -> formLogin.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
