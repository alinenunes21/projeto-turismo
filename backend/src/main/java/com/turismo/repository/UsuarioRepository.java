package com.turismo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turismo.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
