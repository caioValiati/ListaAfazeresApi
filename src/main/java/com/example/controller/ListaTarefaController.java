package com.example.controller;

import com.example.model.ListaTarefa;
import com.example.service.ListaTarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/listas")
public class ListaTarefaController {

    private final ListaTarefaService listaTarefaService;

    @Autowired
    public ListaTarefaController(ListaTarefaService listaTarefaService) {
        this.listaTarefaService = listaTarefaService;
    }

    // Criar listas
    @PostMapping
    public ResponseEntity<ListaTarefa> criarListaTarefa(@RequestBody ListaTarefa listaTarefa) {
        listaTarefa.setUsuario(null);
        ListaTarefa listaCriada = listaTarefaService.criarListaTarefa(listaTarefa);
        return new ResponseEntity<>(listaCriada, HttpStatus.CREATED);
    }

    // Listar todas as listas
    @GetMapping
    public ResponseEntity<List<ListaTarefa>> listarListaTarefa() {
        List<ListaTarefa> listas = listaTarefaService.listarListaTarefa();
        return new ResponseEntity<>(listas, HttpStatus.OK);
    }

    // Buscar lista por ID
    @GetMapping("/{id}")
    public ResponseEntity<ListaTarefa> buscarListaTarefaPorId(Long id) {
        Optional<ListaTarefa> lista = listaTarefaService.buscarListaTarefaPorId(id);

        return lista.map(listaTarefa -> new ResponseEntity<>(listaTarefa, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Atualizar lista
    @PutMapping("/{id}")
    public ResponseEntity<ListaTarefa> atualizarListaTarefa(@PathVariable Long id, @RequestBody ListaTarefa detalhesListaTarefa) {
        ListaTarefa listaAtualizada = listaTarefaService.atualizarListaTarefa(id, detalhesListaTarefa);

        if (listaAtualizada != null) {
            return new ResponseEntity<>(listaAtualizada, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Excluir lista
    @DeleteMapping("/{id}")
    public ResponseEntity<ListaTarefa> removerListaTarefa(@PathVariable Long id) {
        boolean deletada = listaTarefaService.excluirListaTarefa(id);

        if (deletada) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
