package com.turismo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.turismo.model.PontoTuristico;

@Repository  // ADICIONAR ESTA LINHA
public interface PontoTuristicoRepository extends JpaRepository<PontoTuristico, Long> {

    // Busca por cidade
    List<PontoTuristico> findByCidadeIgnoreCase(String cidade);
    
    // Busca por estado
    List<PontoTuristico> findByEstadoIgnoreCase(String estado);
    
    // Busca por cidade e estado
    List<PontoTuristico> findByCidadeIgnoreCaseAndEstadoIgnoreCase(String cidade, String estado);
    
    // Busca por nome (busca parcial)
    List<PontoTuristico> findByNomeContainingIgnoreCase(String nome);
    
    // Verificar se já existe ponto com mesmo nome na cidade
    boolean existsByNomeIgnoreCaseAndCidadeIgnoreCase(String nome, String cidade);
    
    // Busca com paginação
    Page<PontoTuristico> findAll(Pageable pageable);
    
    // Busca com filtros e paginação
    @Query("SELECT p FROM PontoTuristico p WHERE " +
           "(:cidade IS NULL OR LOWER(p.cidade) LIKE LOWER(CONCAT('%', :cidade, '%'))) AND " +
           "(:estado IS NULL OR LOWER(p.estado) LIKE LOWER(CONCAT('%', :estado, '%'))) AND " +
           "(:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')))")
    Page<PontoTuristico> findWithFilters(@Param("cidade") String cidade, 
                                        @Param("estado") String estado, 
                                        @Param("nome") String nome, 
                                        Pageable pageable);
}