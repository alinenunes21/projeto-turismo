package com.turismo.dto;

public class LoginResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String role;
    private String message;

    // Construtor padrão
    public LoginResponseDTO() {
    }

    // Construtor com todos os parâmetros
    public LoginResponseDTO(Long id, String nome, String email, String role, String message) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.message = message;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}