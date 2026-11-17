package com.foro.publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foro.publicaciones.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
