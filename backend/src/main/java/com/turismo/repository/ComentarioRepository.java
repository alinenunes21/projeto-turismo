package com.turismo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.turismo.model.Comentario;

public interface ComentarioRepository extends MongoRepository<Comentario, String> {

    // Buscar comentários por ponto turístico (ordenados por data - mais recentes primeiro)
    List<Comentario> findByPontoIdOrderByCreatedAtDesc(Long pontoId);

    // Buscar comentários por usuário
    List<Comentario> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    // Buscar comentários em um período específico
    List<Comentario> findByPontoIdAndCreatedAtBetween(Long pontoId, LocalDateTime inicio, LocalDateTime fim);

    // Contar comentários de um ponto
    long countByPontoId(Long pontoId);

    // Deletar todos os comentários de um ponto (cascade quando deletar ponto)
    void deleteByPontoId(Long pontoId);

    // Deletar todos os comentários de um usuário
    void deleteByUsuarioId(Long usuarioId);

    // Verificar se usuário já comentou em um ponto específico
    boolean existsByPontoIdAndUsuarioId(Long pontoId, Long usuarioId);
    
    // Métodos simples primeiro - implementaremos busca depois se precisar
}