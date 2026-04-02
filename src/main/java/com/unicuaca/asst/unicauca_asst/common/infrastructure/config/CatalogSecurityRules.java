package com.unicuaca.asst.unicauca_asst.common.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

/**
 * Reglas de seguridad para los endpoints de catálogos comunes.
 *
 * <p>Define el {@link SecurityFilterChain} con {@code @Order(3)} para las rutas
 * {@code /asst/catalog/**}. Permite acceso a usuarios autenticados con rol
 * {@code ADMIN} o {@code PROFESIONAL_ASST}.</p>
 *
 * <p>Rutas cubiertas:</p>
 * <ul>
 *   <li>{@code GET /asst/catalog/**} → ADMIN o PROFESIONAL_ASST</li>
 * </ul>
 */
@Configuration
@RequiredArgsConstructor
public class CatalogSecurityRules {

    private final JwtAuthFilter jwtAuthFilter;
    private final SecurityAuthenticationEntryPoint authenticationEntryPoint;
    private final SecurityAccessDeniedHandler accessDeniedHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * SecurityFilterChain para los endpoints de catálogos ({@code /asst/catalog/**}).
     *
     * @param http configuración de seguridad HTTP
     * @return la cadena de filtros configurada
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    @Order(3)
    public SecurityFilterChain catalogSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher("/asst/catalog/**")
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
                .requestMatchers(HttpMethod.GET, "/asst/catalog/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
