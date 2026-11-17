package com.foro.publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foro.publicaciones.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
