package com.example.service;

import com.example.model.ListaTarefa;
import com.example.model.Usuario;
import com.example.repository.ListaTarefaRepository;
import com.example.repository.UsuarioRepository;
import com.example.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ListaTarefaService {
    private final ListaTarefaRepository listaTarefaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SecurityUtils securityUtils;

    @Autowired
    public ListaTarefaService(ListaTarefaRepository listaTarefaRepository, 
                            UsuarioRepository usuarioRepository, 
                            SecurityUtils securityUtils) {
        this.listaTarefaRepository = listaTarefaRepository;
        this.usuarioRepository = usuarioRepository;
        this.securityUtils = securityUtils;
    }

    // Criar lista
    @Transactional
    public ListaTarefa criarListaTarefa(ListaTarefa listaTarefa) {
        Long userId = securityUtils.getCurrentUserId();
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        listaTarefa.setUsuario(usuario);
        return listaTarefaRepository.save(listaTarefa);
    }

    // Listar todas as listas do usuário logado
    public List<ListaTarefa> listarListaTarefa() {
        Long userId = securityUtils.getCurrentUserId();
        return listaTarefaRepository.findByUsuarioId(userId);
    }

    // Buscar lista por ID
    public Optional<ListaTarefa> buscarListaTarefaPorId(Long id) {
        Long userId = securityUtils.getCurrentUserId();
        return listaTarefaRepository.findById(id)
                .filter(lista -> lista.getUsuario().getId().equals(userId));
    }

    // Atualizar lista
    @Transactional
    public ListaTarefa atualizarListaTarefa(Long id, ListaTarefa detalhesListaTarefa) {
        Long userId = securityUtils.getCurrentUserId();
        Optional<ListaTarefa> listaTarefaOptional = listaTarefaRepository.findById(id)
                .filter(lista -> lista.getUsuario().getId().equals(userId));
        if (listaTarefaOptional.isEmpty()) {
            return null;
        }

        ListaTarefa listaTarefa = listaTarefaOptional.get();
        listaTarefa.setTitulo(detalhesListaTarefa.getTitulo());
        listaTarefa.setCor(detalhesListaTarefa.getCor());
        return listaTarefaRepository.save(listaTarefa);
    }

    // Excluir a lista
    @Transactional
    public boolean excluirListaTarefa(Long id) {
        Long userId = securityUtils.getCurrentUserId();
        Optional<ListaTarefa> listaTarefaOptional = listaTarefaRepository.findById(id)
                .filter(lista -> lista.getUsuario().getId().equals(userId));
        if (listaTarefaOptional.isPresent()) {
            listaTarefaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}