package com.turismo.repository;

import com.turismo.model.Hospedagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de banco de dados com Hospedagens
 */
@Repository
public interface HospedagemRepository extends JpaRepository<Hospedagem, Long> {


    List<Hospedagem> findByPontoIdOrderByCreatedAtDesc(Long pontoId);

    List<Hospedagem> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);


    long countByPontoId(Long pontoId);


    long countByUsuarioId(Long usuarioId);


    List<Hospedagem> findByTipoOrderByCreatedAtDesc(String tipo);


    List<Hospedagem> findByPontoIdAndTipoOrderByCreatedAtDesc(Long pontoId, String tipo);


    @Query("SELECT h FROM Hospedagem h WHERE h.precoMedio BETWEEN :precoMin AND :precoMax ORDER BY h.precoMedio ASC")
    List<Hospedagem> findByPrecoMedioBetween(
            @Param("precoMin") Double precoMin,
            @Param("precoMax") Double precoMax
    );

    @Query("SELECT h FROM Hospedagem h WHERE h.pontoId = :pontoId AND h.precoMedio BETWEEN :precoMin AND :precoMax ORDER BY h.precoMedio ASC")
    List<Hospedagem> findByPontoIdAndPrecoMedioBetween(
            @Param("pontoId") Long pontoId,
            @Param("precoMin") Double precoMin,
            @Param("precoMax") Double precoMax
    );


    @Query("SELECT h FROM Hospedagem h WHERE h.pontoId = :pontoId ORDER BY h.precoMedio ASC")
    List<Hospedagem> findTop5ByPontoIdOrderByPrecoMedioAsc(@Param("pontoId") Long pontoId);


    @Query("SELECT h FROM Hospedagem h WHERE LOWER(h.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY h.createdAt DESC")
    List<Hospedagem> findByNomeContainingIgnoreCase(@Param("nome") String nome);


    @Query("SELECT h FROM Hospedagem h WHERE h.pontoId = :pontoId AND LOWER(h.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY h.createdAt DESC")
    List<Hospedagem> findByPontoIdAndNomeContainingIgnoreCase(
            @Param("pontoId") Long pontoId,
            @Param("nome") String nome
    );


    boolean existsByPontoIdAndNome(Long pontoId, String nome);


    List<Hospedagem> findAllByOrderByPrecoMedioAsc();


    List<Hospedagem> findByPontoIdOrderByPrecoMedioAsc(Long pontoId);


    @Query("SELECT h FROM Hospedagem h WHERE h.linkReserva IS NOT NULL AND h.linkReserva != '' ORDER BY h.createdAt DESC")
    List<Hospedagem> findAllWithLinkReserva();


    @Query("SELECT h FROM Hospedagem h WHERE h.pontoId = :pontoId AND h.linkReserva IS NOT NULL AND h.linkReserva != '' ORDER BY h.createdAt DESC")
    List<Hospedagem> findByPontoIdWithLinkReserva(@Param("pontoId") Long pontoId);


    void deleteByPontoId(Long pontoId);

    void deleteByUsuarioId(Long usuarioId);
}