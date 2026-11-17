package com.foro.moderacion.web.dto;

import java.time.LocalDateTime;

public record TemaModeracionResponse(
		Long id,
		String titulo,
		String contenido,
		LocalDateTime fechaCreacion,
		String estado,
		Long autorId,
		String autorNombre) {
}
