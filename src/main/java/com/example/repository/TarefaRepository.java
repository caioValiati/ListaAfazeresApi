package com.example.repository;

import com.example.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    default List<Tarefa> findByListaTarefaID(Long listId) {
        return null;
    }
    default List<Tarefa> findByCompleted(boolean completed) {
        return null;
    }
    default List<Tarefa> findByPrioridade(int prioridade) {
        return null;
    }
}
