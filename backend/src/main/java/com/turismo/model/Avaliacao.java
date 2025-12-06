package com.turismo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "avaliacoes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ponto_id", "usuario_id"}) // Um usuário avalia um ponto apenas 1x
})
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ponto_id", nullable = false)
    private Long pontoId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private Integer nota; // 1 a 5

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Construtores
    public Avaliacao() {
    }

    public Avaliacao(Long pontoId, Long usuarioId, Integer nota, String comentario) {
        this.pontoId = pontoId;
        this.usuarioId = usuarioId;
        this.nota = nota;
        this.comentario = comentario;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public Long getPontoId() { return pontoId; }
    public Long getUsuarioId() { return usuarioId; }
    public Integer getNota() { return nota; }
    public String getComentario() { return comentario; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setPontoId(Long pontoId) { this.pontoId = pontoId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public void setNota(Integer nota) { this.nota = nota; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}