package com.foro.moderacion.web.dto;

import java.time.LocalDateTime;

public record ComentarioModeracionResponse(
		Long id,
		String contenido,
		LocalDateTime fechaCreacion,
		String estado,
		Long temaId,
		Long autorId,
		String autorNombre) {
}
