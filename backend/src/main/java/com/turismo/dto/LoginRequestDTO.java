package com.turismo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    // Construtor padrão
    public LoginRequestDTO() {
    }

    // Construtor com parâmetros
    public LoginRequestDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}