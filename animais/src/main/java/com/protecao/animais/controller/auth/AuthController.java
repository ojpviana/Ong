package com.protecao.animais.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.protecao.animais.dto.auth.LoginRequest;
import com.protecao.animais.dto.auth.TokenResponse;
import com.protecao.animais.model.Usuario; 
import com.protecao.animais.repository.UsuarioRepository;
import com.protecao.animais.service.jwt.JwtService; 

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    
    @Autowired
    private PasswordEncoder passwordEncoder; // Injeta o Bean no SecurityConfig

    // Endpoint para registrar um novo usuário
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        

        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            // Retorna 409 Conflict se o usuário já existe
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nome de usuário já está em uso.");
        }
        
        String senhaCriptografada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(senhaCriptografada);
        
        Usuario novoUsuario = usuarioRepository.save(usuario);
        
        novoUsuario.setPassword(null); 
        
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @Autowired
    private AuthenticationManager authenticationManager; 

    @Autowired
    private JwtService jwtService; 

    // Endpoint para Login e Geração de Token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        try {
            // 1. Tenta autenticar o usuário
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // 2. Se autenticado (não lança exceção), gera o token JWT
            Usuario usuario = (Usuario) authentication.getPrincipal(); 
            String token = jwtService.gerarToken(usuario); 

            // 3. Retorna o token no DTO
            return ResponseEntity.ok(new TokenResponse(token));

        } catch (AuthenticationException e) {
            // 4. Se a autenticação falhar (senha ou usuário errado), captura a exceção
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos.");
        }
    }
}