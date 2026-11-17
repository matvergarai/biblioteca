package com.foro.publicaciones.service;

import java.util.List;

import com.foro.publicaciones.web.dto.ComentarioRequest;
import com.foro.publicaciones.web.dto.ComentarioResponse;

public interface ComentarioService {

	List<ComentarioResponse> listarPorTema(Long temaId);

	ComentarioResponse crear(Long temaId, ComentarioRequest request);

	ComentarioResponse actualizar(Long id, ComentarioRequest request);

	void eliminar(Long id);

}
