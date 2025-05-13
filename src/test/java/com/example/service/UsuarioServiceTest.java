package com.example.service;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {
    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarTodos() {
        Usuario usuario1 = new Usuario();
        usuario1.setNome("João");
        Usuario usuario2 = new Usuario();
        usuario2.setNome("Maria");

        when(repository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        List<Usuario> usuarios = service.listarTodos();

        assertEquals(2, usuarios.size());
        verify(repository).findAll();
    }

    @Test
    void testSalvarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@exemplo.com");

        when(repository.save(usuario)).thenReturn(usuario);

        Usuario usuarioSalvo = service.salvar(usuario);

        assertNotNull(usuarioSalvo);
        assertEquals("Carlos", usuarioSalvo.getNome());
        verify(repository).save(usuario);
    }
}