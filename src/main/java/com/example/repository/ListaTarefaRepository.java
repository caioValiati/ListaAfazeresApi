package com.example.repository;

import com.example.model.ListaTarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaTarefaRepository extends JpaRepository<ListaTarefa, Long> {
}
