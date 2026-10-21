package com.biblioteca.mapper;

import org.springframework.stereotype.Component;

import com.biblioteca.model.Libro;
import com.biblioteca.web.dto.LibroRequest;
import com.biblioteca.web.dto.LibroResponse;

@Component
public class LibroMapper {

	public Libro toEntity(LibroRequest request) {
		Libro libro = new Libro();
		actualizar(libro, request);
		return libro;
	}

	public void actualizar(Libro libro, LibroRequest request) {
		libro.setTitulo(request.titulo().trim());
		libro.setAutor(request.autor().trim());
		libro.setAnioPublicacion(request.anioPublicacion());
		libro.setGenero(request.genero().trim());
	}

	public LibroResponse toResponse(Libro libro) {
		return new LibroResponse(
				libro.getId(),
				libro.getTitulo(),
				libro.getAutor(),
				libro.getAnioPublicacion(),
				libro.getGenero());
	}

}
