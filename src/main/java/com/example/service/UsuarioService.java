package com.example.service;
import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegistroRequest;
import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public AuthResponse registrar(RegistroRequest request) {
        try {
            if (repository.existsByEmail(request.getEmail())) {
                return new AuthResponse(false, "Email já está em uso");
            }

            Usuario usuario = new Usuario();
            usuario.setNome(request.getNome());
            usuario.setEmail(request.getEmail());
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));

            Usuario usuarioSalvo = repository.save(usuario);

            return new AuthResponse(true, "Usuário registrado com sucesso", 
                                  usuarioSalvo.getId(), usuarioSalvo.getNome(), usuarioSalvo.getEmail());

        } catch (Exception e) {
            return new AuthResponse(false, "Erro interno do servidor");
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Optional<Usuario> usuarioOpt = repository.findByEmail(request.getEmail());
            
            if (usuarioOpt.isEmpty()) {
                return new AuthResponse(false, "Email ou senha inválidos");
            }

            Usuario usuario = usuarioOpt.get();

            if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
                return new AuthResponse(false, "Email ou senha inválidos");
            }

            return new AuthResponse(true, "Login realizado com sucesso", 
                                  usuario.getId(), usuario.getNome(), usuario.getEmail());

        } catch (Exception e) {
            return new AuthResponse(false, "Erro interno do servidor");
        }
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }
}