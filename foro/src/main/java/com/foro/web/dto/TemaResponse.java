package com.foro.web.dto;

import java.time.LocalDateTime;

public record TemaResponse(
		Long id,
		String titulo,
		String contenido,
		LocalDateTime fechaCreacion,
		String estado,
		Long categoriaId,
		String categoriaNombre,
		Long autorId,
		String autorNombre) {
}
