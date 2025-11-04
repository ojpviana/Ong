package com.protecao.animais.repository; // Use o nome do seu pacote

import org.springframework.data.jpa.repository.JpaRepository;

import com.protecao.animais.model.Animal; // Use o nome do seu pacote

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    
}