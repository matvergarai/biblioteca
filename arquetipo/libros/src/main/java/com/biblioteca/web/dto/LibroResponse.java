package com.biblioteca.web.dto;

public record LibroResponse(
		Long id,
		String titulo,
		String autor,
		Integer anioPublicacion,
		String genero) {
}
