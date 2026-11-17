package com.foro.usuarios.web.dto;

import com.foro.usuarios.model.Rol;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
		@NotBlank(message = "El nombre es obligatorio")
		@Size(max = 120, message = "El nombre no puede superar 120 caracteres")
		String nombre,

		@NotBlank(message = "El correo es obligatorio")
		@Email(message = "El correo no es valido")
		@Size(max = 150, message = "El correo no puede superar 150 caracteres")
		String correo,

		@NotBlank(message = "La clave es obligatoria")
		@Size(min = 8, max = 72, message = "La clave debe tener entre 8 y 72 caracteres")
		@Pattern(regexp = ".*[A-Za-z].*", message = "La clave debe incluir letras")
		@Pattern(regexp = ".*\\d.*", message = "La clave debe incluir numeros")
		@Pattern(regexp = ".*[^A-Za-z0-9].*", message = "La clave debe incluir un caracter especial")
		String clave,

		@NotNull(message = "El rol es obligatorio")
		Rol rol) {
}
