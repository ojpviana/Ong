package com.protecao.animais.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.protecao.animais.model.Ong;
import com.protecao.animais.repository.OngRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ongs")
public class OngController {

    @Autowired
    private OngRepository ongRepository;

    @GetMapping
    public List<Ong> listar() {
        return ongRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ong> buscarPorId(@PathVariable Long id) {
        Optional<Ong> ongOptional = ongRepository.findById(id);

    if (ongOptional.isPresent()) {
        return ResponseEntity.ok(ongOptional.get());
    } else {
        return ResponseEntity.notFound().build();
    }
}

    @PutMapping("/{id}")
    public ResponseEntity<Ong> atualizar(@PathVariable Long id, @Valid @RequestBody Ong ong) {
        if (!ongRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        ong.setId(id);
        ong = ongRepository.save(ong);
        return ResponseEntity.ok(ong);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!ongRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        ongRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ong adicionar(@Valid @RequestBody Ong ong) {
        return ongRepository.save(ong);
    }
}