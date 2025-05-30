package com.example.repository;

import com.example.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    default List<Tarefa> findByListaTarefaId(Long listaId){
        return null;
    };
    default List<Tarefa> findByPrioridade(int prioridade){
        return null;
    };
    default List<Tarefa> findByCompletada(boolean completada){
        return null;
    };
    default List<Tarefa> findByListaTarefaUsuarioId(Long usuarioId){
        return null;
    };
    default List<Tarefa> findByListaTarefaIdAndListaTarefaUsuarioId(Long listaId, Long usuarioId){
        return null;
    };
    default List<Tarefa> findByPrioridadeAndListaTarefaUsuarioId(int prioridade, Long usuarioId){
        return null;
    }
    default List<Tarefa> findByCompletadaAndListaTarefaUsuarioId(boolean completada, Long usuarioId){
        return null;
    };
}