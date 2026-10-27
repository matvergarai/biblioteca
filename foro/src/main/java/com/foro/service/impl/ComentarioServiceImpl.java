package com.foro.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foro.exception.RecursoNoEncontradoException;
import com.foro.mapper.ForoMapper;
import com.foro.model.Comentario;
import com.foro.model.EstadoPublicacion;
import com.foro.model.Tema;
import com.foro.model.Usuario;
import com.foro.repository.ComentarioRepository;
import com.foro.repository.TemaRepository;
import com.foro.repository.UsuarioRepository;
import com.foro.service.ComentarioService;
import com.foro.web.dto.ComentarioRequest;
import com.foro.web.dto.ComentarioResponse;

@Service
public class ComentarioServiceImpl implements ComentarioService {

	private final ComentarioRepository comentarioRepository;
	private final TemaRepository temaRepository;
	private final UsuarioRepository usuarioRepository;
	private final ForoMapper foroMapper;

	public ComentarioServiceImpl(
			ComentarioRepository comentarioRepository,
			TemaRepository temaRepository,
			UsuarioRepository usuarioRepository,
			ForoMapper foroMapper) {
		this.comentarioRepository = comentarioRepository;
		this.temaRepository = temaRepository;
		this.usuarioRepository = usuarioRepository;
		this.foroMapper = foroMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ComentarioResponse> listarPorTema(Long temaId) {
		if (!temaRepository.existsById(temaId)) {
			throw new RecursoNoEncontradoException("Tema no encontrado con id " + temaId);
		}
		return comentarioRepository
				.findByTemaIdAndEstadoOrderByFechaCreacionAsc(temaId, EstadoPublicacion.PUBLICADO).stream()
				.map(foroMapper::toResponse)
				.toList();
	}

	@Override
	@Transactional
	public ComentarioResponse crear(Long temaId, ComentarioRequest request) {
		Comentario comentario = new Comentario();
		comentario.setTema(buscarTema(temaId));
		comentario.setAutor(buscarAutor(request.autorId()));
		comentario.setContenido(request.contenido().trim());
		comentario.setFechaCreacion(LocalDateTime.now());
		comentario.setEstado(EstadoPublicacion.PUBLICADO);
		return foroMapper.toResponse(comentarioRepository.save(comentario));
	}

	@Override
	@Transactional
	public ComentarioResponse actualizar(Long id, ComentarioRequest request) {
		Comentario comentario = buscar(id);
		comentario.setContenido(request.contenido().trim());
		comentario.setAutor(buscarAutor(request.autorId()));
		return foroMapper.toResponse(comentarioRepository.save(comentario));
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		if (!comentarioRepository.existsById(id)) {
			throw new RecursoNoEncontradoException("Comentario no encontrado con id " + id);
		}
		comentarioRepository.deleteById(id);
	}

	private Comentario buscar(Long id) {
		return comentarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Comentario no encontrado con id " + id));
	}

	private Tema buscarTema(Long id) {
		return temaRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Tema no encontrado con id " + id));
	}

	private Usuario buscarAutor(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id " + id));
	}

}
