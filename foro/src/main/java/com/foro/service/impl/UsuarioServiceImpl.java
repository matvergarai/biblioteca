package com.foro.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foro.exception.ConflictoException;
import com.foro.exception.CredencialesInvalidasException;
import com.foro.exception.RecursoNoEncontradoException;
import com.foro.mapper.UsuarioMapper;
import com.foro.model.Usuario;
import com.foro.repository.UsuarioRepository;
import com.foro.service.UsuarioService;
import com.foro.web.dto.LoginRequest;
import com.foro.web.dto.UsuarioActualizacionRequest;
import com.foro.web.dto.UsuarioRequest;
import com.foro.web.dto.UsuarioResponse;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final UsuarioMapper usuarioMapper;
	private final PasswordEncoder passwordEncoder;

	public UsuarioServiceImpl(
			UsuarioRepository usuarioRepository,
			UsuarioMapper usuarioMapper,
			PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.usuarioMapper = usuarioMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UsuarioResponse> listar() {
		return usuarioRepository.findAll().stream()
				.map(usuarioMapper::toResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public UsuarioResponse obtenerPorId(Long id) {
		return usuarioMapper.toResponse(buscar(id));
	}

	@Override
	@Transactional
	public UsuarioResponse crear(UsuarioRequest request) {
		String correo = normalizarCorreo(request.correo());
		if (usuarioRepository.existsByCorreo(correo)) {
			throw new ConflictoException("Ya existe un usuario con el correo " + correo);
		}
		Usuario usuario = new Usuario();
		usuario.setNombre(request.nombre().trim());
		usuario.setCorreo(correo);
		usuario.setClave(passwordEncoder.encode(request.clave()));
		usuario.setRol(request.rol());
		return usuarioMapper.toResponse(usuarioRepository.save(usuario));
	}

	@Override
	@Transactional
	public UsuarioResponse actualizar(Long id, UsuarioActualizacionRequest request) {
		Usuario usuario = buscar(id);
		String correo = normalizarCorreo(request.correo());
		if (usuarioRepository.existsByCorreoAndIdNot(correo, id)) {
			throw new ConflictoException("Ya existe un usuario con el correo " + correo);
		}
		usuario.setNombre(request.nombre().trim());
		usuario.setCorreo(correo);
		usuario.setRol(request.rol());
		if (request.clave() != null && !request.clave().isBlank()) {
			usuario.setClave(passwordEncoder.encode(request.clave()));
		}
		return usuarioMapper.toResponse(usuarioRepository.save(usuario));
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
		Usuario usuario = usuarioRepository.findByCorreo(normalizarCorreo(request.correo()))
				.filter(encontrado -> passwordEncoder.matches(request.clave(), encontrado.getClave()))
				.orElseThrow(() -> new CredencialesInvalidasException("Correo o clave incorrectos"));
		return usuarioMapper.toResponse(usuario);
	}

	private Usuario buscar(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id " + id));
	}

	private String normalizarCorreo(String correo) {
		return correo.trim().toLowerCase();
	}

}
