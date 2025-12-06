package com.turismo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.turismo.dto.LoginRequestDTO;
import com.turismo.dto.LoginResponseDTO;
import com.turismo.dto.RegistroRequestDTO;
import com.turismo.dto.UsuarioResponseDTO;
import com.turismo.model.Usuario;
import com.turismo.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // ✅ Construtor manual (sem depender do Lombok)
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO dto) {
        UsuarioResponseDTO usuarioCriado = usuarioService.registrar(dto);
        return ResponseEntity.ok(usuarioCriado);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {

        Usuario usuario = usuarioService.buscarPorEmail(dto.getEmail());

        // valida senha usando BCrypt
        boolean senhaCorreta = usuarioService.validarSenha(dto.getSenha(), usuario.getSenhaHash());
        if (!senhaCorreta) {
            throw new RuntimeException("Senha incorreta!");
        }

        LoginResponseDTO response = new LoginResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getRole().name(),
            "Login realizado com sucesso!"
        );

        return ResponseEntity.ok(response);
    }
}