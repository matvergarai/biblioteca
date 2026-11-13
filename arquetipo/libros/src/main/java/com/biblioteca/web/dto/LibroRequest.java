package com.biblioteca.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LibroRequest(
		@NotBlank(message = "El titulo es obligatorio")
		@Size(max = 200, message = "El titulo no puede superar 200 caracteres")
		String titulo,

		@NotBlank(message = "El autor es obligatorio")
		@Size(max = 150, message = "El autor no puede superar 150 caracteres")
		String autor,

		@NotNull(message = "El anio de publicacion es obligatorio")
		@Min(value = 1, message = "El anio de publicacion no es valido")
		@Max(value = 2100, message = "El anio de publicacion no es valido")
		Integer anioPublicacion,

		@NotBlank(message = "El genero es obligatorio")
		@Size(max = 80, message = "El genero no puede superar 80 caracteres")
		String genero) {
}
