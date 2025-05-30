package com.example.service;

import com.example.model.ListaTarefa;
import com.example.model.Tarefa;
import com.example.repository.ListaTarefaRepository;
import com.example.repository.TarefaRepository;
import com.example.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {
    private final TarefaRepository tarefaRepository;
    private final ListaTarefaRepository listaTarefaRepository;
    private final SecurityUtils securityUtils;

    @Autowired
    public TarefaService(TarefaRepository tarefaRepository, ListaTarefaRepository listaTarefaRepository, SecurityUtils securityUtils) {
        this.tarefaRepository = tarefaRepository;
        this.listaTarefaRepository = listaTarefaRepository;
        this.securityUtils = securityUtils;
    }

    // Criar tarefa
    @Transactional
    public Tarefa criarTarefa(Tarefa tarefa, Long listId) {
        Long userId = securityUtils.getCurrentUserId();
        if (listId != null) {
            ListaTarefa listaTarefa = listaTarefaRepository.findById(listId)
                    .filter(lista -> lista.getUsuario().getId().equals(userId))
                    .orElseThrow(() -> new RuntimeException("Lista de tarefas não encontrada ou não pertence ao usuário"));
            tarefa.setListaTarefa(listaTarefa);
        } else {
            throw new RuntimeException("ID da lista de tarefas é obrigatório");
        }
        return tarefaRepository.save(tarefa);
    }

    // Listar todas tarefas do usuário logado
    public List<Tarefa> getAllTarefas() {
        Long userId = securityUtils.getCurrentUserId();
        return tarefaRepository.findByListaTarefaUsuarioId(userId);
    }

    // Listar tarefas por lista, verificando se pertence ao usuário
    public List<Tarefa> getTarefasByListaId(Long listaId) {
        Long userId = securityUtils.getCurrentUserId();
        return tarefaRepository.findByListaTarefaIdAndListaTarefaUsuarioId(listaId, userId);
    }

    // Buscar tarefa por ID, verificando se pertence ao usuário
    public Optional<Tarefa> getTarefaById(Long id) {
        Long userId = securityUtils.getCurrentUserId();
        return tarefaRepository.findById(id)
                .filter(tarefa -> tarefa.getListaTarefa().getUsuario().getId().equals(userId));
    }

    // Listar tarefas por prioridade do usuário logado
    public List<Tarefa> getTarefasPorPrioridade(int prioridade) {
        Long userId = securityUtils.getCurrentUserId();
        return tarefaRepository.findByPrioridadeAndListaTarefaUsuarioId(prioridade, userId);
    }

    // Listar tarefas por status de conclusão do usuário logado
    @Transactional
    public List<Tarefa> getTarefasCompletadas(boolean completada) {
        Long userId = securityUtils.getCurrentUserId();
        return tarefaRepository.findByCompletadaAndListaTarefaUsuarioId(completada, userId);
    }

    // Atualizar tarefa
    @Transactional
    public Tarefa atualizarTarefa(Long id, Tarefa detalheTarefa) {
        Long userId = securityUtils.getCurrentUserId();
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id)
                .filter(tarefa -> tarefa.getListaTarefa().getUsuario().getId().equals(userId));
        if (tarefaOptional.isEmpty()) {
            return null;
        }

        Tarefa tarefa = tarefaOptional.get();
        tarefa.setDescricao(detalheTarefa.getDescricao());
        tarefa.setPrioridade(detalheTarefa.getPrioridade());
        if (detalheTarefa.isCompletada() != tarefa.isCompletada()) {
            tarefa.setCompletada(detalheTarefa.isCompletada());
        }
        return tarefaRepository.save(tarefa);
    }

    // Marcar tarefa como concluída
    @Transactional
    public Tarefa marcarTarefaComoConcluida(Long id) {
        Long userId = securityUtils.getCurrentUserId();
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id)
                .filter(tarefa -> tarefa.getListaTarefa().getUsuario().getId().equals(userId));
        if (tarefaOptional.isEmpty()) {
            return null;
        }

        Tarefa tarefa = tarefaOptional.get();
        tarefa.setCompletada(true);
        return tarefaRepository.save(tarefa);
    }

    // Desmarcar tarefa como concluída
    @Transactional
    public Tarefa desmarcarTarefaComoConcluida(Long id) {
        Long userId = securityUtils.getCurrentUserId();
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id)
                .filter(tarefa -> tarefa.getListaTarefa().getUsuario().getId().equals(userId));
        if (tarefaOptional.isEmpty()) {
            return null;
        }

        Tarefa tarefa = tarefaOptional.get();
        tarefa.setCompletada(false);
        return tarefaRepository.save(tarefa);
    }

    // Excluir tarefa
    @Transactional
    public boolean excluirTarefa(Long id) {
        Long userId = securityUtils.getCurrentUserId();
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id)
                .filter(tarefa -> tarefa.getListaTarefa().getUsuario().getId().equals(userId));
        if (tarefaOptional.isPresent()) {
            tarefaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}