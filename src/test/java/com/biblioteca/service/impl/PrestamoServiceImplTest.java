package com.biblioteca.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.biblioteca.exception.ConflictoException;
import com.biblioteca.exception.RecursoNoEncontradoException;
import com.biblioteca.exception.SolicitudInvalidaException;
import com.biblioteca.mapper.PrestamoMapper;
import com.biblioteca.model.EstadoPrestamo;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.web.dto.PrestamoRequest;
import com.biblioteca.web.dto.PrestamoResponse;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceImplTest {

	@Mock
	private PrestamoRepository prestamoRepository;

	@Mock
	private LibroRepository libroRepository;

	private PrestamoServiceImpl prestamoService;

	@BeforeEach
	void setUp() {
		prestamoService = new PrestamoServiceImpl(prestamoRepository, libroRepository, new PrestamoMapper());
	}

	@Test
	void crearGuardaUnPrestamoActivo() {
		when(libroRepository.findById(1L)).thenReturn(Optional.of(libro()));
		when(prestamoRepository.existsByLibro_IdAndFechaDevolucionIsNull(1L)).thenReturn(false);
		when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(invocation -> {
			Prestamo prestamo = invocation.getArgument(0);
			prestamo.setId(10L);
			return prestamo;
		});

		PrestamoResponse respuesta = prestamoService.crear(solicitudActiva());

		assertThat(respuesta.id()).isEqualTo(10L);
		assertThat(respuesta.libroId()).isEqualTo(1L);
		assertThat(respuesta.tituloLibro()).isEqualTo("Cien anios de soledad");
		assertThat(respuesta.prestatario()).isEqualTo("Ana Perez");
		assertThat(respuesta.estado()).isEqualTo("ACTIVO");
		assertThat(respuesta.fechaDevolucion()).isNull();
	}

	@Test
	void crearRechazaUnLibroInexistente() {
		when(libroRepository.findById(5L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> prestamoService.crear(
				new PrestamoRequest(5L, "Ana Perez", LocalDate.of(2026, 10, 21), null)))
				.isInstanceOf(RecursoNoEncontradoException.class)
				.hasMessage("Libro no encontrado con id 5");
	}

	@Test
	void crearRechazaUnSegundoPrestamoActivo() {
		when(libroRepository.findById(1L)).thenReturn(Optional.of(libro()));
		when(prestamoRepository.existsByLibro_IdAndFechaDevolucionIsNull(1L)).thenReturn(true);

		assertThatThrownBy(() -> prestamoService.crear(solicitudActiva()))
				.isInstanceOf(ConflictoException.class)
				.hasMessage("El libro ya tiene un prestamo activo");
		verify(prestamoRepository, never()).save(any());
	}

	@Test
	void actualizarMarcaElPrestamoComoDevuelto() {
		Prestamo existente = new Prestamo();
		existente.setId(10L);
		existente.setLibro(libro());
		existente.setPrestatario("Ana Perez");
		existente.setFechaPrestamo(LocalDate.of(2026, 10, 21));
		existente.setEstado(EstadoPrestamo.ACTIVO);
		when(prestamoRepository.findById(10L)).thenReturn(Optional.of(existente));
		when(libroRepository.findById(1L)).thenReturn(Optional.of(libro()));
		when(prestamoRepository.save(existente)).thenReturn(existente);

		PrestamoResponse respuesta = prestamoService.actualizar(10L, solicitudDevuelta());

		assertThat(respuesta.estado()).isEqualTo("DEVUELTO");
		assertThat(respuesta.fechaDevolucion()).isEqualTo(LocalDate.of(2026, 10, 28));
	}

	@Test
	void crearRechazaUnaDevolucionAnteriorAlPrestamo() {
		assertThatThrownBy(() -> prestamoService.crear(
				new PrestamoRequest(1L, "Ana Perez", LocalDate.of(2026, 10, 21), LocalDate.of(2026, 10, 1))))
				.isInstanceOf(SolicitudInvalidaException.class);
		verify(libroRepository, never()).findById(any());
	}

	@Test
	void eliminarRechazaUnIdInexistente() {
		when(prestamoRepository.existsById(8L)).thenReturn(false);

		assertThatThrownBy(() -> prestamoService.eliminar(8L))
				.isInstanceOf(RecursoNoEncontradoException.class);
		verify(prestamoRepository, never()).deleteById(8L);
	}

	private Libro libro() {
		Libro libro = new Libro();
		libro.setId(1L);
		libro.setTitulo("Cien anios de soledad");
		libro.setAutor("Gabriel Garcia Marquez");
		libro.setAnioPublicacion(1967);
		libro.setGenero("Novela");
		return libro;
	}

	private PrestamoRequest solicitudActiva() {
		return new PrestamoRequest(1L, "Ana Perez", LocalDate.of(2026, 10, 21), null);
	}

	private PrestamoRequest solicitudDevuelta() {
		return new PrestamoRequest(1L, "Ana Perez", LocalDate.of(2026, 10, 21), LocalDate.of(2026, 10, 28));
	}

}
