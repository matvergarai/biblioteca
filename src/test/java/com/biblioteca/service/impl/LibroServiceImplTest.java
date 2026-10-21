package com.biblioteca.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.biblioteca.exception.RecursoNoEncontradoException;
import com.biblioteca.mapper.LibroMapper;
import com.biblioteca.model.Libro;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.web.dto.LibroRequest;
import com.biblioteca.web.dto.LibroResponse;

@ExtendWith(MockitoExtension.class)
class LibroServiceImplTest {

	@Mock
	private LibroRepository libroRepository;

	private LibroServiceImpl libroService;

	@BeforeEach
	void setUp() {
		libroService = new LibroServiceImpl(libroRepository, new LibroMapper());
	}

	@Test
	void crearGuardaElLibroYDevuelveSuId() {
		when(libroRepository.save(any(Libro.class))).thenAnswer(invocation -> {
			Libro libro = invocation.getArgument(0);
			libro.setId(1L);
			return libro;
		});

		LibroResponse respuesta = libroService.crear(solicitud());

		assertThat(respuesta.id()).isEqualTo(1L);
		assertThat(respuesta.titulo()).isEqualTo("Cien anios de soledad");
		assertThat(respuesta.autor()).isEqualTo("Gabriel Garcia Marquez");
		assertThat(respuesta.anioPublicacion()).isEqualTo(1967);
		assertThat(respuesta.genero()).isEqualTo("Novela");
	}

	@Test
	void obtenerPorIdLanzaExcepcionSiNoExiste() {
		when(libroRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> libroService.obtenerPorId(99L))
				.isInstanceOf(RecursoNoEncontradoException.class)
				.hasMessage("Libro no encontrado con id 99");
	}

	@Test
	void actualizarModificaElLibroExistente() {
		Libro existente = new Libro();
		existente.setId(4L);
		existente.setTitulo("Titulo viejo");
		existente.setAutor("Autor viejo");
		existente.setAnioPublicacion(1900);
		existente.setGenero("Ensayo");
		when(libroRepository.findById(4L)).thenReturn(Optional.of(existente));
		when(libroRepository.save(existente)).thenReturn(existente);

		LibroResponse respuesta = libroService.actualizar(4L, solicitud());

		assertThat(respuesta.id()).isEqualTo(4L);
		assertThat(respuesta.titulo()).isEqualTo("Cien anios de soledad");
		assertThat(existente.getGenero()).isEqualTo("Novela");
	}

	@Test
	void eliminarRechazaUnIdInexistente() {
		when(libroRepository.existsById(8L)).thenReturn(false);

		assertThatThrownBy(() -> libroService.eliminar(8L))
				.isInstanceOf(RecursoNoEncontradoException.class);
		verify(libroRepository, never()).deleteById(8L);
	}

	private LibroRequest solicitud() {
		return new LibroRequest("Cien anios de soledad", "Gabriel Garcia Marquez", 1967, "Novela");
	}

}
