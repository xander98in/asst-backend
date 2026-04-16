package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.config;

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
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

/**
 * Reglas de seguridad del módulo de informes.
 *
 * <p>Define el {@link SecurityFilterChain} con {@code @Order(5)} para las rutas del módulo
 * de informes de riesgo psicosocial.</p>
 *
 * <p>Todas las operaciones requieren rol {@code ADMIN} o {@code PROFESIONAL_ASST}.</p>
 */
@Configuration
@RequiredArgsConstructor
public class ReportsSecurityRules {

    private final JwtAuthFilter jwtAuthFilter;
    private final SecurityAuthenticationEntryPoint authenticationEntryPoint;
    private final SecurityAccessDeniedHandler accessDeniedHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * SecurityFilterChain para el módulo de informes.
     *
     * <p>Rutas cubiertas:</p>
     * <ul>
     *   <li>{@code /asst/reports/**}</li>
     * </ul>
     *
     * @param http configuración de seguridad HTTP
     * @return la cadena de filtros configurada
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    @Order(5)
    public SecurityFilterChain reportsSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher("/asst/reports/**")
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
                // Informes y Espacios de Análisis
                .requestMatchers(HttpMethod.GET, "/asst/reports/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.POST, "/asst/reports/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.PUT, "/asst/reports/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.DELETE, "/asst/reports/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
