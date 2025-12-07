package com.turismo.dto;

public class RespostaDTO {
    private Long usuarioId;
    private String nomeUsuario;
    private String texto;
    private String data;

    // Construtor padrão
    public RespostaDTO() {
    }

    // Construtor com parâmetros
    public RespostaDTO(Long usuarioId, String nomeUsuario, String texto, String data) {
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.texto = texto;
        this.data = data;
    }

    // Getters
    public Long getUsuarioId() { return usuarioId; }
    public String getNomeUsuario() { return nomeUsuario; }
    public String getTexto() { return texto; }
    public String getData() { return data; }

    // Setters
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public void setTexto(String texto) { this.texto = texto; }
    public void setData(String data) { this.data = data; }
}