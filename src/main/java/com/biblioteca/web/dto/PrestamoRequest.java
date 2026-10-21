package com.biblioteca.web.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PrestamoRequest(
		@NotNull(message = "El libro es obligatorio")
		Long libroId,

		@NotBlank(message = "El prestatario es obligatorio")
		@Size(max = 150, message = "El prestatario no puede superar 150 caracteres")
		String prestatario,

		@NotNull(message = "La fecha de prestamo es obligatoria")
		LocalDate fechaPrestamo,

		LocalDate fechaDevolucion) {
}
