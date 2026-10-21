package com.biblioteca.web.dto;

import java.time.LocalDate;

public record PrestamoResponse(
		Long id,
		Long libroId,
		String tituloLibro,
		String prestatario,
		LocalDate fechaPrestamo,
		LocalDate fechaDevolucion,
		String estado) {
}
