package com.turismo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.turismo.dto.AvaliacaoRequestDTO;
import com.turismo.dto.AvaliacaoResponseDTO;
import com.turismo.service.AvaliacaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/avaliacoes")
@CrossOrigin(origins = "http://localhost:4200")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    // Construtor manual
    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    // POST /api/avaliacoes - Criar/atualizar avaliação
    @PostMapping
    public ResponseEntity<AvaliacaoResponseDTO> criarAvaliacao(
            @Valid @RequestBody AvaliacaoRequestDTO dto) {
        
        // Por enquanto, usuário fixo (depois implementamos autenticação real)
        Long usuarioId = 1L;
        
        AvaliacaoResponseDTO avaliacaoDto = avaliacaoService.criarOuAtualizar(dto, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoDto);
    }

    // GET /api/avaliacoes/ponto/{pontoId} - Listar avaliações de um ponto
    @GetMapping("/ponto/{pontoId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarPorPonto(@PathVariable Long pontoId) {
        List<AvaliacaoResponseDTO> avaliacoes = avaliacaoService.listarPorPonto(pontoId);
        return ResponseEntity.ok(avaliacoes);
    }

    // GET /api/avaliacoes/usuario/{usuarioId} - Listar avaliações de um usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<AvaliacaoResponseDTO> avaliacoes = avaliacaoService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(avaliacoes);
    }

    // GET /api/avaliacoes/{id} - Buscar avaliação por ID
    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        AvaliacaoResponseDTO avaliacao = avaliacaoService.buscarPorId(id);
        return ResponseEntity.ok(avaliacao);
    }

    // GET /api/avaliacoes/usuario-ponto/{pontoId} - Buscar avaliação do usuário para um ponto específico
    @GetMapping("/usuario-ponto/{pontoId}")
    public ResponseEntity<AvaliacaoResponseDTO> buscarAvaliacaoUsuario(@PathVariable Long pontoId) {
        
        // Por enquanto, usuário fixo (depois implementamos autenticação real)
        Long usuarioId = 1L;
        
        try {
            AvaliacaoResponseDTO avaliacao = avaliacaoService.buscarAvaliacaoUsuario(pontoId, usuarioId);
            return ResponseEntity.ok(avaliacao);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/avaliacoes/{id} - Deletar avaliação
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAvaliacao(@PathVariable Long id) {
        
        // Por enquanto, usuário fixo (depois implementamos autenticação real)
        Long usuarioId = 1L;
        
        avaliacaoService.deletar(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    // GET /api/avaliacoes/estatisticas/{pontoId} - Estatísticas de um ponto
    @GetMapping("/estatisticas/{pontoId}")
    public ResponseEntity<Map<String, Object>> obterEstatisticas(@PathVariable Long pontoId) {
        Double media = avaliacaoService.calcularMediaPonto(pontoId);
        long totalAvaliacoes = avaliacaoService.contarAvaliacoesPonto(pontoId);
        
        Map<String, Object> estatisticas = Map.of(
            "mediaNotas", media,
            "totalAvaliacoes", totalAvaliacoes,
            "pontoId", pontoId
        );
        
        return ResponseEntity.ok(estatisticas);
    }
}