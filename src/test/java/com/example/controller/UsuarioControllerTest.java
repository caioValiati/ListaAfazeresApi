package com.example.controller;

import com.example.model.Usuario;
import com.example.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {
    @Mock
    private UsuarioService service;

    @InjectMocks
    private UsuarioController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarUsuarios() {
        Usuario usuario1 = new Usuario();
        usuario1.setNome("João");
        Usuario usuario2 = new Usuario();
        usuario2.setNome("Maria");

        when(service.listarTodos()).thenReturn(Arrays.asList(usuario1, usuario2));

        List<Usuario> usuarios = controller.listar();

        assertEquals(2, usuarios.size());
        verify(service).listarTodos();
    }

    @Test
    void testSalvarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@exemplo.com");

        when(service.salvar(usuario)).thenReturn(usuario);

        Usuario usuarioSalvo = controller.salvar(usuario);

        assertNotNull(usuarioSalvo);
        assertEquals("Carlos", usuarioSalvo.getNome());
        verify(service).salvar(usuario);
    }
}