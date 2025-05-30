package com.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListaTarefaTest {

    @Test
    void testCriarListaTarefa() {
        Usuario usuario = new Usuario("testuser", "password");
        usuario.setId(1L);
        ListaTarefa lista = new ListaTarefa("Trabalho", usuario);

        assertNotNull(lista);
        assertEquals("Trabalho", lista.getTitulo());
        assertEquals(usuario, lista.getUsuario());
        assertTrue(lista.getTarefas().isEmpty());
        assertNotNull(lista.getCreatedAt());
        assertNotNull(lista.getUpdatedAt());
    }

    @Test
    void testAdicionarTarefa() {
        Usuario usuario = new Usuario("testuser", "password");
        usuario.setId(1L);
        ListaTarefa lista = new ListaTarefa("Pessoal", usuario);
        Tarefa tarefa = new Tarefa("Comprar mantimentos", 2);

        lista.addTarefa(tarefa);

        assertEquals(1, lista.getTarefas().size());
        assertEquals(lista, tarefa.getListaTarefa());
        assertEquals(usuario, lista.getUsuario());
    }

    @Test
    void testRemoverTarefa() {
        Usuario usuario = new Usuario("testuser", "password");
        usuario.setId(1L);
        ListaTarefa lista = new ListaTarefa("Pessoal", usuario);
        Tarefa tarefa = new Tarefa("Comprar mantimentos", 2);
        lista.addTarefa(tarefa);

        lista.removeTarefa(tarefa);

        assertTrue(lista.getTarefas().isEmpty());
        assertNull(tarefa.getListaTarefa());
        assertEquals(usuario, lista.getUsuario());
    }

    @Test
    void testSetAndGetUsuario() {
        Usuario usuario = new Usuario("testuser", "password");
        usuario.setId(1L);
        ListaTarefa lista = new ListaTarefa("Trabalho", usuario);

        Usuario novoUsuario = new Usuario("newuser", "newpassword");
        novoUsuario.setId(2L);
        lista.setUsuario(novoUsuario);

        assertEquals(novoUsuario, lista.getUsuario());
        assertEquals("Trabalho", lista.getTitulo());
    }
}