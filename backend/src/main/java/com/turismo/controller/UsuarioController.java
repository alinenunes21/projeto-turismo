package com.turismo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.turismo.dto.LoginRequestDTO;
import com.turismo.dto.LoginResponseDTO;
import com.turismo.dto.RegistroRequestDTO;
import com.turismo.dto.UsuarioResponseDTO;
import com.turismo.model.Usuario;
import com.turismo.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody RegistroRequestDTO dto) {
        UsuarioResponseDTO usuarioCriado = usuarioService.registrar(dto);
        return ResponseEntity.ok(usuarioCriado);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {

        Usuario usuario = usuarioService.buscarPorEmail(dto.getEmail());

        // valida senha usando BCrypt
        boolean senhaCorreta = usuarioService.validarSenha(dto.getSenha(), usuario.getSenhaHash());
        if (!senhaCorreta) {
            throw new RuntimeException("Senha incorreta!");
        }

        LoginResponseDTO response = new LoginResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail()
        );

        return ResponseEntity.ok(response);
    }
}
