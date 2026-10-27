package com.foro.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TemaRequest(
		@NotBlank(message = "El titulo es obligatorio")
		@Size(max = 200, message = "El titulo no puede superar 200 caracteres")
		String titulo,

		@NotBlank(message = "El contenido es obligatorio")
		@Size(max = 4000, message = "El contenido no puede superar 4000 caracteres")
		String contenido,

		@NotNull(message = "La categoria es obligatoria")
		Long categoriaId,

		@NotNull(message = "El autor es obligatorio")
		Long autorId) {
}
