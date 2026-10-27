package com.foro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foro.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	boolean existsByNombre(String nombre);

}
