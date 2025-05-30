package com.example.controller;

import com.example.model.ListaTarefa;
import com.example.model.Usuario;
import com.example.service.ListaTarefaService;
import com.example.security.SecurityUtils;
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

class ListaTarefaControllerTest {

    @Mock
    private ListaTarefaService listaTarefaService;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private ListaTarefaController controller;

    private Usuario usuario;
    private ListaTarefa listaTarefa;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario("testuser", "password");
        usuario.setId(1L);
        listaTarefa = new ListaTarefa("Trabalho", usuario);
        listaTarefa.setId(1L);
        when(securityUtils.getCurrentUserId()).thenReturn(1L);
    }

    @Test
    void testCriarListaTarefa() {
        ListaTarefa novaLista = new ListaTarefa("Pessoal", null); // Client sends without usuario
        ListaTarefa listaCriada = new ListaTarefa("Pessoal", usuario);
        listaCriada.setId(2L);

        when(listaTarefaService.criarListaTarefa(any(ListaTarefa.class))).thenReturn(listaCriada);

        ResponseEntity<ListaTarefa> response = controller.criarListaTarefa(novaLista);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pessoal", response.getBody().getTitulo());
        assertEquals(usuario.getId(), response.getBody().getUsuario().getId());
        verify(listaTarefaService).criarListaTarefa(any(ListaTarefa.class));
        verify(securityUtils).getCurrentUserId();
    }

    @Test
    void testListarListaTarefa() {
        ListaTarefa lista1 = new ListaTarefa("Trabalho", usuario);
        lista1.setId(1L);
        ListaTarefa lista2 = new ListaTarefa("Pessoal", usuario);
        lista2.setId(2L);
        List<ListaTarefa> listas = Arrays.asList(lista1, lista2);

        when(listaTarefaService.listarListaTarefa()).thenReturn(listas);

        ResponseEntity<List<ListaTarefa>> response = controller.listarListaTarefa();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Trabalho", response.getBody().get(0).getTitulo());
        assertEquals("Pessoal", response.getBody().get(1).getTitulo());
        verify(listaTarefaService).listarListaTarefa();
        verify(securityUtils).getCurrentUserId();
    }

    @Test
    void testBuscarListaTarefaPorId() {
        when(listaTarefaService.buscarListaTarefaPorId(1L)).thenReturn(Optional.of(listaTarefa));

        ResponseEntity<ListaTarefa> response = controller.buscarListaTarefaPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Trabalho", response.getBody().getTitulo());
        assertEquals(usuario.getId(), response.getBody().getUsuario().getId());
        verify(listaTarefaService).buscarListaTarefaPorId(1L);
        verify(securityUtils).getCurrentUserId();
    }

    @Test
    void testBuscarListaTarefaPorIdNotFound() {
        when(listaTarefaService.buscarListaTarefaPorId(999L)).thenReturn(Optional.empty());

        ResponseEntity<ListaTarefa> response = controller.buscarListaTarefaPorId(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(listaTarefaService).buscarListaTarefaPorId(999L);
        verify(securityUtils).getCurrentUserId();
    }

    @Test
    void testAtualizarListaTarefa() {
        ListaTarefa detalhesAtualizacao = new ListaTarefa("Novo Título", null);
        detalhesAtualizacao.setCor("#FF0000");
        ListaTarefa listaAtualizada = new ListaTarefa("Novo Título", usuario);
        listaAtualizada.setId(1L);
        listaAtualizada.setCor("#FF0000");

        when(listaTarefaService.atualizarListaTarefa(1L, detalhesAtualizacao)).thenReturn(listaAtualizada);

        ResponseEntity<ListaTarefa> response = controller.atualizarListaTarefa(1L, detalhesAtualizacao);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Novo Título", response.getBody().getTitulo());
        assertEquals("#FF0000", response.getBody().getCor());
        assertEquals(usuario.getId(), response.getBody().getUsuario().getId());
        verify(listaTarefaService).atualizarListaTarefa(1L, detalhesAtualizacao);
        verify(securityUtils).getCurrentUserId();
    }

    @Test
    void testAtualizarListaTarefaNotFound() {
        ListaTarefa detalhesAtualizacao = new ListaTarefa("Novo Título", null);

        when(listaTarefaService.atualizarListaTarefa(999L, detalhesAtualizacao)).thenReturn(null);

        ResponseEntity<ListaTarefa> response = controller.atualizarListaTarefa(999L, detalhesAtualizacao);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(listaTarefaService).atualizarListaTarefa(999L, detalhesAtualizacao);
        verify(securityUtils).getCurrentUserId();
    }

    @Test
    void testRemoverListaTarefa() {
        when(listaTarefaService.excluirListaTarefa(1L)).thenReturn(true);

        ResponseEntity<ListaTarefa> response = controller.removerListaTarefa(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(listaTarefaService).excluirListaTarefa(1L);
        verify(securityUtils).getCurrentUserId();
    }

    @Test
    void testRemoverListaTarefaNotFound() {
        when(listaTarefaService.excluirListaTarefa(999L)).thenReturn(false);

        ResponseEntity<ListaTarefa> response = controller.removerListaTarefa(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(listaTarefaService).excluirListaTarefa(999L);
        verify(securityUtils).getCurrentUserId();
    }
}
