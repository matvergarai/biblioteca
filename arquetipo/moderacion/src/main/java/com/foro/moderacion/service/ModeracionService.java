package com.foro.moderacion.service;

import java.util.List;

import com.foro.moderacion.web.dto.BanRequest;
import com.foro.moderacion.web.dto.ComentarioModeracionResponse;
import com.foro.moderacion.web.dto.TemaModeracionResponse;

public interface ModeracionService {

	List<TemaModeracionResponse> listarTemas();

	List<ComentarioModeracionResponse> listarComentarios();

	TemaModeracionResponse banearTema(Long id, BanRequest request);

	ComentarioModeracionResponse banearComentario(Long id, BanRequest request);

}
