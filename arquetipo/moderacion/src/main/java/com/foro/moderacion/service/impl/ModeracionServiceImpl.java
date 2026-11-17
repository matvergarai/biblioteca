package com.foro.moderacion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foro.moderacion.exception.AccesoDenegadoException;
import com.foro.moderacion.exception.RecursoNoEncontradoException;
import com.foro.moderacion.fabrica.DecisionModeracion;
import com.foro.moderacion.fabrica.SancionFabrica;
import com.foro.moderacion.fabrica.SolicitudBan;
import com.foro.moderacion.model.Comentario;
import com.foro.moderacion.model.Rol;
import com.foro.moderacion.model.Tema;
import com.foro.moderacion.model.Usuario;
import com.foro.moderacion.repository.ComentarioRepository;
import com.foro.moderacion.repository.TemaRepository;
import com.foro.moderacion.repository.UsuarioRepository;
import com.foro.moderacion.service.ModeracionService;
import com.foro.moderacion.web.dto.BanRequest;
import com.foro.moderacion.web.dto.ComentarioModeracionResponse;
import com.foro.moderacion.web.dto.TemaModeracionResponse;

@Service
public class ModeracionServiceImpl implements ModeracionService {

	private final TemaRepository temaRepository;
	private final ComentarioRepository comentarioRepository;
	private final UsuarioRepository usuarioRepository;
	private final SancionFabrica sancionFabrica;

	public ModeracionServiceImpl(
			TemaRepository temaRepository,
			ComentarioRepository comentarioRepository,
			UsuarioRepository usuarioRepository,
			SancionFabrica sancionFabrica) {
		this.temaRepository = temaRepository;
		this.comentarioRepository = comentarioRepository;
		this.usuarioRepository = usuarioRepository;
		this.sancionFabrica = sancionFabrica;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TemaModeracionResponse> listarTemas() {
		return temaRepository.listarConAutor().stream()
				.map(this::aRespuesta)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ComentarioModeracionResponse> listarComentarios() {
		return comentarioRepository.listarConAutor().stream()
				.map(this::aRespuesta)
				.toList();
	}

	@Override
	@Transactional
	public TemaModeracionResponse banearTema(Long id, BanRequest request) {
		Tema tema = temaRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Tema no encontrado con id " + id));
		DecisionModeracion decision = decisionDe(request);
		sancionFabrica.aplicar(tema, decision);
		return aRespuesta(temaRepository.save(tema));
	}

	@Override
	@Transactional
	public ComentarioModeracionResponse banearComentario(Long id, BanRequest request) {
		Comentario comentario = comentarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Comentario no encontrado con id " + id));
		DecisionModeracion decision = decisionDe(request);
		sancionFabrica.aplicar(comentario, decision);
		return aRespuesta(comentarioRepository.save(comentario));
	}

	private DecisionModeracion decisionDe(BanRequest request) {
		Usuario administrador = usuarioRepository.findById(request.administradorId())
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"Usuario no encontrado con id " + request.administradorId()));
		if (administrador.getRol() != Rol.ADMINISTRADOR) {
			throw new AccesoDenegadoException("Solo un administrador puede banear publicaciones");
		}
		return sancionFabrica.crear(new SolicitudBan(administrador.getId()));
	}

	private TemaModeracionResponse aRespuesta(Tema tema) {
		return new TemaModeracionResponse(
				tema.getId(),
				tema.getTitulo(),
				tema.getContenido(),
				tema.getFechaCreacion(),
				tema.getEstado().name(),
				tema.getAutor().getId(),
				tema.getAutor().getNombre());
	}

	private ComentarioModeracionResponse aRespuesta(Comentario comentario) {
		return new ComentarioModeracionResponse(
				comentario.getId(),
				comentario.getContenido(),
				comentario.getFechaCreacion(),
				comentario.getEstado().name(),
				comentario.getTema().getId(),
				comentario.getAutor().getId(),
				comentario.getAutor().getNombre());
	}

}
