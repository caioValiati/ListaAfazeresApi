package com.example.dto;
import com.example.dto.BaseResponse;

public class AuthResponse extends BaseResponse {
    private Long id;
    private String nome;
    private String email;
    private String token;

    public AuthResponse(boolean success, String message) {
        super(success, message);
    }

    public AuthResponse(boolean success, String message, Long id, String nome, String email, String token) {
        super(success, message);
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.token = token;
    }

    // Getters e Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}