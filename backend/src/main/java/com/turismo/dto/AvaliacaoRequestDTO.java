package com.turismo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AvaliacaoRequestDTO {

    @NotNull(message = "ID do ponto turístico é obrigatório")
    private Long pontoId;

    @NotNull(message = "Nota é obrigatória")
    @Min(value = 1, message = "Nota deve ser entre 1 e 5")
    @Max(value = 5, message = "Nota deve ser entre 1 e 5")
    private Integer nota;

    @Size(max = 500, message = "Comentário deve ter no máximo 500 caracteres")
    private String comentario;

    // Construtores
    public AvaliacaoRequestDTO() {
    }

    public AvaliacaoRequestDTO(Long pontoId, Integer nota, String comentario) {
        this.pontoId = pontoId;
        this.nota = nota;
        this.comentario = comentario;
    }

    // Getters
    public Long getPontoId() { return pontoId; }
    public Integer getNota() { return nota; }
    public String getComentario() { return comentario; }

    // Setters
    public void setPontoId(Long pontoId) { this.pontoId = pontoId; }
    public void setNota(Integer nota) { this.nota = nota; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}