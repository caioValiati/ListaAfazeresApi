package com.example.controller;

import com.example.model.Tarefa;
import com.example.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {
    private final TarefaService tarefaService;

    @Autowired
    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    // Listar todas tarefas urgentes
    @GetMapping
    public ResponseEntity<List<Tarefa>> findAll(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) Integer prioridade
    ) {
        List<Tarefa> tarefas;

        if (prioridade != null && prioridade == 3) {
            tarefas = tarefaService.getTarefasPorPrioridade(3);
        } else if (completed != null) {
            tarefas = tarefaService.getTarefasCompletadas(completed);
        } else {
            tarefas = tarefaService.getAllTarefas();
        }

        return new ResponseEntity<>(tarefas, HttpStatus.OK);
    }

    // Criar tarefa
    @PostMapping
    public ResponseEntity<Tarefa> createTask(@RequestBody Tarefa tarefa, @RequestParam(required = false) Long id) {
        Tarefa createdTask = tarefaService.criarTarefa(tarefa, id);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // Listar tarefas por lista
    @GetMapping("/lista/{listaId}")
    public ResponseEntity<List<Tarefa>> findAllByListaId(@PathVariable Long listaId) {
        List<Tarefa> tarefas = tarefaService.getTarefasByListaId(listaId);
        return new ResponseEntity<>(tarefas, HttpStatus.OK);
    }

    // Buscar tarefa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> findById(@PathVariable Long id) {
        Optional<Tarefa> tarefa = tarefaService.getTarefaById(id);

        return tarefa.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Atualizar tarefa
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> update(@PathVariable Long id, @RequestBody Tarefa detalhesTarefa) {
        Tarefa tarefaAtualizada = tarefaService.atualizarTarefa(id, detalhesTarefa);

        if (tarefaAtualizada == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tarefaAtualizada, HttpStatus.OK);
    }

    // Marcar tarefa como concluída
    @PatchMapping("/{id}/completar")
    public ResponseEntity<Tarefa> markAsCompleted(@PathVariable Long id) {
        Tarefa tarefaCompletada = tarefaService.marcarTarefaComoConcluida(id);
        if (tarefaCompletada != null) {
            return new ResponseEntity<>(tarefaCompletada, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Desmarcar tarefa como concluída
    @PatchMapping("/{id}/incompletar")
    public ResponseEntity<Tarefa> markAsUncompleted(@PathVariable Long id) {
        Tarefa tarefaCompletada = tarefaService.desmarcarTarefaComoConcluida(id);
        if (tarefaCompletada != null) {
            return new ResponseEntity<>(tarefaCompletada, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Excluir tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<Tarefa> delete(@PathVariable Long id) {
        boolean deleted = tarefaService.excluirTarefa(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
