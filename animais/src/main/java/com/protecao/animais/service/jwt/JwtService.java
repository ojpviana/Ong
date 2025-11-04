package com.protecao.animais.service.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec; // Necessário para compilação
import javax.xml.bind.DatatypeConverter; // Necessário para Java 11+

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.protecao.animais.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;



@Service
public class JwtService {

    // Chave secreta BEM longa e configurada via variáveis de ambiente em produção
    private final String CHAVE_SECRETA = "5F1G2H3J4K5L6M7N8P9Q0R1S2T3U4V5W6X7Y8Z9A0B1C2D3E4F5G6H7I8J9K0L1M";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Verifica se o username bate e se o token não expirou
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // Usa o builder
                .setSigningKey(getSignInKey())
                .build() // Constrói o parser
                .parseClaimsJws(token)
                .getBody();
    }
    public String gerarToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        // Adiciona a role do usuário (permissão) ao payload do token
        claims.put("role", usuario.getRole());
        return criarToken(claims, usuario.getUsername());
    }

    private String criarToken(Map<String, Object> claims, String userName) {
        // Define o tempo de expiração do token (1 dia)
        long tempoExpiracaoMillis = 1000 * 60 * 60 * 24; // 24 horas

        return Jwts.builder() 
                .setClaims(claims) // Adiciona as permissões
                .setSubject(userName) // Assunto: o nome de usuário
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + tempoExpiracaoMillis)) // Data de expiração
                
                // CORREÇÃO: signWith() na versão 0.9.1 aceita o SecretKeySpec.
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                
                .compact(); // Constrói e serializa o token para String
    }

    // MÉTODO CORRIGIDO E ESTABILIZADO PARA VERSÕES ANTIGAS/NOVAS DO JAVA
    private Key getSignInKey() {
        // Usa SecretKeySpec para garantir que a Key seja do tipo correto para o signWith
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(CHAVE_SECRETA);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    // Dentro da classe JwtService
    private Key getSigningKey() {
        
        // 1. Decodificar a CHAVE_SECRETA (que deve ser Base64) em um array de bytes
        // Assumindo que você usa javax.xml.bind.DatatypeConverter para decodificar Base64
        // Se estiver usando Spring Boot mais recente, pode precisar de outra biblioteca, 
        // mas vamos manter o que é comum.
        byte[] keyBytes = DatatypeConverter.parseBase64Binary(CHAVE_SECRETA);
        
        // 2. Criar o objeto Key usando SecretKeySpec para o algoritmo HMAC (HS256)
        // SignatureAlgorithm.HS256.getJcaName() retorna "HmacSHA256"
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }
}