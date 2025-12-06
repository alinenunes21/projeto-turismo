package com.turismo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turismo.dto.AvaliacaoRequestDTO;
import com.turismo.dto.AvaliacaoResponseDTO;
import com.turismo.model.Avaliacao;
import com.turismo.model.Usuario;
import com.turismo.repository.AvaliacaoRepository;
import com.turismo.repository.PontoTuristicoRepository;
import com.turismo.repository.UsuarioRepository;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final PontoTuristicoRepository pontoTuristicoRepository;
    private final UsuarioRepository usuarioRepository;

    // Construtor manual
    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository, 
                           PontoTuristicoRepository pontoTuristicoRepository,
                           UsuarioRepository usuarioRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.pontoTuristicoRepository = pontoTuristicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public AvaliacaoResponseDTO criarOuAtualizar(AvaliacaoRequestDTO dto, Long usuarioId) {
        // Verificar se o ponto turístico existe
        if (!pontoTuristicoRepository.existsById(dto.getPontoId())) {
            throw new RuntimeException("Ponto turístico não encontrado");
        }

        // Verificar se o usuário já avaliou este ponto
        Avaliacao avaliacaoExistente = avaliacaoRepository
                .findByPontoIdAndUsuarioId(dto.getPontoId(), usuarioId)
                .orElse(null);

        Avaliacao avaliacao;

        if (avaliacaoExistente != null) {
            // Atualizar avaliação existente
            avaliacaoExistente.setNota(dto.getNota());
            avaliacaoExistente.setComentario(dto.getComentario());
            avaliacao = avaliacaoRepository.save(avaliacaoExistente);
        } else {
            // Criar nova avaliação
            avaliacao = new Avaliacao();
            avaliacao.setPontoId(dto.getPontoId());
            avaliacao.setUsuarioId(usuarioId);
            avaliacao.setNota(dto.getNota());
            avaliacao.setComentario(dto.getComentario());
            avaliacao.setCreatedAt(LocalDateTime.now());
            avaliacao = avaliacaoRepository.save(avaliacao);
        }

        return converterParaDTO(avaliacao);
    }

    public List<AvaliacaoResponseDTO> listarPorPonto(Long pontoId) {
        return avaliacaoRepository.findByPontoIdOrderByCreatedAtDesc(pontoId)
                .stream()
                .map(this::converterParaDTOComNome)
                .collect(Collectors.toList());
    }

    public List<AvaliacaoResponseDTO> listarPorUsuario(Long usuarioId) {
        return avaliacaoRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public AvaliacaoResponseDTO buscarPorId(Long id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
        
        return converterParaDTOComNome(avaliacao);
    }

    public AvaliacaoResponseDTO buscarAvaliacaoUsuario(Long pontoId, Long usuarioId) {
        Avaliacao avaliacao = avaliacaoRepository.findByPontoIdAndUsuarioId(pontoId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário ainda não avaliou este ponto"));
        
        return converterParaDTO(avaliacao);
    }

    public void deletar(Long id, Long usuarioId) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        // Verificar se o usuário é dono da avaliação
        if (!avaliacao.getUsuarioId().equals(usuarioId)) {
            throw new RuntimeException("Você não pode deletar a avaliação de outro usuário");
        }

        avaliacaoRepository.deleteById(id);
    }

    public Double calcularMediaPonto(Long pontoId) {
        Double media = avaliacaoRepository.calcularMediaNotas(pontoId);
        return media != null ? Math.round(media * 10.0) / 10.0 : 0.0; // Arredondar para 1 casa decimal
    }

    public long contarAvaliacoesPonto(Long pontoId) {
        return avaliacaoRepository.countByPontoId(pontoId);
    }

    private AvaliacaoResponseDTO converterParaDTO(Avaliacao avaliacao) {
        return new AvaliacaoResponseDTO(
                avaliacao.getId(),
                avaliacao.getPontoId(),
                avaliacao.getUsuarioId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getCreatedAt()
        );
    }

    private AvaliacaoResponseDTO converterParaDTOComNome(Avaliacao avaliacao) {
        // Buscar nome do usuário
        String nomeUsuario = usuarioRepository.findById(avaliacao.getUsuarioId())
                .map(Usuario::getNome)
                .orElse("Usuário desconhecido");

        return new AvaliacaoResponseDTO(
                avaliacao.getId(),
                avaliacao.getPontoId(),
                avaliacao.getUsuarioId(),
                nomeUsuario,
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getCreatedAt()
        );
    }
}