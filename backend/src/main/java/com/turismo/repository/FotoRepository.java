package com.turismo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.turismo.model.Foto;

public interface FotoRepository extends MongoRepository<Foto, String> {

    // Buscar fotos por ponto turístico
    List<Foto> findByPontoIdOrderByCreatedAtDesc(Long pontoId);

    // Buscar fotos por usuário
    List<Foto> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    // Buscar foto por filename
    Optional<Foto> findByFilename(String filename);

    // Contar fotos de um ponto (para limite de 10)
    long countByPontoId(Long pontoId);

    // Deletar todas as fotos de um ponto
    void deleteByPontoId(Long pontoId);

    // Deletar todas as fotos de um usuário
    void deleteByUsuarioId(Long usuarioId);

    // Verificar se arquivo já existe
    boolean existsByFilename(String filename);
}