package com.biblioteca.service;

import java.util.List;

import com.biblioteca.web.dto.LibroRequest;
import com.biblioteca.web.dto.LibroResponse;

public interface LibroService {

	List<LibroResponse> listar();

	LibroResponse obtenerPorId(Long id);

	LibroResponse crear(LibroRequest request);

	LibroResponse actualizar(Long id, LibroRequest request);

	void eliminar(Long id);

}
