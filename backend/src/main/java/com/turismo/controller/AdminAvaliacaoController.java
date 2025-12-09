package com.turismo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turismo.service.AvaliacaoService;

@RestController
@RequestMapping("/api/admin/avaliacoes")
@CrossOrigin(origins = "*")
public class AdminAvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AdminAvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping("/recalcular-medias")
    public ResponseEntity<String> recalcularTodasAsMedias() {
        try {
            avaliacaoService.recalcularTodasAsMedias();
            return ResponseEntity.ok("Médias recalculadas com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao recalcular médias: " + e.getMessage());
        }
    }
}