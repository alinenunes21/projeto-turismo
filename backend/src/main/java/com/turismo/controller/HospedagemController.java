package com.turismo.controller;

import com.turismo.dto.HospedagemRequestDTO;
import com.turismo.dto.HospedagemResponseDTO;
import com.turismo.service.HospedagemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/hospedagens")
@CrossOrigin(origins = "*")
public class HospedagemController {

    private final HospedagemService hospedagemService;

    public HospedagemController(HospedagemService hospedagemService) {
        this.hospedagemService = hospedagemService;
    }

    /**
     * POST /api/hospedagens
     * Criar nova hospedagem
     */
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody HospedagemRequestDTO dto) {
        log.info("POST /api/hospedagens - Criando hospedagem para ponto: {}", dto.getPontoId());

        try {
            // TODO: Substituir por usuário autenticado
            Long usuarioId = 1L;

            HospedagemResponseDTO hospedagem = hospedagemService.criar(dto, usuarioId);
            log.info("Hospedagem criada com sucesso - ID: {}", hospedagem.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(hospedagem);

        } catch (RuntimeException e) {
            log.error("Erro ao criar hospedagem: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro inesperado ao criar hospedagem", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro interno ao criar hospedagem"));
        }
    }

    /**
     * GET /api/hospedagens/ponto/{pontoId}
     * Listar todas as hospedagens de um ponto turístico
     */
    @GetMapping("/ponto/{pontoId}")
    public ResponseEntity<?> listarPorPonto(@PathVariable Long pontoId) {
        log.info("GET /api/hospedagens/ponto/{} - Listando hospedagens", pontoId);

        try {
            List<HospedagemResponseDTO> hospedagens = hospedagemService.listarPorPonto(pontoId);
            log.info("Encontradas {} hospedagens para o ponto {}", hospedagens.size(), pontoId);

            return ResponseEntity.ok(hospedagens);

        } catch (Exception e) {
            log.error("Erro ao listar hospedagens do ponto {}: {}", pontoId, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao listar hospedagens"));
        }
    }

    /**
     * PUT /api/hospedagens/{id}
     * Atualizar uma hospedagem existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody HospedagemRequestDTO dto) {

        log.info("PUT /api/hospedagens/{} - Atualizando hospedagem", id);

        try {
            // TODO: Substituir por usuário autenticado
            Long usuarioId = 1L;

            HospedagemResponseDTO hospedagem = hospedagemService.atualizar(id, dto, usuarioId);
            log.info("Hospedagem {} atualizada com sucesso", id);

            return ResponseEntity.ok(hospedagem);

        } catch (RuntimeException e) {
            log.error("Erro ao atualizar hospedagem {}: {}", id, e.getMessage());

            if (e.getMessage().contains("não encontrada")) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(e.getMessage()));
            }

            if (e.getMessage().contains("permissão")) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(e.getMessage()));
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));

        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar hospedagem {}", id, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro interno ao atualizar hospedagem"));
        }
    }

    /**
     * DELETE /api/hospedagens/{id}
     * Deletar uma hospedagem
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        log.info("DELETE /api/hospedagens/{} - Deletando hospedagem", id);

        try {
            // TODO: Substituir por usuário autenticado
            Long usuarioId = 1L;

            hospedagemService.deletar(id, usuarioId);
            log.info("Hospedagem {} deletada com sucesso", id);

            return ResponseEntity.ok(new SuccessResponse("Hospedagem deletada com sucesso"));

        } catch (RuntimeException e) {
            log.error("Erro ao deletar hospedagem {}: {}", id, e.getMessage());

            if (e.getMessage().contains("não encontrada")) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(e.getMessage()));
            }

            if (e.getMessage().contains("permissão")) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(e.getMessage()));
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));

        } catch (Exception e) {
            log.error("Erro inesperado ao deletar hospedagem {}", id, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro interno ao deletar hospedagem"));
        }
    }

    /**
     * GET /api/hospedagens/{id}
     * Buscar uma hospedagem específica por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/hospedagens/{} - Buscando hospedagem", id);

        try {
            HospedagemResponseDTO hospedagem = hospedagemService.buscarPorId(id);
            return ResponseEntity.ok(hospedagem);

        } catch (RuntimeException e) {
            log.error("Hospedagem {} não encontrada", id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Hospedagem não encontrada"));
        } catch (Exception e) {
            log.error("Erro ao buscar hospedagem {}", id, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao buscar hospedagem"));
        }
    }

    /**
     * GET /api/hospedagens/usuario/{usuarioId}
     * Listar hospedagens criadas por um usuário específico
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/hospedagens/usuario/{} - Listando hospedagens do usuário", usuarioId);

        try {
            List<HospedagemResponseDTO> hospedagens = hospedagemService.listarPorUsuario(usuarioId);
            log.info("Encontradas {} hospedagens do usuário {}", hospedagens.size(), usuarioId);

            return ResponseEntity.ok(hospedagens);

        } catch (Exception e) {
            log.error("Erro ao listar hospedagens do usuário {}: {}", usuarioId, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao listar hospedagens do usuário"));
        }
    }

    /**
     * GET /api/hospedagens
     * Listar todas as hospedagens (com paginação opcional)
     */
    @GetMapping
    public ResponseEntity<?> listarTodas(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "50") int size) {

        log.info("GET /api/hospedagens - Listando todas as hospedagens (page: {}, size: {})", page, size);

        try {
            List<HospedagemResponseDTO> hospedagens = hospedagemService.listarTodas();
            log.info("Total de {} hospedagens encontradas", hospedagens.size());

            return ResponseEntity.ok(hospedagens);

        } catch (Exception e) {
            log.error("Erro ao listar todas as hospedagens: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao listar hospedagens"));
        }
    }

    // ==================== CLASSES DE RESPOSTA ====================

    /**
     * Classe para respostas de erro padronizadas
     */
    private static class ErrorResponse {
        private final String message;
        private final String status = "error";
        private final long timestamp;

        public ErrorResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMessage() {
            return message;
        }

        public String getStatus() {
            return status;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    /**
     * Classe para respostas de sucesso padronizadas
     */
    private static class SuccessResponse {
        private final String message;
        private final String status = "success";
        private final long timestamp;

        public SuccessResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMessage() {
            return message;
        }

        public String getStatus() {
            return status;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}