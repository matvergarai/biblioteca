package com.foro.usuarios.web.dto;

import com.foro.usuarios.model.Rol;

public record UsuarioResponse(
		Long id,
		String nombre,
		String correo,
		Rol rol) {
}
