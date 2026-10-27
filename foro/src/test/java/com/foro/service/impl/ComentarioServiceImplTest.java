package com.foro.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foro.exception.RecursoNoEncontradoException;
import com.foro.mapper.ForoMapper;
import com.foro.model.EstadoPublicacion;
import com.foro.model.Tema;
import com.foro.model.Usuario;
import com.foro.repository.ComentarioRepository;
import com.foro.repository.TemaRepository;
import com.foro.repository.UsuarioRepository;
import com.foro.web.dto.ComentarioRequest;
import com.foro.web.dto.ComentarioResponse;

@ExtendWith(MockitoExtension.class)
class ComentarioServiceImplTest {

	@Mock
	private ComentarioRepository comentarioRepository;

	@Mock
	private TemaRepository temaRepository;

	@Mock
	private UsuarioRepository usuarioRepository;

	private ComentarioServiceImpl comentarioService;

	@BeforeEach
	void setUp() {
		comentarioService = new ComentarioServiceImpl(
				comentarioRepository,
				temaRepository,
				usuarioRepository,
				new ForoMapper());
	}

	@Test
	void crearPublicaUnComentarioEnElTema() {
		when(temaRepository.findById(10L)).thenReturn(Optional.of(tema()));
		when(usuarioRepository.findById(2L)).thenReturn(Optional.of(autor()));
		when(comentarioRepository.save(any())).thenAnswer(invocation -> {
			var comentario = invocation.getArgument(0, com.foro.model.Comentario.class);
			comentario.setId(4L);
			return comentario;
		});

		ComentarioResponse respuesta = comentarioService.crear(10L,
				new ComentarioRequest("Yo use el alias biblio_low", 2L));

		assertThat(respuesta.id()).isEqualTo(4L);
		assertThat(respuesta.temaId()).isEqualTo(10L);
		assertThat(respuesta.autorNombre()).isEqualTo("Ana Perez");
		assertThat(respuesta.estado()).isEqualTo("PUBLICADO");
	}

	@Test
	void crearRechazaUnTemaInexistente() {
		when(temaRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> comentarioService.crear(99L, new ComentarioRequest("Hola", 2L)))
				.isInstanceOf(RecursoNoEncontradoException.class)
				.hasMessage("Tema no encontrado con id 99");
	}

	private Tema tema() {
		Tema tema = new Tema();
		tema.setId(10L);
		tema.setTitulo("Como conectar Oracle");
		tema.setEstado(EstadoPublicacion.PUBLICADO);
		return tema;
	}

	private Usuario autor() {
		Usuario usuario = new Usuario();
		usuario.setId(2L);
		usuario.setNombre("Ana Perez");
		return usuario;
	}

}
