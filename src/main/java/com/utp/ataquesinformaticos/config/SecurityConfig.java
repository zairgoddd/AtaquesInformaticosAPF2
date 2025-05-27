package com.utp.ataquesinformaticos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    
                    // Recursos estáticos
                    .requestMatchers(
                        "/css/**",
                        "/vendor/**",
                        "/js/**",
                        "/imagenes/**",
                        "/images/**",
                        "/img/**",
                        "/uploads/**",
                        "/assets/**",
                        "/recursos/**",
                        "/static/**",
                        "/webjars/**"
                ).permitAll()
                
                // Rutas públicas
                .requestMatchers(
                        "/",
                        "/search",
                        "/error",
                        "/login",
                        "/registro",
                        "/verificar-codigo",
                        "/verificar",
                        "/registrar",
                        "/ataquesinformaticos"
                ).permitAll()
                
                // Rutas específicas por rol
                .requestMatchers("/analista/**").hasRole("ANALISTA")
                .requestMatchers("/administrador/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/cliente/**").hasRole("CLIENTE")
                
                // APIs REST - ADMINISTRADOR y ANALISTA pueden gestionar datos
                .requestMatchers(HttpMethod.GET, "/api/amenazas/**").hasAnyRole("ANALISTA", "ADMINISTRADOR", "CLIENTE")
                .requestMatchers(HttpMethod.GET, "/api/ataques/**").hasAnyRole("ANALISTA", "ADMINISTRADOR", "CLIENTE")
                .requestMatchers(HttpMethod.POST, "/api/amenazas/**").hasAnyRole("ANALISTA", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/amenazas/**").hasAnyRole("ANALISTA", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/amenazas/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/api/ataques/**").hasAnyRole("ANALISTA", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/ataques/**").hasAnyRole("ANALISTA", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/ataques/**").hasRole("ADMINISTRADOR")
                
                // Rutas de gestión web - diferenciación por operación
                .requestMatchers(HttpMethod.GET, "/amenazas/**").hasAnyRole("ANALISTA", "ADMINISTRADOR", "CLIENTE")
                .requestMatchers(HttpMethod.POST, "/amenazas/nuevo").hasAnyRole("ANALISTA", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/amenazas/editar/**").hasAnyRole("ANALISTA", "ADMINISTRADOR")
                .requestMatchers("/amenazas/eliminar/**").hasRole("ADMINISTRADOR")
                
                .requestMatchers(HttpMethod.GET, "/ataques/**").hasAnyRole("ANALISTA", "ADMINISTRADOR", "CLIENTE")
                .requestMatchers(HttpMethod.POST, "/ataques/nuevo").hasAnyRole("ANALISTA", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/ataques/editar/**").hasAnyRole("ANALISTA", "ADMINISTRADOR")
                .requestMatchers("/ataques/eliminar/**").hasRole("ADMINISTRADOR")
                
                // Reportes accesibles para todos los roles autenticados
                .requestMatchers("/reportes/**").hasAnyRole("ANALISTA", "ADMINISTRADOR", "CLIENTE")
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/redirect", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/ataquesinformaticos")
                .permitAll()
            );
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}