package com.protecao.animais.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder; // NOVO
import org.springframework.security.web.SecurityFilterChain; // NOVO
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // NOVO

import com.protecao.animais.repository.UsuarioRepository; // NOVO
import com.protecao.animais.service.jwt.JwtAuthFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http, 
        JwtAuthFilter jwtAuthFilter
        ) throws Exception {
        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/auth/register", "/auth/login").permitAll() 
            .requestMatchers("/ongs/**", "/animais/**").permitAll() 

            .anyRequest().authenticated()
        )


            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository repository) {
        return username -> repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
    /**
     * 2. Define o AuthenticationManager
     * Cria a instância que será usada no AuthController para autenticar.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
