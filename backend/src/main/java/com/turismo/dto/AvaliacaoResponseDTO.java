package com.turismo.dto;

import java.time.LocalDateTime;

public class AvaliacaoResponseDTO {
    private Long id;
    private Long pontoId;
    private Long usuarioId;
    private String nomeUsuario; // Nome do usuário que fez a avaliação
    private Integer nota;
    private String comentario;
    private LocalDateTime createdAt;

    // Construtores
    public AvaliacaoResponseDTO() {
    }

    public AvaliacaoResponseDTO(Long id, Long pontoId, Long usuarioId, String nomeUsuario, 
                               Integer nota, String comentario, LocalDateTime createdAt) {
        this.id = id;
        this.pontoId = pontoId;
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.nota = nota;
        this.comentario = comentario;
        this.createdAt = createdAt;
    }

    // Construtor simplificado (sem nome do usuário)
    public AvaliacaoResponseDTO(Long id, Long pontoId, Long usuarioId, Integer nota, 
                               String comentario, LocalDateTime createdAt) {
        this.id = id;
        this.pontoId = pontoId;
        this.usuarioId = usuarioId;
        this.nota = nota;
        this.comentario = comentario;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() { return id; }
    public Long getPontoId() { return pontoId; }
    public Long getUsuarioId() { return usuarioId; }
    public String getNomeUsuario() { return nomeUsuario; }
    public Integer getNota() { return nota; }
    public String getComentario() { return comentario; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setPontoId(Long pontoId) { this.pontoId = pontoId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public void setNota(Integer nota) { this.nota = nota; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}