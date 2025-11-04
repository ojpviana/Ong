package com.protecao.animais.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protecao.animais.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método especial para buscar o usuário pelo nome de usuário (username)
    Optional<Usuario> findByUsername(String username);
}