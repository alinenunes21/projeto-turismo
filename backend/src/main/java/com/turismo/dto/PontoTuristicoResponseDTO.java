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
    private Double notaMedia;
    private Long quantidadeAvaliacoes;

    // Construtor completo
    public PontoTuristicoResponseDTO(Long id, String nome, String descricao, String cidade,
                                     String estado, String pais, Double latitude, Double longitude,
                                     String endereco, Long criadoPor, LocalDateTime createdAt,
                                     Double notaMedia, Long quantidadeAvaliacoes) {
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
        this.notaMedia = notaMedia;
        this.quantidadeAvaliacoes = quantidadeAvaliacoes;
    }

    // Construtor sem avaliações (para compatibilidade)
    public PontoTuristicoResponseDTO(Long id, String nome, String descricao, String cidade,
                                     String estado, String pais, Double latitude, Double longitude,
                                     String endereco, Long criadoPor, LocalDateTime createdAt) {
        this(id, nome, descricao, cidade, estado, pais, latitude, longitude,
                endereco, criadoPor, createdAt, 0.0, 0L);
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public Long getCriadoPor() { return criadoPor; }
    public void setCriadoPor(Long criadoPor) { this.criadoPor = criadoPor; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Double getNotaMedia() { return notaMedia; }
    public void setNotaMedia(Double notaMedia) { this.notaMedia = notaMedia; }

    public Long getQuantidadeAvaliacoes() { return quantidadeAvaliacoes; }
    public void setQuantidadeAvaliacoes(Long quantidadeAvaliacoes) { this.quantidadeAvaliacoes = quantidadeAvaliacoes; }
}