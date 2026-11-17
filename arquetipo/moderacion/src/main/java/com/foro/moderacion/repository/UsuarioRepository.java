package com.foro.moderacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foro.moderacion.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
