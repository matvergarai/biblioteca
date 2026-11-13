package com.biblioteca.fabrica;

import org.springframework.stereotype.Component;

import com.biblioteca.model.Libro;
import com.biblioteca.web.dto.LibroRequest;
import com.biblioteca.web.dto.LibroResponse;

@Component
public class LibroFabrica extends FabricaBase<Libro, LibroRequest> {

	@Override
	protected Libro nuevaInstancia() {
		return new Libro();
	}

	@Override
	protected void completar(Libro libro, LibroRequest datos) {
		libro.setTitulo(datos.titulo().trim());
		libro.setAutor(datos.autor().trim());
		libro.setAnioPublicacion(datos.anioPublicacion());
		libro.setGenero(datos.genero().trim());
	}

	public void actualizar(Libro libro, LibroRequest datos) {
		completar(libro, datos);
	}

	public LibroResponse aRespuesta(Libro libro) {
		return new LibroResponse(
				libro.getId(),
				libro.getTitulo(),
				libro.getAutor(),
				libro.getAnioPublicacion(),
				libro.getGenero());
	}

}
