package com.turismo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ComentarioRequestDTO {

    @NotNull(message = "ID do ponto turístico é obrigatório")
    private Long pontoId;

    @NotBlank(message = "Comentário não pode estar vazio")
    @Size(max = 500, message = "Comentário deve ter no máximo 500 caracteres")
    private String texto;

    // Metadados opcionais
    private String device;
    private String userAgent;

    // Construtores
    public ComentarioRequestDTO() {
    }

    public ComentarioRequestDTO(Long pontoId, String texto) {
        this.pontoId = pontoId;
        this.texto = texto;
    }

    // Getters
    public Long getPontoId() { return pontoId; }
    public String getTexto() { return texto; }
    public String getDevice() { return device; }
    public String getUserAgent() { return userAgent; }

    // Setters
    public void setPontoId(Long pontoId) { this.pontoId = pontoId; }
    public void setTexto(String texto) { this.texto = texto; }
    public void setDevice(String device) { this.device = device; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
}