package com.turismo.dto;

import java.util.List;

public class ComentarioResponseDTO {
    private String id;
    private Long pontoId;
    private Long usuarioId;
    private String nomeUsuario;
    private String texto;
    private String createdAt;
    private List<RespostaDTO> respostas;
    private int totalRespostas;

    // Construtores
    public ComentarioResponseDTO() {
    }

    public ComentarioResponseDTO(String id, Long pontoId, Long usuarioId, String nomeUsuario, 
                                String texto, String createdAt, List<RespostaDTO> respostas) {
        this.id = id;
        this.pontoId = pontoId;
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.texto = texto;
        this.createdAt = createdAt;
        this.respostas = respostas;
        this.totalRespostas = respostas != null ? respostas.size() : 0;
    }

    // Getters
    public String getId() { return id; }
    public Long getPontoId() { return pontoId; }
    public Long getUsuarioId() { return usuarioId; }
    public String getNomeUsuario() { return nomeUsuario; }
    public String getTexto() { return texto; }
    public String getCreatedAt() { return createdAt; }
    public List<RespostaDTO> getRespostas() { return respostas; }
    public int getTotalRespostas() { return totalRespostas; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setPontoId(Long pontoId) { this.pontoId = pontoId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public void setTexto(String texto) { this.texto = texto; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setRespostas(List<RespostaDTO> respostas) { 
        this.respostas = respostas;
        this.totalRespostas = respostas != null ? respostas.size() : 0;
    }
    public void setTotalRespostas(int totalRespostas) { this.totalRespostas = totalRespostas; }
}