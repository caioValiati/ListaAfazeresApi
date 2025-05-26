package com.example.service;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public UsuarioService(PasswordEncoder passwordEncoder, UsuarioRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = userRepository;
    }

    public Usuario criarUsuario(String username, String email, String rawPassword) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new RuntimeException("Username já existe");
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("Email já existe");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        Usuario usuario = new Usuario(username, email, encodedPassword);
        return usuarioRepository.save(usuario);
    }

    public Usuario findByUsuario(String usuario) {
        return usuarioRepository.findByUsername(usuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public boolean validarSenha(String rawPassword, String encodedPassword) {
        return !passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Usuario autenticar(String username, String rawPassword) {
        Usuario usuario = findByUsuario(username);

        if (!usuario.getEnabled()) {
            throw new RuntimeException("Usuário desabilitado");
        }

        if (validarSenha(rawPassword, usuario.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        return usuario;
    }

    public void mudarSenha(Long userId, String oldPassword, String newPassword) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (validarSenha(oldPassword, usuario.getPassword())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
    }
}