package com.turismo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comentarios")
public class Comentario {

    @Id
    private String id;

    private Long pontoId;
    private Long usuarioId;
    private String texto;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Metadata metadata;
    private List<Resposta> respostas = new ArrayList<>();

    // Classe interna para metadados
    public static class Metadata {
        private String language = "pt";
        private String device;
        private String userAgent;

        public Metadata() {
        }

        public Metadata(String language, String device, String userAgent) {
            this.language = language;
            this.device = device;
            this.userAgent = userAgent;
        }

        // Getters e Setters
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public String getDevice() { return device; }
        public void setDevice(String device) { this.device = device; }
        public String getUserAgent() { return userAgent; }
        public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    }

    // Classe interna para respostas
    public static class Resposta {
        private Long usuarioId;
        private String texto;
        private LocalDateTime data = LocalDateTime.now();

        public Resposta() {
        }

        public Resposta(Long usuarioId, String texto) {
            this.usuarioId = usuarioId;
            this.texto = texto;
            this.data = LocalDateTime.now();
        }

        // Getters e Setters
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        public String getTexto() { return texto; }
        public void setTexto(String texto) { this.texto = texto; }
        public LocalDateTime getData() { return data; }
        public void setData(LocalDateTime data) { this.data = data; }
    }

    // Construtores
    public Comentario() {
    }

    public Comentario(Long pontoId, Long usuarioId, String texto) {
        this.pontoId = pontoId;
        this.usuarioId = usuarioId;
        this.texto = texto;
        this.createdAt = LocalDateTime.now();
        this.metadata = new Metadata();
        this.respostas = new ArrayList<>();
    }

    // Getters
    public String getId() { return id; }
    public Long getPontoId() { return pontoId; }
    public Long getUsuarioId() { return usuarioId; }
    public String getTexto() { return texto; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Metadata getMetadata() { return metadata; }
    public List<Resposta> getRespostas() { return respostas; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setPontoId(Long pontoId) { this.pontoId = pontoId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public void setTexto(String texto) { this.texto = texto; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setMetadata(Metadata metadata) { this.metadata = metadata; }
    public void setRespostas(List<Resposta> respostas) { this.respostas = respostas; }

    // Método utilitário para adicionar resposta
    public void adicionarResposta(Long usuarioId, String texto) {
        if (this.respostas == null) {
            this.respostas = new ArrayList<>();
        }
        this.respostas.add(new Resposta(usuarioId, texto));
    }
}