package com.foro.service;

import java.util.List;

import com.foro.web.dto.ComentarioRequest;
import com.foro.web.dto.ComentarioResponse;

public interface ComentarioService {

	List<ComentarioResponse> listarPorTema(Long temaId);

	ComentarioResponse crear(Long temaId, ComentarioRequest request);

	ComentarioResponse actualizar(Long id, ComentarioRequest request);

	void eliminar(Long id);

}
