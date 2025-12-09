package com.turismo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.turismo.dto.PontoTuristicoRequestDTO;
import com.turismo.dto.PontoTuristicoResponseDTO;
import com.turismo.service.PontoTuristicoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pontos")
@CrossOrigin(origins = "*")
public class PontoTuristicoController {

    private final PontoTuristicoService pontoTuristicoService;

    public PontoTuristicoController(PontoTuristicoService pontoTuristicoService) {
        this.pontoTuristicoService = pontoTuristicoService;
    }

    @GetMapping
    public ResponseEntity<Page<PontoTuristicoResponseDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String nome) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<PontoTuristicoResponseDTO> pontos = pontoTuristicoService.listarTodos(pageRequest, cidade, estado, nome);
        
        return ResponseEntity.ok(pontos);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<PontoTuristicoResponseDTO>> listarTodosSemPaginacao() {
        List<PontoTuristicoResponseDTO> pontos = pontoTuristicoService.listarTodosSemPaginacao();
        return ResponseEntity.ok(pontos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoTuristicoResponseDTO> buscarPorId(@PathVariable Long id) {
        PontoTuristicoResponseDTO ponto = pontoTuristicoService.buscarPorId(id);
        return ResponseEntity.ok(ponto);
    }

    @PostMapping
    public ResponseEntity<PontoTuristicoResponseDTO> criar(@Valid @RequestBody PontoTuristicoRequestDTO dto) {
        PontoTuristicoResponseDTO pontoSalvo = pontoTuristicoService.criar(dto, 1L); // usuarioId fixo por enquanto
        return ResponseEntity.status(HttpStatus.CREATED).body(pontoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontoTuristicoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PontoTuristicoRequestDTO dto) {
        PontoTuristicoResponseDTO pontoAtualizado = pontoTuristicoService.atualizar(id, dto, 1L); // usuarioId fixo por enquanto
        return ResponseEntity.ok(pontoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pontoTuristicoService.deletar(id, 1L); // usuarioId fixo por enquanto
        return ResponseEntity.noContent().build();
    }
}