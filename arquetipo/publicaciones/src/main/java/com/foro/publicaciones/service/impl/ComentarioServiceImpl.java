package com.foro.publicaciones.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foro.publicaciones.exception.RecursoNoEncontradoException;
import com.foro.publicaciones.fabrica.ComentarioDatos;
import com.foro.publicaciones.fabrica.ComentarioFabrica;
import com.foro.publicaciones.model.Comentario;
import com.foro.publicaciones.model.EstadoPublicacion;
import com.foro.publicaciones.model.Tema;
import com.foro.publicaciones.model.Usuario;
import com.foro.publicaciones.repository.ComentarioRepository;
import com.foro.publicaciones.repository.TemaRepository;
import com.foro.publicaciones.repository.UsuarioRepository;
import com.foro.publicaciones.service.ComentarioService;
import com.foro.publicaciones.web.dto.ComentarioRequest;
import com.foro.publicaciones.web.dto.ComentarioResponse;

@Service
public class ComentarioServiceImpl implements ComentarioService {

	private final ComentarioRepository comentarioRepository;
	private final TemaRepository temaRepository;
	private final UsuarioRepository usuarioRepository;
	private final ComentarioFabrica comentarioFabrica;

	public ComentarioServiceImpl(
			ComentarioRepository comentarioRepository,
			TemaRepository temaRepository,
			UsuarioRepository usuarioRepository,
			ComentarioFabrica comentarioFabrica) {
		this.comentarioRepository = comentarioRepository;
		this.temaRepository = temaRepository;
		this.usuarioRepository = usuarioRepository;
		this.comentarioFabrica = comentarioFabrica;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ComentarioResponse> listarPorTema(Long temaId) {
		buscarTemaPublicado(temaId);
		return comentarioRepository
				.findByTemaIdAndEstadoOrderByFechaCreacionAsc(temaId, EstadoPublicacion.PUBLICADO).stream()
				.map(comentarioFabrica::aRespuesta)
				.toList();
	}

	@Override
	@Transactional
	public ComentarioResponse crear(Long temaId, ComentarioRequest request) {
		Comentario comentario = comentarioFabrica.crear(new ComentarioDatos(
				request,
				buscarTemaPublicado(temaId),
				buscarAutor(request.autorId())));
		return comentarioFabrica.aRespuesta(comentarioRepository.save(comentario));
	}

	@Override
	@Transactional
	public ComentarioResponse actualizar(Long id, ComentarioRequest request) {
		Comentario comentario = buscarPublicado(id);
		comentarioFabrica.actualizar(comentario, new ComentarioDatos(request, comentario.getTema(), buscarAutor(request.autorId())));
		return comentarioFabrica.aRespuesta(comentarioRepository.save(comentario));
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		if (!comentarioRepository.existsById(id)) {
			throw new RecursoNoEncontradoException("Comentario no encontrado con id " + id);
		}
		comentarioRepository.deleteById(id);
	}

	private Comentario buscarPublicado(Long id) {
		Comentario comentario = comentarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Comentario no encontrado con id " + id));
		if (comentario.getEstado() != EstadoPublicacion.PUBLICADO) {
			throw new RecursoNoEncontradoException("Comentario no encontrado con id " + id);
		}
		return comentario;
	}

	private Tema buscarTemaPublicado(Long id) {
		Tema tema = temaRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Tema no encontrado con id " + id));
		if (tema.getEstado() != EstadoPublicacion.PUBLICADO) {
			throw new RecursoNoEncontradoException("Tema no encontrado con id " + id);
		}
		return tema;
	}

	private Usuario buscarAutor(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id " + id));
	}

}
