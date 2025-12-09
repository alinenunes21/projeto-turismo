package com.turismo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospedagemRequestDTO {

    @NotNull(message = "ID do ponto turístico é obrigatório")
    private Long pontoId;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 200, message = "Nome deve ter no máximo 200 caracteres")
    private String nome;

    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    private String telefone;

    @Positive(message = "Preço médio deve ser positivo")
    private Double precoMedio;

    @Size(max = 50, message = "Tipo deve ter no máximo 50 caracteres")
    private String tipo;

    @Size(max = 500, message = "Link de reserva deve ter no máximo 500 caracteres")
    private String linkReserva;
}
