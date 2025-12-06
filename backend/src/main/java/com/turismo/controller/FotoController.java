package com.turismo.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.turismo.dto.FotoResponseDTO;
import com.turismo.service.FotoService;

@RestController
@RequestMapping("/api/fotos")
@CrossOrigin(origins = "http://localhost:4200")
public class FotoController {

    private final FotoService fotoService;

    // Construtor manual
    public FotoController(FotoService fotoService) {
        this.fotoService = fotoService;
    }

    // POST /api/fotos/upload - Upload de foto
    @PostMapping("/upload")
    public ResponseEntity<FotoResponseDTO> uploadFoto(
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("pontoId") Long pontoId,
            @RequestParam(value = "titulo", required = false) String titulo) {
        
        // Por enquanto, usuário fixo (depois implementamos autenticação real)
        Long usuarioId = 1L;
        
        FotoResponseDTO fotoDto = fotoService.upload(arquivo, pontoId, titulo, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(fotoDto);
    }

    // GET /api/fotos/ponto/{pontoId} - Listar fotos de um ponto
    @GetMapping("/ponto/{pontoId}")
    public ResponseEntity<List<FotoResponseDTO>> listarPorPonto(@PathVariable Long pontoId) {
        List<FotoResponseDTO> fotos = fotoService.listarPorPonto(pontoId);
        return ResponseEntity.ok(fotos);
    }

    // GET /api/fotos/usuario/{usuarioId} - Listar fotos de um usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<FotoResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<FotoResponseDTO> fotos = fotoService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(fotos);
    }

    // GET /api/fotos/arquivo/{filename} - Servir arquivo de foto
    @GetMapping("/arquivo/{filename}")
    public ResponseEntity<byte[]> obterArquivo(@PathVariable String filename) {
        try {
            byte[] arquivo = fotoService.obterArquivo(filename);
            String contentType = fotoService.obterContentType(filename);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(arquivo.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(arquivo);
                    
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/fotos/{fotoId} - Deletar foto
    @DeleteMapping("/{fotoId}")
    public ResponseEntity<Void> deletarFoto(@PathVariable String fotoId) {
        
        // Por enquanto, usuário fixo (depois implementamos autenticação real)
        Long usuarioId = 1L;
        
        fotoService.deletar(fotoId, usuarioId);
        return ResponseEntity.noContent().build();
    }
}