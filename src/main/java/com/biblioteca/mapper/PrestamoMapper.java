package com.biblioteca.mapper;

import org.springframework.stereotype.Component;

import com.biblioteca.model.EstadoPrestamo;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.web.dto.PrestamoRequest;
import com.biblioteca.web.dto.PrestamoResponse;

@Component
public class PrestamoMapper {

	public Prestamo toEntity(PrestamoRequest request, Libro libro) {
		Prestamo prestamo = new Prestamo();
		actualizar(prestamo, request, libro);
		return prestamo;
	}

	public void actualizar(Prestamo prestamo, PrestamoRequest request, Libro libro) {
		prestamo.setLibro(libro);
		prestamo.setPrestatario(request.prestatario().trim());
		prestamo.setFechaPrestamo(request.fechaPrestamo());
		prestamo.setFechaDevolucion(request.fechaDevolucion());
		prestamo.setEstado(request.fechaDevolucion() == null ? EstadoPrestamo.ACTIVO : EstadoPrestamo.DEVUELTO);
	}

	public PrestamoResponse toResponse(Prestamo prestamo) {
		return new PrestamoResponse(
				prestamo.getId(),
				prestamo.getLibro().getId(),
				prestamo.getLibro().getTitulo(),
				prestamo.getPrestatario(),
				prestamo.getFechaPrestamo(),
				prestamo.getFechaDevolucion(),
				prestamo.getEstado().name());
	}

}
