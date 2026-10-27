package com.foro.web.dto;

import java.time.LocalDateTime;

public record ComentarioResponse(
		Long id,
		String contenido,
		LocalDateTime fechaCreacion,
		String estado,
		Long temaId,
		Long autorId,
		String autorNombre) {
}
