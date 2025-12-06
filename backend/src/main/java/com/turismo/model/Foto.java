package com.turismo.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "fotos")
public class Foto {

    @Id
    private String id;

    private Long pontoId;
    private Long usuarioId;
    private String filename;
    private String titulo;
    private String path;
    private String contentType;
    private Long tamanho; // em bytes
    private LocalDateTime createdAt = LocalDateTime.now();

    // Construtores
    public Foto() {
    }

    public Foto(Long pontoId, Long usuarioId, String filename, String titulo, String path, 
                String contentType, Long tamanho) {
        this.pontoId = pontoId;
        this.usuarioId = usuarioId;
        this.filename = filename;
        this.titulo = titulo;
        this.path = path;
        this.contentType = contentType;
        this.tamanho = tamanho;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public String getId() { return id; }
    public Long getPontoId() { return pontoId; }
    public Long getUsuarioId() { return usuarioId; }
    public String getFilename() { return filename; }
    public String getTitulo() { return titulo; }
    public String getPath() { return path; }
    public String getContentType() { return contentType; }
    public Long getTamanho() { return tamanho; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setPontoId(Long pontoId) { this.pontoId = pontoId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public void setFilename(String filename) { this.filename = filename; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setPath(String path) { this.path = path; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setTamanho(Long tamanho) { this.tamanho = tamanho; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}