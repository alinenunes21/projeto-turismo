package com.turismo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.turismo.dto.PontoTuristicoRequestDTO;
import com.turismo.dto.PontoTuristicoResponseDTO;
import com.turismo.model.PontoTuristico;
import com.turismo.repository.PontoTuristicoRepository;

@Service
public class PontoTuristicoService {

    private final PontoTuristicoRepository pontoRepository;

    // Construtor manual
    public PontoTuristicoService(PontoTuristicoRepository pontoRepository) {
        this.pontoRepository = pontoRepository;
    }

    public PontoTuristicoResponseDTO criar(PontoTuristicoRequestDTO dto, Long usuarioId) {
        
        // Verificar se já existe ponto com mesmo nome na cidade
        if (pontoRepository.existsByNomeIgnoreCaseAndCidadeIgnoreCase(dto.getNome(), dto.getCidade())) {
            throw new RuntimeException("Já existe um ponto turístico com este nome nesta cidade");
        }

        PontoTuristico ponto = new PontoTuristico();
        ponto.setNome(dto.getNome());
        ponto.setDescricao(dto.getDescricao());
        ponto.setCidade(dto.getCidade());
        ponto.setEstado(dto.getEstado());
        ponto.setPais(dto.getPais());
        ponto.setLatitude(dto.getLatitude());
        ponto.setLongitude(dto.getLongitude());
        ponto.setEndereco(dto.getEndereco());
        ponto.setCriadoPor(usuarioId);
        ponto.setCreatedAt(LocalDateTime.now());

        PontoTuristico pontoSalvo = pontoRepository.save(ponto);
        
        return converterParaDTO(pontoSalvo);
    }

    public PontoTuristicoResponseDTO buscarPorId(Long id) {
        PontoTuristico ponto = pontoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ponto turístico não encontrado"));
        
        return converterParaDTO(ponto);
    }

    public List<PontoTuristicoResponseDTO> listarTodos() {
        return pontoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public Page<PontoTuristicoResponseDTO> listarComPaginacao(Pageable pageable) {
        return pontoRepository.findAll(pageable)
                .map(this::converterParaDTO);
    }

    public Page<PontoTuristicoResponseDTO> listarComFiltros(String cidade, String estado, String nome, Pageable pageable) {
        return pontoRepository.findWithFilters(cidade, estado, nome, pageable)
                .map(this::converterParaDTO);
    }

    public PontoTuristicoResponseDTO atualizar(Long id, PontoTuristicoRequestDTO dto, Long usuarioId) {
        PontoTuristico ponto = pontoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ponto turístico não encontrado"));

        // Verificar se mudou nome/cidade e se já existe outro com esses dados
        if (!ponto.getNome().equalsIgnoreCase(dto.getNome()) || 
            !ponto.getCidade().equalsIgnoreCase(dto.getCidade())) {
            if (pontoRepository.existsByNomeIgnoreCaseAndCidadeIgnoreCase(dto.getNome(), dto.getCidade())) {
                throw new RuntimeException("Já existe um ponto turístico com este nome nesta cidade");
            }
        }

        ponto.setNome(dto.getNome());
        ponto.setDescricao(dto.getDescricao());
        ponto.setCidade(dto.getCidade());
        ponto.setEstado(dto.getEstado());
        ponto.setPais(dto.getPais());
        ponto.setLatitude(dto.getLatitude());
        ponto.setLongitude(dto.getLongitude());
        ponto.setEndereco(dto.getEndereco());

        PontoTuristico pontoAtualizado = pontoRepository.save(ponto);
        
        return converterParaDTO(pontoAtualizado);
    }

    public void deletar(Long id) {
        if (!pontoRepository.existsById(id)) {
            throw new RuntimeException("Ponto turístico não encontrado");
        }
        pontoRepository.deleteById(id);
    }

    private PontoTuristicoResponseDTO converterParaDTO(PontoTuristico ponto) {
        return new PontoTuristicoResponseDTO(
                ponto.getId(),
                ponto.getNome(),
                ponto.getDescricao(),
                ponto.getCidade(),
                ponto.getEstado(),
                ponto.getPais(),
                ponto.getLatitude(),
                ponto.getLongitude(),
                ponto.getEndereco(),
                ponto.getCriadoPor(),
                ponto.getCreatedAt()
        );
    }
}