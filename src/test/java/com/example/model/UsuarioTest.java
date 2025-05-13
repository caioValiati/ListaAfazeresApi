package com.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {
    @Test
    void testCriarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@exemplo.com");

        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao@exemplo.com", usuario.getEmail());
    }

    @Test
    void testSettersEGetters() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Maria Souza");
        usuario.setEmail("maria@exemplo.com");

        assertEquals(1L, usuario.getId());
        assertEquals("Maria Souza", usuario.getNome());
        assertEquals("maria@exemplo.com", usuario.getEmail());
    }
}