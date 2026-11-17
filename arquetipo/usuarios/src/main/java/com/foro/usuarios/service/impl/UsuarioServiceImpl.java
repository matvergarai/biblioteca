package com.foro.usuarios.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foro.usuarios.exception.ConflictoException;
import com.foro.usuarios.exception.CredencialesInvalidasException;
import com.foro.usuarios.exception.RecursoNoEncontradoException;
import com.foro.usuarios.fabrica.UsuarioFabrica;
import com.foro.usuarios.model.Usuario;
import com.foro.usuarios.repository.UsuarioRepository;
import com.foro.usuarios.service.UsuarioService;
import com.foro.usuarios.web.dto.LoginRequest;
import com.foro.usuarios.web.dto.UsuarioActualizacionRequest;
import com.foro.usuarios.web.dto.UsuarioRequest;
import com.foro.usuarios.web.dto.UsuarioResponse;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final UsuarioFabrica usuarioFabrica;
	private final PasswordEncoder passwordEncoder;

	public UsuarioServiceImpl(
			UsuarioRepository usuarioRepository,
			UsuarioFabrica usuarioFabrica,
			PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.usuarioFabrica = usuarioFabrica;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UsuarioResponse> listar() {
		return usuarioRepository.findAll().stream()
				.map(usuarioFabrica::aRespuesta)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public UsuarioResponse obtenerPorId(Long id) {
		return usuarioFabrica.aRespuesta(buscar(id));
	}

	@Override
	@Transactional
	public UsuarioResponse crear(UsuarioRequest request) {
		String correo = usuarioFabrica.normalizarCorreo(request.correo());
		if (usuarioRepository.existsByCorreo(correo)) {
			throw new ConflictoException("Ya existe un usuario con el correo " + correo);
		}
		return usuarioFabrica.aRespuesta(usuarioRepository.save(usuarioFabrica.crear(request)));
	}

	@Override
	@Transactional
	public UsuarioResponse actualizar(Long id, UsuarioActualizacionRequest request) {
		Usuario usuario = buscar(id);
		String correo = usuarioFabrica.normalizarCorreo(request.correo());
		if (usuarioRepository.existsByCorreoAndIdNot(correo, id)) {
			throw new ConflictoException("Ya existe un usuario con el correo " + correo);
		}
		usuarioFabrica.actualizar(usuario, request);
		return usuarioFabrica.aRespuesta(usuarioRepository.save(usuario));
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		if (!usuarioRepository.existsById(id)) {
			throw new RecursoNoEncontradoException("Usuario no encontrado con id " + id);
		}
		usuarioRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public UsuarioResponse iniciarSesion(LoginRequest request) {
		Usuario usuario = usuarioRepository.findByCorreo(usuarioFabrica.normalizarCorreo(request.correo()))
				.filter(encontrado -> passwordEncoder.matches(request.clave(), encontrado.getClave()))
				.orElseThrow(() -> new CredencialesInvalidasException("Correo o clave incorrectos"));
		return usuarioFabrica.aRespuesta(usuario);
	}

	private Usuario buscar(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id " + id));
	}

}
