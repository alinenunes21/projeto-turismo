package com.turismo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PontoTuristicoRequestDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 200, message = "Nome deve ter entre 2 e 200 caracteres")
    private String nome;
    
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    private String descricao;
    
    @NotBlank(message = "Cidade é obrigatória")
    @Size(min = 2, max = 100, message = "Cidade deve ter entre 2 e 100 caracteres")
    private String cidade;
    
    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 100, message = "Estado deve ter entre 2 e 100 caracteres")
    private String estado;
    
    private String pais = "Brasil";
    
    private Double latitude;
    
    private Double longitude;
    
    @Size(max = 300, message = "Endereço deve ter no máximo 300 caracteres")
    private String endereco;

    // Construtores
    public PontoTuristicoRequestDTO() {
    }

    // Getters
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public String getPais() { return pais; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getEndereco() { return endereco; }

    // Setters
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setPais(String pais) { this.pais = pais; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}