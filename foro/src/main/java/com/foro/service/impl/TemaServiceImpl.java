package com.foro.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.foro.service.TemaService;
import com.foro.web.dto.ComentarioResponse;
import com.foro.web.dto.TemaDetalleResponse;
import com.foro.web.dto.TemaRequest;
import com.foro.web.dto.TemaResponse;

@Service
public class TemaServiceImpl implements TemaService {

	private final TemaRepository temaRepository;
	private final ComentarioRepository comentarioRepository;
	private final CategoriaRepository categoriaRepository;
	private final UsuarioRepository usuarioRepository;
	private final ForoMapper foroMapper;

	public TemaServiceImpl(
			TemaRepository temaRepository,
			ComentarioRepository comentarioRepository,
			CategoriaRepository categoriaRepository,
			UsuarioRepository usuarioRepository,
			ForoMapper foroMapper) {
		this.temaRepository = temaRepository;
		this.comentarioRepository = comentarioRepository;
		this.categoriaRepository = categoriaRepository;
		this.usuarioRepository = usuarioRepository;
		this.foroMapper = foroMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TemaResponse> buscar(String texto, Long categoriaId) {
		String filtro = texto == null || texto.isBlank() ? null : texto.trim();
		return temaRepository.buscar(filtro, categoriaId).stream()
				.map(foroMapper::toResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public TemaDetalleResponse obtenerPorId(Long id) {
		Tema tema = buscar(id);
		List<ComentarioResponse> comentarios = comentarioRepository
				.findByTemaIdAndEstadoOrderByFechaCreacionAsc(id, EstadoPublicacion.PUBLICADO).stream()
				.map(foroMapper::toResponse)
				.toList();
		return new TemaDetalleResponse(foroMapper.toResponse(tema), comentarios);
	}

	@Override
	@Transactional
	public TemaResponse crear(TemaRequest request) {
		Tema tema = new Tema();
		completar(tema, request);
		tema.setFechaCreacion(LocalDateTime.now());
		tema.setEstado(EstadoPublicacion.PUBLICADO);
		return foroMapper.toResponse(temaRepository.save(tema));
	}

	@Override
	@Transactional
	public TemaResponse actualizar(Long id, TemaRequest request) {
		Tema tema = buscar(id);
		completar(tema, request);
		return foroMapper.toResponse(temaRepository.save(tema));
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

	private void completar(Tema tema, TemaRequest request) {
		tema.setTitulo(request.titulo().trim());
		tema.setContenido(request.contenido().trim());
		tema.setCategoria(buscarCategoria(request.categoriaId()));
		tema.setAutor(buscarAutor(request.autorId()));
	}

	private Tema buscar(Long id) {
		return temaRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Tema no encontrado con id " + id));
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
