package com.backend.producto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Permitir acceso público a la documentación de Swagger
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/producto/v1/api-docs/**").permitAll()
                // Requerir autenticación para cualquier otra ruta (incluyendo /api/v1/productos)
                .anyRequest().authenticated()
            )
            // Habilitar OAuth2 Resource Server para validar tokens JWT
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
            
        return http.build();
    }
}