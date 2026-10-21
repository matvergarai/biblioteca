package com.biblioteca.service;

import java.util.List;

import com.biblioteca.web.dto.PrestamoRequest;
import com.biblioteca.web.dto.PrestamoResponse;

public interface PrestamoService {

	List<PrestamoResponse> listar();

	PrestamoResponse obtenerPorId(Long id);

	PrestamoResponse crear(PrestamoRequest request);

	PrestamoResponse actualizar(Long id, PrestamoRequest request);

	void eliminar(Long id);

}
