package com.turismo.dto;

import java.time.LocalDateTime;

public class PontoTuristicoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String cidade;
    private String estado;
    private String pais;
    private Double latitude;
    private Double longitude;
    private String endereco;
    private Long criadoPor;
    private LocalDateTime createdAt;

    // Construtor padrão
    public PontoTuristicoResponseDTO() {
    }

    // Construtor completo
    public PontoTuristicoResponseDTO(Long id, String nome, String descricao, String cidade, String estado, 
                                    String pais, Double latitude, Double longitude, String endereco, 
                                    Long criadoPor, LocalDateTime createdAt) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
        this.latitude = latitude;
        this.longitude = longitude;
        this.endereco = endereco;
        this.criadoPor = criadoPor;
        this.createdAt = createdAt;
    }

    // Construtor simplificado para listagem
    public PontoTuristicoResponseDTO(Long id, String nome, String cidade, String estado) {
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
        this.estado = estado;
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