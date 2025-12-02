package com.turismo.dto;

import lombok.Data;

@Data
public class RegistroRequestDTO {
    private String nome;
    private String email;
    private String senha;
}
