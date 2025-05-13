package com.example.controller;

import com.example.model.Tarefa;
import com.example.service.TarefaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TarefaControllerTest {
    @Mock
    private TarefaService tarefaService;

    @InjectMocks
    private TarefaController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllTarefas() {
        Tarefa tarefa1 = new Tarefa("Tarefa 1", 2);
        Tarefa tarefa2 = new Tarefa("Tarefa 2", 3);

        when(tarefaService.getAllTarefas()).thenReturn(Arrays.asList(tarefa1, tarefa2));

        ResponseEntity<List<Tarefa>> response = controller.findAll(null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(tarefaService).getAllTarefas();
    }

    @Test
    void testFindAllTarefasPorPrioridade() {
        Tarefa tarefa1 = new Tarefa("Tarefa Alta Prioridade 1", 3);
        Tarefa tarefa2 = new Tarefa("Tarefa Alta Prioridade 2", 3);

        when(tarefaService.getTarefasPorPrioridade(3)).thenReturn(Arrays.asList(tarefa1, tarefa2));

        ResponseEntity<List<Tarefa>> response = controller.findAll(null, 3);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(tarefaService).getTarefasPorPrioridade(3);
    }

    @Test
    void testFindAllTarefasCompletadas() {
        Tarefa tarefa1 = new Tarefa("Tarefa Completa 1", 1);
        tarefa1.setCompletada(true);
        Tarefa tarefa2 = new Tarefa("Tarefa Completa 2", 2);
        tarefa2.setCompletada(true);

        when(tarefaService.getTarefasCompletadas(true)).thenReturn(Arrays.asList(tarefa1, tarefa2));

        ResponseEntity<List<Tarefa>> response = controller.findAll(true, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(tarefaService).getTarefasCompletadas(true);
    }

    @Test
    void testCreateTask() {
        Tarefa tarefa = new Tarefa("Nova Tarefa", 2);

        when(tarefaService.criarTarefa(tarefa, null)).thenReturn(tarefa);

        ResponseEntity<Tarefa> response = controller.createTask(tarefa, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Nova Tarefa", response.getBody().getDescricao());
        verify(tarefaService).criarTarefa(tarefa, null);
    }

    @Test
    void testFindById() {
        Tarefa tarefa = new Tarefa("Tarefa Específica", 1);
        tarefa.setId(1L);

        when(tarefaService.getTarefaById(1L)).thenReturn(Optional.of(tarefa));

        ResponseEntity<Tarefa> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Tarefa Específica", response.getBody().getDescricao());
        verify(tarefaService).getTarefaById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(tarefaService.getTarefaById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Tarefa> response = controller.findById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(tarefaService).getTarefaById(999L);
    }

    @Test
    void testUpdateTarefa() {
        Tarefa tarefaExistente = new Tarefa("Tarefa Antiga", 1);
        tarefaExistente.setId(1L);

        Tarefa tarefaAtualizada = new Tarefa("Tarefa Atualizada", 2);
        tarefaAtualizada.setCompletada(true);

        when(tarefaService.atualizarTarefa(1L, tarefaAtualizada)).thenReturn(tarefaAtualizada);

        ResponseEntity<Tarefa> response = controller.update(1L, tarefaAtualizada);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Tarefa Atualizada", response.getBody().getDescricao());
        assertTrue(response.getBody().isCompletada());
        verify(tarefaService).atualizarTarefa(1L, tarefaAtualizada);
    }

    @Test
    void testUpdateTarefaNotFound() {
        Tarefa tarefaAtualizada = new Tarefa("Tarefa Atualizada", 2);

        when(tarefaService.atualizarTarefa(999L, tarefaAtualizada)).thenReturn(null);

        ResponseEntity<Tarefa> response = controller.update(999L, tarefaAtualizada);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(tarefaService).atualizarTarefa(999L, tarefaAtualizada);
    }

    @Test
    void testMarkAsCompleted() {
        Tarefa tarefaCompletada = new Tarefa("Tarefa para Completar", 1);
        tarefaCompletada.setId(1L);
        tarefaCompletada.setCompletada(true);

        when(tarefaService.marcarTarefaComoConcluida(1L)).thenReturn(tarefaCompletada);

        ResponseEntity<Tarefa> response = controller.markAsCompleted(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isCompletada());
        verify(tarefaService).marcarTarefaComoConcluida(1L);
    }

    @Test
    void testMarkAsCompletedNotFound() {
        when(tarefaService.marcarTarefaComoConcluida(999L)).thenReturn(null);

        ResponseEntity<Tarefa> response = controller.markAsCompleted(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(tarefaService).marcarTarefaComoConcluida(999L);
    }

    @Test
    void testDeleteTarefa() {
        when(tarefaService.excluirTarefa(1L)).thenReturn(true);

        ResponseEntity<Tarefa> response = controller.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(tarefaService).excluirTarefa(1L);
    }

    @Test
    void testDeleteTarefaNotFound() {
        when(tarefaService.excluirTarefa(999L)).thenReturn(false);

        ResponseEntity<Tarefa> response = controller.delete(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(tarefaService).excluirTarefa(999L);
    }
}