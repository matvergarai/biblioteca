package com.foro.publicaciones.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foro.publicaciones.exception.RecursoNoEncontradoException;
import com.foro.publicaciones.fabrica.ComentarioFabrica;
import com.foro.publicaciones.fabrica.TemaDatos;
import com.foro.publicaciones.fabrica.TemaFabrica;
import com.foro.publicaciones.model.Categoria;
import com.foro.publicaciones.model.EstadoPublicacion;
import com.foro.publicaciones.model.Tema;
import com.foro.publicaciones.model.Usuario;
import com.foro.publicaciones.repository.CategoriaRepository;
import com.foro.publicaciones.repository.ComentarioRepository;
import com.foro.publicaciones.repository.TemaRepository;
import com.foro.publicaciones.repository.UsuarioRepository;
import com.foro.publicaciones.service.TemaService;
import com.foro.publicaciones.web.dto.TemaDetalleResponse;
import com.foro.publicaciones.web.dto.TemaRequest;
import com.foro.publicaciones.web.dto.TemaResponse;

@Service
public class TemaServiceImpl implements TemaService {

	private final TemaRepository temaRepository;
	private final ComentarioRepository comentarioRepository;
	private final CategoriaRepository categoriaRepository;
	private final UsuarioRepository usuarioRepository;
	private final TemaFabrica temaFabrica;
	private final ComentarioFabrica comentarioFabrica;

	public TemaServiceImpl(
			TemaRepository temaRepository,
			ComentarioRepository comentarioRepository,
			CategoriaRepository categoriaRepository,
			UsuarioRepository usuarioRepository,
			TemaFabrica temaFabrica,
			ComentarioFabrica comentarioFabrica) {
		this.temaRepository = temaRepository;
		this.comentarioRepository = comentarioRepository;
		this.categoriaRepository = categoriaRepository;
		this.usuarioRepository = usuarioRepository;
		this.temaFabrica = temaFabrica;
		this.comentarioFabrica = comentarioFabrica;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TemaResponse> buscar(String texto, Long categoriaId) {
		String filtro = texto == null || texto.isBlank() ? null : texto.trim();
		return temaRepository.buscar(filtro, categoriaId).stream()
				.map(temaFabrica::aRespuesta)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public TemaDetalleResponse obtenerPorId(Long id) {
		Tema tema = buscarPublicado(id);
		var comentarios = comentarioRepository
				.findByTemaIdAndEstadoOrderByFechaCreacionAsc(id, EstadoPublicacion.PUBLICADO).stream()
				.map(comentarioFabrica::aRespuesta)
				.toList();
		return new TemaDetalleResponse(temaFabrica.aRespuesta(tema), comentarios);
	}

	@Override
	@Transactional
	public TemaResponse crear(TemaRequest request) {
		Tema tema = temaFabrica.crear(new TemaDatos(request, buscarCategoria(request.categoriaId()), buscarAutor(request.autorId())));
		return temaFabrica.aRespuesta(temaRepository.save(tema));
	}

	@Override
	@Transactional
	public TemaResponse actualizar(Long id, TemaRequest request) {
		Tema tema = buscarPublicado(id);
		temaFabrica.actualizar(tema, new TemaDatos(request, buscarCategoria(request.categoriaId()), buscarAutor(request.autorId())));
		return temaFabrica.aRespuesta(temaRepository.save(tema));
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		if (!temaRepository.existsById(id)) {
			throw new RecursoNoEncontradoException("Tema no encontrado con id " + id);
		}
		comentarioRepository.deleteByTemaId(id);
		temaRepository.deleteById(id);
	}

	private Tema buscarPublicado(Long id) {
		Tema tema = temaRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Tema no encontrado con id " + id));
		if (tema.getEstado() != EstadoPublicacion.PUBLICADO) {
			throw new RecursoNoEncontradoException("Tema no encontrado con id " + id);
		}
		return tema;
	}

	private Categoria buscarCategoria(Long id) {
		return categoriaRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Categoria no encontrada con id " + id));
	}

	private Usuario buscarAutor(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id " + id));
	}

}
