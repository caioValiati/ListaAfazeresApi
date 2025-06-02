package com.example.repository;

import com.example.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByListaTarefaId(Long listaId);
    List<Tarefa> findByPrioridade(int prioridade);
    List<Tarefa> findByCompletada(boolean completada);
    List<Tarefa> findByListaTarefaUsuarioId(Long usuarioId);
    List<Tarefa> findByListaTarefaIdAndListaTarefaUsuarioId(Long listaId, Long usuarioId);
    List<Tarefa> findByPrioridadeAndListaTarefaUsuarioId(int prioridade, Long usuarioId);    
    List<Tarefa> findByCompletadaAndListaTarefaUsuarioId(boolean completada, Long usuarioId);
}