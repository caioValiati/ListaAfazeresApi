package com.example.service;

import com.example.model.ListaTarefa;
import com.example.model.Usuario;
import com.example.repository.ListaTarefaRepository;
import com.example.repository.UsuarioRepository;
import com.example.security.SecurityUtils;
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

class ListaTarefaServiceTest {

    @Mock
    private ListaTarefaRepository listaTarefaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private ListaTarefaService listaTarefaService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario("testuser", "password");
        usuario.setId(1L);
    }

    @Test
    void testCriarListaTarefa() {
        ListaTarefa lista = new ListaTarefa("Pessoal", usuario);
        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(listaTarefaRepository.save(any(ListaTarefa.class))).thenReturn(lista);

        ListaTarefa listaCriada = listaTarefaService.criarListaTarefa(lista);

        assertNotNull(listaCriada);
        assertEquals("Pessoal", listaCriada.getTitulo());
        assertEquals(usuario, listaCriada.getUsuario());
        verify(securityUtils).getCurrentUserId();
        verify(usuarioRepository).findById(1L);
        verify(listaTarefaRepository).save(lista);
    }

    @Test
    void testCriarListaTarefaUsuarioNaoEncontrado() {
        ListaTarefa lista = new ListaTarefa("Pessoal", usuario);
        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> listaTarefaService.criarListaTarefa(lista));
        verify(securityUtils).getCurrentUserId();
        verify(usuarioRepository).findById(1L);
        verify(listaTarefaRepository, never()).save(any(ListaTarefa.class));
    }

    @Test
    void testListarListaTarefa() {
        ListaTarefa lista1 = new ListaTarefa("Trabalho", usuario);
        lista1.setUsuario(usuario);
        ListaTarefa lista2 = new ListaTarefa("Pessoal", usuario);
        lista2.setUsuario(usuario);
        List<ListaTarefa> listas = Arrays.asList(lista1, lista2);

        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(listaTarefaRepository.findByUsuarioId(1L)).thenReturn(listas);

        List<ListaTarefa> result = listaTarefaService.listarListaTarefa();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Trabalho", result.get(0).getTitulo());
        assertEquals("Pessoal", result.get(1).getTitulo());
        verify(securityUtils).getCurrentUserId();
        verify(listaTarefaRepository).findByUsuarioId(1L);
    }

    @Test
    void testBuscarListaTarefaPorId() {
        ListaTarefa lista = new ListaTarefa("Trabalho", usuario);
        lista.setId(1L);
        lista.setUsuario(usuario);

        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(listaTarefaRepository.findById(1L)).thenReturn(Optional.of(lista));

        Optional<ListaTarefa> listaEncontrada = listaTarefaService.buscarListaTarefaPorId(1L);

        assertTrue(listaEncontrada.isPresent());
        assertEquals("Trabalho", listaEncontrada.get().getTitulo());
        assertEquals(usuario, listaEncontrada.get().getUsuario());
        verify(securityUtils).getCurrentUserId();
        verify(listaTarefaRepository).findById(1L);
    }

    @Test
    void testBuscarListaTarefaPorIdNaoPertenceAoUsuario() {
        ListaTarefa lista = new ListaTarefa("Trabalho", usuario);
        lista.setId(1L);
        Usuario outroUsuario = new Usuario("outro", "password");
        outroUsuario.setId(2L);
        lista.setUsuario(outroUsuario);

        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(listaTarefaRepository.findById(1L)).thenReturn(Optional.of(lista));

        Optional<ListaTarefa> listaEncontrada = listaTarefaService.buscarListaTarefaPorId(1L);

        assertFalse(listaEncontrada.isPresent());
        verify(securityUtils).getCurrentUserId();
        verify(listaTarefaRepository).findById(1L);
    }

    @Test
    void testBuscarListaTarefaPorIdNaoEncontrada() {
        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(listaTarefaRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ListaTarefa> listaEncontrada = listaTarefaService.buscarListaTarefaPorId(1L);

        assertFalse(listaEncontrada.isPresent());
        verify(securityUtils).getCurrentUserId();
        verify(listaTarefaRepository).findById(1L);
    }

    @Test
    void testAtualizarListaTarefa() {
        ListaTarefa listaExistente = new ListaTarefa("Antigo Título", usuario);
        listaExistente.setId(1L);
        listaExistente.setUsuario(usuario);
        ListaTarefa detalhesAtualizacao = new ListaTarefa("Novo Título", usuario);
        detalhesAtualizacao.setCor("#FF0000");

        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(listaTarefaRepository.findById(1L)).thenReturn(Optional.of(listaExistente));
        when(listaTarefaRepository.save(any(ListaTarefa.class))).thenReturn(listaExistente);

        ListaTarefa listaAtualizada = listaTarefaService.atualizarListaTarefa(1L, detalhesAtualizacao);

        assertNotNull(listaAtualizada);
        assertEquals("Novo Título", listaAtualizada.getTitulo());
        assertEquals("#FF0000", listaAtualizada.getCor());
        assertEquals(usuario, listaAtualizada.getUsuario());
        verify(securityUtils).getCurrentUserId();
        verify(listaTarefaRepository).findById(1L);
        verify(listaTarefaRepository).save(listaExistente);
    }

    @Test
    void testAtualizarListaTarefaNaoPertenceAoUsuario() {
        ListaTarefa listaExistente = new ListaTarefa("Antigo Título", usuario);
        listaExistente.setId(1L);
        Usuario outroUsuario = new Usuario("outro", "password");
        outroUsuario.setId(2L);
        listaExistente.setUsuario(outroUsuario);
        ListaTarefa detalhesAtualizacao = new ListaTarefa("Novo Título", usuario);

        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(listaTarefaRepository.findById(1L)).thenReturn(Optional.of(listaExistente));

        ListaTarefa listaAtualizada = listaTarefaService.atualizarListaTarefa(1L, detalhesAtualizacao);

        assertNull(listaAtualizada);
        verify(securityUtils).getCurrentUserId();
        verify(listaTarefaRepository).findById(1L);
        verify(listaTarefaRepository, never()).save(any(ListaTarefa.class));
    }

    @Test
    void testAtualizarListaTarefaNaoEncontrada() {
        ListaTarefa detalhesAtualizacao = new ListaTarefa("Novo Título", usuario);

        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(listaTarefaRepository.findById(1L)).thenReturn(Optional.empty());

        ListaTarefa listaAtualizada = listaTarefaService.atualizarListaTarefa(1L, detalhesAtualizacao);

        assertNull(listaAtualizada);
        verify(securityUtils).getCurrentUserId();
        verify(listaTarefaRepository).findById(1L);
        verify(listaTarefaRepository, never()).save(any(ListaTarefa.class));
    }

    @Test
    void testExcluirListaTarefa() {
        ListaTarefa lista = new ListaTarefa("Trabalho", usuario);
        lista.setId(1L);
        lista.setUsuario(usuario);

        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(listaTarefaRepository.findById(1L)).thenReturn(Optional.of(lista));

        boolean result = listaTarefaService.excluirListaTarefa(1L);

        assertTrue(result);
        verify(securityUtils).getCurrentUserId();
        verify(listaTarefaRepository).findById(1L);
        verify(listaTarefaRepository).deleteById(1L);
    }

    @Test
    void testExcluirListaTarefaNaoPertenceAoUsuario() {
        ListaTarefa lista = new ListaTarefa("Trabalho", usuario);
        lista.setId(1L);
        Usuario outroUsuario = new Usuario("outro", "password");
        outroUsuario.setId(2L);
        lista.setUsuario(outroUsuario);

        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(listaTarefaRepository.findById(1L)).thenReturn(Optional.of(lista));

        boolean result = listaTarefaService.excluirListaTarefa(1L);

        assertFalse(result);
        verify(securityUtils).getCurrentUserId();
        verify(listaTarefaRepository).findById(1L);
        verify(listaTarefaRepository, never()).deleteById(1L);
    }

    @Test
    void testExcluirListaTarefaNaoEncontrada() {
        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(listaTarefaRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = listaTarefaService.excluirListaTarefa(1L);

        assertFalse(result);
        verify(securityUtils).getCurrentUserId();
        verify(listaTarefaRepository).findById(1L);
        verify(listaTarefaRepository, never()).deleteById(1L);
    }
}