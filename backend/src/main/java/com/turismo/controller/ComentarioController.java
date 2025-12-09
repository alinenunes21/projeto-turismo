package com.turismo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.turismo.dto.ComentarioRequestDTO;
import com.turismo.dto.ComentarioResponseDTO;
import com.turismo.service.ComentarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comentarios")
@CrossOrigin(origins = "*")
public class ComentarioController {

    private final ComentarioService comentarioService;

    // Construtor manual
    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    // POST /api/comentarios - Criar comentário
    @PostMapping
    public ResponseEntity<ComentarioResponseDTO> criarComentario(
            @Valid @RequestBody ComentarioRequestDTO dto) {
        
        // Por enquanto, usuário fixo (depois implementamos autenticação real)
        Long usuarioId = 1L;
        
        ComentarioResponseDTO comentarioDto = comentarioService.criar(dto, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(comentarioDto);
    }

    // GET /api/comentarios/ponto/{pontoId} - Listar comentários de um ponto
    @GetMapping("/ponto/{pontoId}")
    public ResponseEntity<List<ComentarioResponseDTO>> listarPorPonto(@PathVariable Long pontoId) {
        List<ComentarioResponseDTO> comentarios = comentarioService.listarPorPonto(pontoId);
        return ResponseEntity.ok(comentarios);
    }

    // GET /api/comentarios/usuario/{usuarioId} - Listar comentários de um usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ComentarioResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<ComentarioResponseDTO> comentarios = comentarioService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(comentarios);
    }

    // GET /api/comentarios/{id} - Buscar comentário por ID
    @GetMapping("/{id}")
    public ResponseEntity<ComentarioResponseDTO> buscarPorId(@PathVariable String id) {
        ComentarioResponseDTO comentario = comentarioService.buscarPorId(id);
        return ResponseEntity.ok(comentario);
    }

    // PUT /api/comentarios/{id} - Atualizar comentário
    @PutMapping("/{id}")
    public ResponseEntity<ComentarioResponseDTO> atualizarComentario(
            @PathVariable String id,
            @Valid @RequestBody ComentarioRequestDTO dto) {
        
        // Por enquanto, usuário fixo (depois implementamos autenticação real)
        Long usuarioId = 1L;
        
        ComentarioResponseDTO comentarioDto = comentarioService.atualizar(id, dto, usuarioId);
        return ResponseEntity.ok(comentarioDto);
    }

    // POST /api/comentarios/{id}/resposta - Adicionar resposta a um comentário
    @PostMapping("/{id}/resposta")
    public ResponseEntity<ComentarioResponseDTO> adicionarResposta(
            @PathVariable String id,
            @RequestBody Map<String, String> request) {
        
        String textoResposta = request.get("texto");
        if (textoResposta == null || textoResposta.trim().isEmpty()) {
            throw new RuntimeException("Texto da resposta é obrigatório");
        }
        
        // Por enquanto, usuário fixo (depois implementamos autenticação real)
        Long usuarioId = 1L;
        
        ComentarioResponseDTO comentarioDto = comentarioService.adicionarResposta(id, textoResposta, usuarioId);
        return ResponseEntity.ok(comentarioDto);
    }

    // DELETE /api/comentarios/{id} - Deletar comentário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarComentario(@PathVariable String id) {
        
        // Por enquanto, usuário fixo (depois implementamos autenticação real)
        Long usuarioId = 1L;
        boolean isAdmin = false; // Depois implementamos verificação de role
        
        comentarioService.deletar(id, usuarioId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    // GET /api/comentarios/pesquisar?texto={texto} - Pesquisar comentários por texto
    @GetMapping("/pesquisar")
    public ResponseEntity<List<ComentarioResponseDTO>> pesquisarTexto(
            @RequestParam("texto") String texto) {
        
        if (texto == null || texto.trim().isEmpty()) {
            throw new RuntimeException("Parâmetro 'texto' é obrigatório");
        }
        
        List<ComentarioResponseDTO> comentarios = comentarioService.pesquisarTexto(texto);
        return ResponseEntity.ok(comentarios);
    }

    // GET /api/comentarios/estatisticas/{pontoId} - Estatísticas de comentários de um ponto
    @GetMapping("/estatisticas/{pontoId}")
    public ResponseEntity<Map<String, Object>> obterEstatisticas(@PathVariable Long pontoId) {
        long totalComentarios = comentarioService.contarComentariosPonto(pontoId);
        
        Map<String, Object> estatisticas = Map.of(
            "totalComentarios", totalComentarios,
            "pontoId", pontoId
        );
        
        return ResponseEntity.ok(estatisticas);
    }
}