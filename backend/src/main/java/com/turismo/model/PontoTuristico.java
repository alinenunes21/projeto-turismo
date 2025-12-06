package com.turismo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pontos_turisticos")
public class PontoTuristico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String estado;

    private String pais = "Brasil";

    private Double latitude;
    
    private Double longitude;

    private String endereco;

    @Column(name = "criado_por")
    private Long criadoPor;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Construtores
    public PontoTuristico() {
    }

    public PontoTuristico(String nome, String descricao, String cidade, String estado) {
        this.nome = nome;
        this.descricao = descricao;
        this.cidade = cidade;
        this.estado = estado;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public String getPais() { return pais; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getEndereco() { return endereco; }
    public Long getCriadoPor() { return criadoPor; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setPais(String pais) { this.pais = pais; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setCriadoPor(Long criadoPor) { this.criadoPor = criadoPor; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}