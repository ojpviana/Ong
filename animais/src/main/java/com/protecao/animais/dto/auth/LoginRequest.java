package com.protecao.animais.dto.auth;

public class LoginRequest {

    private String username;
    private String password;

    // Construtor vazio
    public LoginRequest() {}

    // Getters e Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}