package com.protecao.animais.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // Nome de usuário ou email para login
    private String password; // Senha criptografada
    private String role = "USUARIO_COMUM"; // Permissão (ADMIN, ONG, COMUM)

    // Construtor vazio
    public Usuario() {}

    // --- MÉTODOS OBRIGATÓRIOS DE USERDETAILS (Implementados) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna as permissões/funções (role) do usuário
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    // --- GETTERS E SETTERS PÚBLICOS (Sem duplicação) ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    // Os métodos getUsername() e getPassword() já estão definidos acima
    
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}