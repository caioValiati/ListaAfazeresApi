package com.example.controller;
import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegistroRequest;
import com.example.model.Usuario;
import com.example.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import com.example.controller.util.ResponseHandler;


@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final UsuarioService service;

    public AuthController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        return ResponseHandler.handleServiceCall(
            () -> service.registrar(request), 
            HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseHandler.handleServiceCall(
            () -> service.login(request)
        );
    }
}