package com.foro.web.dto;

import com.foro.model.Rol;

public record UsuarioResponse(
		Long id,
		String nombre,
		String correo,
		Rol rol) {
}
