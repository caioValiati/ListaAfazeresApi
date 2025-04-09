package com.example.service;

import com.example.model.ListaTarefa;
import com.example.repository.ListaTarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ListaTarefaService {
    private final ListaTarefaRepository listaTarefaRepository;

    @Autowired
    public ListaTarefaService(ListaTarefaRepository listaTarefaRepository) {
        this.listaTarefaRepository = listaTarefaRepository;
    }

    // Criar lista
    @Transactional
    public ListaTarefa criarListaTarefa(ListaTarefa listaTarefa) {
        return listaTarefaRepository.save(listaTarefa);
    }

    // Listar todas as listas
    public List<ListaTarefa> listarListaTarefa() {
        return listaTarefaRepository.findAll();
    }

    // Buscar lista por ID
    public Optional<ListaTarefa> buscarListaTarefaPorId(Long id) {
        return listaTarefaRepository.findById(id);
    }

    // Atualizar lista
    @Transactional
    public ListaTarefa atualizarListaTarefa(Long id, ListaTarefa detalhesListaTarefa) {
        Optional<ListaTarefa> listaTarefaOptional = listaTarefaRepository.findById(id);
        if (listaTarefaOptional.isEmpty()) {
            return null;
        }

        ListaTarefa listaTarefa = listaTarefaOptional.get();
        listaTarefa.setTitle(detalhesListaTarefa.getTitle());
        return listaTarefaRepository.save(listaTarefa);
    }

    // Excluir a lista
    @Transactional
    public boolean excluirListaTarefa(Long id) {
        if (listaTarefaRepository.existsById(id)) {
            listaTarefaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
