package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.config.SecurityAccessDeniedHandler;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.config.SecurityAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

/**
 * Reglas de seguridad para la gestión de usuarios del sistema.
 *
 * <p>Define el {@link SecurityFilterChain} con {@code @Order(2)} para las rutas
 * {@code /asst/users/**}. El endpoint {@code /me} es accesible por cualquier usuario
 * autenticado; todas las demás operaciones (consultas, creación, actualización,
 * cambio de estado y eliminación) requieren rol {@code ADMIN}.</p>
 *
 * <p>Reglas de acceso:</p>
 * <ul>
 *   <li>{@code GET /asst/users/me} → cualquier usuario autenticado</li>
 *   <li>{@code GET /asst/users/list-paged} → solo ADMIN</li>
 *   <li>{@code GET /asst/users/list-paged-filtered} → solo ADMIN</li>
 *   <li>{@code GET /asst/users/{id}} → solo ADMIN</li>
 *   <li>{@code POST /asst/users} → solo ADMIN</li>
 *   <li>{@code PUT /asst/users/**} → solo ADMIN</li>
 *   <li>{@code DELETE /asst/users/**} → solo ADMIN</li>
 *   <li>{@code PATCH /asst/users/**} → solo ADMIN</li>
 * </ul>
 */
@Configuration
@RequiredArgsConstructor
public class AuthUserSecurityRules {

    private final JwtAuthFilter jwtAuthFilter;
    private final SecurityAuthenticationEntryPoint authenticationEntryPoint;
    private final SecurityAccessDeniedHandler accessDeniedHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * SecurityFilterChain para la gestión de usuarios del sistema ({@code /asst/users/**}).
     *
     * @param http configuración de seguridad HTTP
     * @return la cadena de filtros configurada
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    @Order(2)
    public SecurityFilterChain usersSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher("/asst/users/**")
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
                // Catálogos de auth — solo ADMIN
                .requestMatchers(HttpMethod.GET, "/asst/users/catalog/**").hasRole("ADMIN")

                // /me — cualquier usuario autenticado
                .requestMatchers(HttpMethod.GET, "/asst/users/me").authenticated()

                // Consultas — solo ADMIN
                .requestMatchers(HttpMethod.GET, "/asst/users/list-paged").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/asst/users/list-paged-filtered").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/asst/users/{id}").hasRole("ADMIN")

                // Comandos — solo ADMIN
                .requestMatchers(HttpMethod.POST, "/asst/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/asst/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/asst/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/asst/users/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
