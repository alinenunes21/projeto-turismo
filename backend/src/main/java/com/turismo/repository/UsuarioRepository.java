package com.turismo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turismo.model.Usuario;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u")
    boolean existsAny();
}
