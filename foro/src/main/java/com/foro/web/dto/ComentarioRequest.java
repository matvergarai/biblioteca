package com.foro.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ComentarioRequest(
		@NotBlank(message = "El contenido es obligatorio")
		@Size(max = 2000, message = "El contenido no puede superar 2000 caracteres")
		String contenido,

		@NotNull(message = "El autor es obligatorio")
		Long autorId) {
}
