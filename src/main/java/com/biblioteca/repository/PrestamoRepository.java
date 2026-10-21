package com.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.model.Prestamo;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

	boolean existsByLibro_IdAndFechaDevolucionIsNull(Long libroId);

	boolean existsByLibro_IdAndFechaDevolucionIsNullAndIdNot(Long libroId, Long id);

}
