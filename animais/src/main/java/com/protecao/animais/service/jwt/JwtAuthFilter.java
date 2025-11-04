package com.protecao.animais.service.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService; // Para validar o token e extrair o username

    @Autowired
    private UserDetailsService userDetailsService; // Para carregar os dados do usuário

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        // 1. Tenta extrair o cabeçalho de Autorização
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userName;

        // Verifica se o token JWT existe e tem o formato "Bearer <token>"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extrai o token e o username
        jwt = authHeader.substring(7); // Remove "Bearer " (7 caracteres)
        userName = jwtService.extractUsername(jwt); // Este método será criado no JwtService

        // 3. Verifica se o usuário é válido e se não está autenticado ainda
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Carrega os dados do usuário (que implementa UserDetails)
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            
            // 4. Valida o token (Este método será criado no JwtService)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                
                // Cria o objeto de autenticação do Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 5. Define o usuário como autenticado no contexto do Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}