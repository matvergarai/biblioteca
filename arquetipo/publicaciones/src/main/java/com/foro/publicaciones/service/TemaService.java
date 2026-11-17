package com.foro.publicaciones.service;

import java.util.List;

import com.foro.publicaciones.web.dto.TemaDetalleResponse;
import com.foro.publicaciones.web.dto.TemaRequest;
import com.foro.publicaciones.web.dto.TemaResponse;

public interface TemaService {

	List<TemaResponse> buscar(String texto, Long categoriaId);

	TemaDetalleResponse obtenerPorId(Long id);

	TemaResponse crear(TemaRequest request);

	TemaResponse actualizar(Long id, TemaRequest request);

	void eliminar(Long id);

}
