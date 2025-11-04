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
import org.springframework.web.bind.annotation.RestController;

import com.protecao.animais.model.Animal;
import com.protecao.animais.model.Ong;
import com.protecao.animais.repository.AnimalRepository;
import com.protecao.animais.repository.OngRepository;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private OngRepository ongRepository;

    // --- Endpoint para Cadastrar um Novo Animal (POST) ---
    // A requisição deve ser feita para /animais/ongs/{ongId}
    @PostMapping("/ongs/{ongId}")
    public ResponseEntity<Animal> cadastrar(
            @PathVariable Long ongId,
            @RequestBody Animal animal
    ) {
        // 1. Verificar se a ONG existe
        Optional<Ong> ongExistente = ongRepository.findById(ongId);

        if (ongExistente.isEmpty()) {
            // Retorna 404 Not Found se a ONG não for encontrada
            return ResponseEntity.notFound().build(); 
        }

        // 2. Associar o animal à ONG encontrada
        animal.setOng(ongExistente.get());

        // 3. Salvar o animal
        Animal animalSalvo = animalRepository.save(animal);

        // 4. Retornar 201 Created com o animal salvo
        return ResponseEntity.status(HttpStatus.CREATED).body(animalSalvo);
    }

    // Endpoint para listar todos os animais


    // Outros métodos CRUD virão aqui (GET, PUT, DELETE)]]

    // Endpoint para listar todos os animais
    @GetMapping
    public ResponseEntity<List<Animal>> listarTodos() {
        // Usa o método findAll() que o JpaRepository fornece
        List<Animal> animais = animalRepository.findAll();

        // Retorna a lista de animais com status 200 OK
        return ResponseEntity.ok(animais);
    }

    // Endpoint para buscar um animal específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Animal> buscarPorId(@PathVariable Long id) {
        // Usa Optional para lidar com a possibilidade de o animal não existir
        Optional<Animal> animalOptional = animalRepository.findById(id);

        if (animalOptional.isPresent()) {
            // Retorna 200 OK com o objeto Animal
            return ResponseEntity.ok(animalOptional.get());
        } else {
            // Retorna 404 Not Found se o animal não for encontrado
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para atualizar um animal existente
    @PutMapping("/{id}")
    public ResponseEntity<Animal> atualizar(
            @PathVariable Long id, 
            @RequestBody Animal animalAtualizado
    ) {
        // 1. Verificar se o animal existe
        if (!animalRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // 2. Se a ONG foi passada no corpo, garante que ela existe
        if (animalAtualizado.getOng() != null && animalAtualizado.getOng().getId() != null) {
            if (!ongRepository.existsById(animalAtualizado.getOng().getId())) {
                return ResponseEntity.badRequest().build(); // Ou um status mais detalhado
            }
            // Se a ONG for válida, ela será associada automaticamente
        }

        // 3. Força o ID do animal a ser o mesmo da URL para garantir a atualização
        animalAtualizado.setId(id);

        // 4. Salva o objeto. Como o ID existe, ele será atualizado.
        Animal animalSalvo = animalRepository.save(animalAtualizado);

        return ResponseEntity.ok(animalSalvo);
    }

    // Endpoint para deletar um animal
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        // 1. Verifica se o animal existe
        if (!animalRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // 2. Deleta o registro pelo ID
        animalRepository.deleteById(id);

        // 3. Retorna o status 204 No Content para indicar sucesso sem corpo de resposta
        return ResponseEntity.noContent().build();
    }
}