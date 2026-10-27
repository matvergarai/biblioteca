package com.foro.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foro.exception.RecursoNoEncontradoException;
import com.foro.mapper.ForoMapper;
import com.foro.model.Categoria;
import com.foro.model.EstadoPublicacion;
import com.foro.model.Tema;
import com.foro.model.Usuario;
import com.foro.repository.CategoriaRepository;
import com.foro.repository.ComentarioRepository;
import com.foro.repository.TemaRepository;
import com.foro.repository.UsuarioRepository;
import com.foro.web.dto.TemaRequest;
import com.foro.web.dto.TemaResponse;

@ExtendWith(MockitoExtension.class)
class TemaServiceImplTest {

	@Mock
	private TemaRepository temaRepository;

	@Mock
	private ComentarioRepository comentarioRepository;

	@Mock
	private CategoriaRepository categoriaRepository;

	@Mock
	private UsuarioRepository usuarioRepository;

	private TemaServiceImpl temaService;

	@BeforeEach
	void setUp() {
		temaService = new TemaServiceImpl(
				temaRepository,
				comentarioRepository,
				categoriaRepository,
				usuarioRepository,
				new ForoMapper());
	}

	@Test
	void crearPublicaUnTema() {
		when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria()));
		when(usuarioRepository.findById(2L)).thenReturn(Optional.of(autor()));
		when(temaRepository.save(any(Tema.class))).thenAnswer(invocation -> {
			Tema tema = invocation.getArgument(0);
			tema.setId(10L);
			return tema;
		});

		TemaResponse respuesta = temaService.crear(solicitud());

		assertThat(respuesta.id()).isEqualTo(10L);
		assertThat(respuesta.titulo()).isEqualTo("Como conectar Oracle");
		assertThat(respuesta.estado()).isEqualTo("PUBLICADO");
		assertThat(respuesta.categoriaNombre()).isEqualTo("Bases de datos");
		assertThat(respuesta.autorNombre()).isEqualTo("Ana Perez");
		assertThat(respuesta.fechaCreacion()).isNotNull();
	}

	@Test
	void crearRechazaUnaCategoriaInexistente() {
		when(categoriaRepository.findById(9L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> temaService.crear(
				new TemaRequest("Titulo", "Contenido del tema", 9L, 2L)))
				.isInstanceOf(RecursoNoEncontradoException.class)
				.hasMessage("Categoria no encontrada con id 9");
		verify(temaRepository, never()).save(any());
	}

	@Test
	void buscarFiltraPorTextoYCategoria() {
		Tema tema = temaPublicado();
		when(temaRepository.buscar("oracle", 1L)).thenReturn(List.of(tema));

		List<TemaResponse> resultado = temaService.buscar("  oracle  ", 1L);

		assertThat(resultado).hasSize(1);
		assertThat(resultado.get(0).titulo()).isEqualTo("Como conectar Oracle");
	}

	@Test
	void eliminarBorraTambienLosComentarios() {
		when(temaRepository.existsById(10L)).thenReturn(true);

		temaService.eliminar(10L);

		verify(comentarioRepository).deleteByTemaId(10L);
		verify(temaRepository).deleteById(10L);
	}

	private TemaRequest solicitud() {
		return new TemaRequest("Como conectar Oracle", "Uso el wallet y JPA", 1L, 2L);
	}

	private Tema temaPublicado() {
		Tema tema = new Tema();
		tema.setId(10L);
		tema.setTitulo("Como conectar Oracle");
		tema.setContenido("Uso el wallet y JPA");
		tema.setEstado(EstadoPublicacion.PUBLICADO);
		tema.setCategoria(categoria());
		tema.setAutor(autor());
		return tema;
	}

	private Categoria categoria() {
		Categoria categoria = new Categoria();
		categoria.setId(1L);
		categoria.setNombre("Bases de datos");
		categoria.setDescripcion("Modelado, SQL y Oracle");
		return categoria;
	}

	private Usuario autor() {
		Usuario usuario = new Usuario();
		usuario.setId(2L);
		usuario.setNombre("Ana Perez");
		usuario.setCorreo("ana@foro.cl");
		return usuario;
	}

}
