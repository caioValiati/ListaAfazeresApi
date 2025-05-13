package com.example.service;

import com.example.model.ListaTarefa;
import com.example.repository.ListaTarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListaTarefaServiceTest {
    @Mock
    private ListaTarefaRepository listaTarefaRepository;

    @InjectMocks
    private ListaTarefaService listaTarefaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarListaTarefa() {
        ListaTarefa lista = new ListaTarefa("Pessoal");

        when(listaTarefaRepository.save(lista)).thenReturn(lista);

        ListaTarefa listaCriada = listaTarefaService.criarListaTarefa(lista);

        assertNotNull(listaCriada);
        assertEquals("Pessoal", listaCriada.getTitulo());
        verify(listaTarefaRepository).save(lista);
    }

    @Test
    void testBuscarListaTarefaPorId() {
        ListaTarefa lista = new ListaTarefa("Trabalho");
        lista.setId(1L);

        when(listaTarefaRepository.findById(1L)).thenReturn(Optional.of(lista));

        Optional<ListaTarefa> listaEncontrada = listaTarefaService.buscarListaTarefaPorId(1L);

        assertTrue(listaEncontrada.isPresent());
        assertEquals("Trabalho", listaEncontrada.get().getTitulo());
        verify(listaTarefaRepository).findById(1L);
    }

    @Test
    void testAtualizarListaTarefa() {
        ListaTarefa listaExistente = new ListaTarefa("Antigo Título");
        listaExistente.setId(1L);

        ListaTarefa detalhesAtualizacao = new ListaTarefa("Novo Título");

        when(listaTarefaRepository.findById(1L)).thenReturn(Optional.of(listaExistente));
        when(listaTarefaRepository.save(listaExistente)).thenReturn(listaExistente);

        ListaTarefa listaAtualizada = listaTarefaService.atualizarListaTarefa(1L, detalhesAtualizacao);

        assertNotNull(listaAtualizada);
        assertEquals("Novo Título", listaAtualizada.getTitulo());
        verify(listaTarefaRepository).save(listaExistente);
    }
}