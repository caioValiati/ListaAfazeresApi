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
import com.example.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public AuthResponse registrar(RegistroRequest request) {
        try {
            if (usuarioRepository.existsByEmail(request.getEmail())) {
                return new AuthResponse(false, "Email já está em uso");
            }

            Usuario usuario = new Usuario();
            usuario.setNome(request.getNome());
            usuario.setEmail(request.getEmail());
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));

            Usuario usuarioSalvo = usuarioRepository.save(usuario);

            return new AuthResponse(true, "Usuário registrado com sucesso");

        } catch (Exception e) {
            return new AuthResponse(false, "Erro interno do servidor");
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());
            
            if (usuarioOpt.isEmpty()) {
                return new AuthResponse(false, "Email ou senha inválidos");
            }

            Usuario usuario = usuarioOpt.get();

            if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
                return new AuthResponse(false, "Email ou senha inválidos");
            }

            String token = jwtUtil.generateToken(usuario);

            return new AuthResponse(true, "Login realizado com sucesso", 
                                  usuario.getId(), usuario.getNome(), usuario.getEmail(), token);

        } catch (Exception e) {
            return new AuthResponse(false, "Erro interno do servidor: " + e.getMessage());
        }
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}