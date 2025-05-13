package com.example.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TarefaTest {
    @Test
    void testCriarTarefa() {
        Tarefa tarefa = new Tarefa("Estudar Java", 2);
        
        assertNotNull(tarefa);
        assertEquals("Estudar Java", tarefa.getDescricao());
        assertEquals(2, tarefa.getPrioridade());
        assertFalse(tarefa.isCompletada());

    }

    @Test
    void testMarcarTarefaComoConcluida() {
        Tarefa tarefa = new Tarefa("Fazer exercícios", 1);
        tarefa.setCompletada(true);

        assertTrue(tarefa.isCompletada());
    }
}