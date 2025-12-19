package com.api.user_register.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF porque es una API REST, no una web con formularios HTML
                .csrf(AbstractHttpConfigurer::disable)
                // Configuramos las reglas de las URLs
                .authorizeHttpRequests(auth -> auth
                        // IMPORTANTE: Aquí debes poner la ruta exacta que definiste en tu Controller
                        .requestMatchers("/api/users/register").permitAll()
                        // Cualquier otra ruta requerirá autenticación
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
