package com.turismo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de hospedagem
 * Retorna todas as informações da hospedagem incluindo dados do usuário
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospedagemResponseDTO {

    private Long id;

    private Long pontoId;

    private Long usuarioId;

    private String usuarioNome;

    private String nome;

    private String endereco;

    private String telefone;

    private Double precoMedio;

    private String tipo;

    private String linkReserva;

    private String createdAt;

    private String updatedAt;
}