package com.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListaTarefaTest {
    @Test
    void testCriarListaTarefa() {
        ListaTarefa lista = new ListaTarefa("Trabalho");
        assertNotNull(lista);
        assertEquals("Trabalho", lista.getTitulo());
        assertTrue(lista.getTarefas().isEmpty());
    }

    @Test
    void testAdicionarTarefa() {
        ListaTarefa lista = new ListaTarefa("Pessoal");
        Tarefa tarefa = new Tarefa("Comprar mantimentos", 2);

        lista.addTarefa(tarefa);

        assertEquals(1, lista.getTarefas().size());
        assertEquals(lista, tarefa.getListaTarefa());
    }
}