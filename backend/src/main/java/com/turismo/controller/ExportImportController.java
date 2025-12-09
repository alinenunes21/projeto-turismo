package com.turismo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.exceptions.CsvException;
import com.turismo.dto.PontoTuristicoResponseDTO;
import com.turismo.service.ExportImportService;

@RestController
@RequestMapping("/api/pontos")
@CrossOrigin(origins = "*")
public class ExportImportController {

    private final ExportImportService exportImportService;

    public ExportImportController(ExportImportService exportImportService) {
        this.exportImportService = exportImportService;
    }


    @GetMapping("/export/json")
    public ResponseEntity<InputStreamResource> exportarJSON() {
        try {
            ByteArrayInputStream stream = exportImportService.exportarJSON();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=pontos_turisticos.json");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new InputStreamResource(stream));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/csv")
    public ResponseEntity<InputStreamResource> exportarCSV() {
        try {
            ByteArrayInputStream stream = exportImportService.exportarCSV();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=pontos_turisticos.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(new InputStreamResource(stream));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/xml")
    public ResponseEntity<InputStreamResource> exportarXML() {
        try {
            ByteArrayInputStream stream = exportImportService.exportarXML();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=pontos_turisticos.xml");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_XML)
                    .body(new InputStreamResource(stream));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/import/json")
    public ResponseEntity<?> importarJSON(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Arquivo vazio");
            }

            List<PontoTuristicoResponseDTO> pontosImportados =
                    exportImportService.importarJSON(file, 1L); // usuarioId fixo por enquanto

            return ResponseEntity.ok()
                    .body(new ImportResponse(pontosImportados.size(), pontosImportados));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar arquivo JSON: " + e.getMessage());
        }
    }

    @PostMapping("/import/csv")
    public ResponseEntity<?> importarCSV(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Arquivo vazio");
            }

            List<PontoTuristicoResponseDTO> pontosImportados =
                    exportImportService.importarCSV(file, 1L); // usuarioId fixo por enquanto

            return ResponseEntity.ok()
                    .body(new ImportResponse(pontosImportados.size(), pontosImportados));

        } catch (IOException | CsvException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar arquivo CSV: " + e.getMessage());
        }
    }

    @PostMapping("/import/xml")
    public ResponseEntity<?> importarXML(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Arquivo vazio");
            }

            List<PontoTuristicoResponseDTO> pontosImportados =
                    exportImportService.importarXML(file, 1L); // usuarioId fixo por enquanto

            return ResponseEntity.ok()
                    .body(new ImportResponse(pontosImportados.size(), pontosImportados));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar arquivo XML: " + e.getMessage());
        }
    }

    // Classe interna para resposta de importação
    public static class ImportResponse {
        private int totalImportados;
        private List<PontoTuristicoResponseDTO> pontos;

        public ImportResponse(int totalImportados, List<PontoTuristicoResponseDTO> pontos) {
            this.totalImportados = totalImportados;
            this.pontos = pontos;
        }

        public int getTotalImportados() {
            return totalImportados;
        }

        public void setTotalImportados(int totalImportados) {
            this.totalImportados = totalImportados;
        }

        public List<PontoTuristicoResponseDTO> getPontos() {
            return pontos;
        }

        public void setPontos(List<PontoTuristicoResponseDTO> pontos) {
            this.pontos = pontos;
        }
    }
}