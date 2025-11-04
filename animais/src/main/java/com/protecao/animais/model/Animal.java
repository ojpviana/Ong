package com.protecao.animais.model; 

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String especie; // Ex: "Cão", "Gato"
    private String raca;
    private int idade; // Idade em anos (pode ser alterado para String se preferir "5 meses")
    private String statusAdocao = "DISPONIVEL"; // DISPONIVEL, ADOTADO, RESERVADO
    private String fotoUrl;

    // --- RELACIONAMENTO COM ONG ---
    
    // Indica que muitos animais pertencem a uma ONG (Many-to-One)
    @ManyToOne 
    @JoinColumn(name = "ong_id", nullable = false)
    @JsonBackReference // Evita loops infinitos na serialização JSON
    private Ong ong;

    // --- Construtores ---
    
    public Animal() {
    }

    // --- Getters e Setters ---
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getStatusAdocao() {
        return statusAdocao;
    }

    public void setStatusAdocao(String statusAdocao) {
        this.statusAdocao = statusAdocao;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public Ong getOng() {
        return ong;
    }

    public void setOng(Ong ong) {
        this.ong = ong;
    }
}