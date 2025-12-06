package com.turismo.dto;

public class FotoResponseDTO {
    private String id;
    private Long pontoId;
    private Long usuarioId;
    private String nomeUsuario;
    private String filename;
    private String titulo;
    private String url; // URL para acessar a foto
    private String contentType;
    private Long tamanho;
    private String createdAt;

    // Construtores
    public FotoResponseDTO() {
    }

    public FotoResponseDTO(String id, Long pontoId, Long usuarioId, String nomeUsuario, 
                          String filename, String titulo, String url, String contentType, 
                          Long tamanho, String createdAt) {
        this.id = id;
        this.pontoId = pontoId;
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.filename = filename;
        this.titulo = titulo;
        this.url = url;
        this.contentType = contentType;
        this.tamanho = tamanho;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() { return id; }
    public Long getPontoId() { return pontoId; }
    public Long getUsuarioId() { return usuarioId; }
    public String getNomeUsuario() { return nomeUsuario; }
    public String getFilename() { return filename; }
    public String getTitulo() { return titulo; }
    public String getUrl() { return url; }
    public String getContentType() { return contentType; }
    public Long getTamanho() { return tamanho; }
    public String getCreatedAt() { return createdAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setPontoId(Long pontoId) { this.pontoId = pontoId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public void setFilename(String filename) { this.filename = filename; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setUrl(String url) { this.url = url; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setTamanho(Long tamanho) { this.tamanho = tamanho; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}