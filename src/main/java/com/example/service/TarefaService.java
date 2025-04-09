package com.example.service;

import com.example.model.ListaTarefa;
import com.example.model.Tarefa;
import com.example.repository.ListaTarefaRepository;
import com.example.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {
    private final TarefaRepository tarefaRepository;
    private final ListaTarefaRepository listaTarefaRepository;

    @Autowired
    public TarefaService (TarefaRepository tarefaRepository, ListaTarefaRepository listaTarefaRepository) {
        this.tarefaRepository = tarefaRepository;
        this.listaTarefaRepository = listaTarefaRepository;
    }

    // Criar uma tarefa
    @Transactional
    public Tarefa criarTarefa(Tarefa tarefa, Long listaId) {
        if(tarefa.getId() == null) {
            return null;
        }
        Optional<ListaTarefa> listaTarefaOptional = listaTarefaRepository.findById(listaId);
        listaTarefaOptional.ifPresent(tarefa::setListaTarefa);
        return tarefaRepository.save(tarefa);
    }

    // Listar todas tarefas
    public List<Tarefa> getAllTarefas() {
        return tarefaRepository.findAll();
    }

    // Listar tarefas por lista
    public List<Tarefa> getTarefasByListaId(Long listaId) {
        return tarefaRepository.findByListaTarefaID(listaId);
    }

    // Buscar tarefa por ID
    public Optional<Tarefa> getTarefaById(Long id) {
        return tarefaRepository.findById(id);
    }

    // Atualizar tarefa
    @Transactional
    public Tarefa atualizarTarefa(Long id, Tarefa detalheTarefa) {
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        if(tarefaOptional.isEmpty()) {
            return null;
        }

        Tarefa tarefa = tarefaOptional.get();
        tarefa.setTitulo(detalheTarefa.getTitulo());
        tarefa.setDescricao(detalheTarefa.getDescricao());
        if (detalheTarefa.isCompletada() != tarefa.isCompletada()) {
            tarefa.setCompletada(detalheTarefa.isCompletada());
        }
        return tarefaRepository.save(tarefa);

    }

    // Marcar tarefa como concluída
    @Transactional
    public Tarefa marcarTarefaComoConcluida(Long id) {
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        if(tarefaOptional.isEmpty()) {
            return null;
        }

        Tarefa tarefa = tarefaOptional.get();
        tarefa.setCompletada(true);
        return tarefaRepository.save(tarefa);
    }

    // Excluir tarefa
    @Transactional
    public boolean excluirTarefa(Long id) {
        if (tarefaRepository.existsById(id)) {
            tarefaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public List<Tarefa> getTarefasCompletadas(boolean completada) {
        return tarefaRepository.findByCompleted(completada);
    }
}
