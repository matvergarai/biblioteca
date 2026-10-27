package com.foro.service;

import java.util.List;

import com.foro.web.dto.TemaDetalleResponse;
import com.foro.web.dto.TemaRequest;
import com.foro.web.dto.TemaResponse;

public interface TemaService {

	List<TemaResponse> buscar(String texto, Long categoriaId);

	TemaDetalleResponse obtenerPorId(Long id);

	TemaResponse crear(TemaRequest request);

	TemaResponse actualizar(Long id, TemaRequest request);

	void eliminar(Long id);

}
