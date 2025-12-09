package com.turismo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turismo.dto.RegistroRequestDTO;
import com.turismo.dto.UsuarioResponseDTO;
import com.turismo.model.Role;
import com.turismo.model.Usuario;
import com.turismo.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private Boolean primeiroUsuario = null; // Cache para verificar o primeiro usuário

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO registrar(RegistroRequestDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado.");
        }

        // Verifica se existem usuários no banco de dados
        boolean isPrimeiroUsuario = !usuarioRepository.existsAny();

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenhaHash(passwordEncoder.encode(dto.getSenha()));

        // Define a role: ADMIN se for o primeiro usuário, USER caso contrário
        usuario.setRole(isPrimeiroUsuario ? Role.ADMIN : Role.USER);

        usuario.setCreatedAt(LocalDateTime.now());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getRole().name()
        );
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public boolean validarSenha(String senhaDigitada, String senhaHash) {
        return passwordEncoder.matches(senhaDigitada, senhaHash);
    }
}