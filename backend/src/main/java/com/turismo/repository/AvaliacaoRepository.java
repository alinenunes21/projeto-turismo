package com.turismo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.turismo.model.Avaliacao;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    // Buscar avaliação específica de um usuário para um ponto
    Optional<Avaliacao> findByPontoIdAndUsuarioId(Long pontoId, Long usuarioId);

    // Listar todas as avaliações de um ponto
    List<Avaliacao> findByPontoIdOrderByCreatedAtDesc(Long pontoId);

    // Buscar avaliações de um usuário
    List<Avaliacao> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    // Verificar se usuário já avaliou o ponto
    boolean existsByPontoIdAndUsuarioId(Long pontoId, Long usuarioId);

    // Calcular média de notas de um ponto
    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.pontoId = :pontoId")
    Double calcularMediaNotas(@Param("pontoId") Long pontoId);

    // Contar total de avaliações de um ponto
    long countByPontoId(Long pontoId);

    // Buscar avaliações por nota específica
    List<Avaliacao> findByPontoIdAndNota(Long pontoId, Integer nota);

    // Deletar todas as avaliações de um ponto (cascade quando deletar ponto)
    void deleteByPontoId(Long pontoId);
}