package com.foro.service.impl;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.foro.exception.ConflictoException;
import com.foro.exception.CredencialesInvalidasException;
import com.foro.exception.RecursoNoEncontradoException;
import com.foro.mapper.UsuarioMapper;
import com.foro.model.Rol;
import com.foro.model.Usuario;
import com.foro.repository.UsuarioRepository;
import com.foro.web.dto.LoginRequest;
import com.foro.web.dto.UsuarioActualizacionRequest;
import com.foro.web.dto.UsuarioRequest;
import com.foro.web.dto.UsuarioResponse;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	private UsuarioServiceImpl usuarioService;

	@BeforeEach
	void setUp() {
		usuarioService = new UsuarioServiceImpl(usuarioRepository, new UsuarioMapper(), new BCryptPasswordEncoder());
	}

	@Test
	void crearGuardaElUsuarioSinDevolverLaClave() {
		when(usuarioRepository.existsByCorreo("ana@foro.cl")).thenReturn(false);
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
			Usuario usuario = invocation.getArgument(0);
			usuario.setId(1L);
			return usuario;
		});

		UsuarioResponse respuesta = usuarioService.crear(
				new UsuarioRequest("Ana Perez", "Ana@foro.cl", "secreto", Rol.USUARIO));

		assertThat(respuesta.id()).isEqualTo(1L);
		assertThat(respuesta.correo()).isEqualTo("ana@foro.cl");
		assertThat(respuesta.rol()).isEqualTo(Rol.USUARIO);
	}

	@Test
	void crearRechazaUnCorreoRepetido() {
		when(usuarioRepository.existsByCorreo("ana@foro.cl")).thenReturn(true);

		assertThatThrownBy(() -> usuarioService.crear(
				new UsuarioRequest("Ana Perez", "ana@foro.cl", "secreto", Rol.USUARIO)))
				.isInstanceOf(ConflictoException.class);
		verify(usuarioRepository, never()).save(any());
	}

	@Test
	void iniciarSesionAceptaLaClaveCorrecta() {
		Usuario usuario = new Usuario();
		usuario.setId(2L);
		usuario.setNombre("Admin");
		usuario.setCorreo("admin@foro.cl");
		usuario.setRol(Rol.ADMINISTRADOR);
		usuario.setClave(new BCryptPasswordEncoder().encode("secreto"));
		when(usuarioRepository.findByCorreo("admin@foro.cl")).thenReturn(Optional.of(usuario));

		UsuarioResponse respuesta = usuarioService.iniciarSesion(new LoginRequest("admin@foro.cl", "secreto"));

		assertThat(respuesta.rol()).isEqualTo(Rol.ADMINISTRADOR);
	}

	@Test
	void iniciarSesionRechazaUnaClaveIncorrecta() {
		Usuario usuario = new Usuario();
		usuario.setClave(new BCryptPasswordEncoder().encode("secreto"));
		when(usuarioRepository.findByCorreo("ana@foro.cl")).thenReturn(Optional.of(usuario));

		assertThatThrownBy(() -> usuarioService.iniciarSesion(new LoginRequest("ana@foro.cl", "otra")))
				.isInstanceOf(CredencialesInvalidasException.class);
	}

	@Test
	void actualizarPuedeCambiarElRolSinCambiarLaClave() {
		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setClave("hash-anterior");
		when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usuario));
		when(usuarioRepository.existsByCorreoAndIdNot("ana@foro.cl", 3L)).thenReturn(false);
		when(usuarioRepository.save(usuario)).thenReturn(usuario);

		UsuarioResponse respuesta = usuarioService.actualizar(3L,
				new UsuarioActualizacionRequest("Ana Perez", "ana@foro.cl", null, Rol.ADMINISTRADOR));

		assertThat(respuesta.rol()).isEqualTo(Rol.ADMINISTRADOR);
		assertThat(usuario.getClave()).isEqualTo("hash-anterior");
	}

	@Test
	void eliminarRechazaUnIdInexistente() {
		when(usuarioRepository.existsById(9L)).thenReturn(false);

		assertThatThrownBy(() -> usuarioService.eliminar(9L))
				.isInstanceOf(RecursoNoEncontradoException.class);
		verify(usuarioRepository, never()).deleteById(9L);
	}

}
