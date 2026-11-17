package com.foro.moderacion.web.dto;

import jakarta.validation.constraints.NotNull;

public record BanRequest(
		@NotNull(message = "El administrador es obligatorio")
		Long administradorId) {
}
