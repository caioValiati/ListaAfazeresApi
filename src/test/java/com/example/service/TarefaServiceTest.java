package com.example.service;

import com.example.model.ListaTarefa;
import com.example.model.Tarefa;
import com.example.repository.ListaTarefaRepository;
import com.example.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TarefaServiceTest {
    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private ListaTarefaRepository listaTarefaRepository;

    @InjectMocks
    private TarefaService tarefaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarTarefaComLista() {
        ListaTarefa lista = new ListaTarefa("Trabalho");
        Tarefa tarefa = new Tarefa("Reunião", 2);

        when(listaTarefaRepository.findById(1L)).thenReturn(Optional.of(lista));
        when(tarefaRepository.save(tarefa)).thenReturn(tarefa);

        Tarefa tarefaCriada = tarefaService.criarTarefa(tarefa, 1L);

        assertNotNull(tarefaCriada);
        assertEquals(lista, tarefaCriada.getListaTarefa());
        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void testMarcarTarefaComoConcluida() {
        Tarefa tarefa = new Tarefa("Estudar", 1);
        tarefa.setId(1L);

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(tarefa)).thenReturn(tarefa);

        Tarefa tarefaAtualizada = tarefaService.marcarTarefaComoConcluida(1L);

        assertTrue(tarefaAtualizada.isCompletada());
        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void testBuscarTarefasPorPrioridade() {
        Tarefa tarefa1 = new Tarefa("Alta prioridade 1", 3);
        Tarefa tarefa2 = new Tarefa("Alta prioridade 2", 3);

        when(tarefaRepository.findByPrioridade(3)).thenReturn(Arrays.asList(tarefa1, tarefa2));

        List<Tarefa> tarefas = tarefaService.getTarefasPorPrioridade(3);

        assertEquals(2, tarefas.size());
        verify(tarefaRepository).findByPrioridade(3);
    }
}