package com.protecao.animais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.protecao.animais.model.Ong;

@Repository
public interface OngRepository extends JpaRepository<Ong, Long> {
}