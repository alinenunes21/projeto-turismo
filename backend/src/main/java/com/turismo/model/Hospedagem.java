package com.turismo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "hospedagem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hospedagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pontoId;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(nullable = false)
    private Double precoMedio;

    @Column(nullable = false, length = 50)
    private String tipo; // Hotel, Pousada, Hostel, Resort, Airbnb, Camping

    @Column(length = 500)
    private String linkReserva;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}