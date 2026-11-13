package com.biblioteca.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.exception.RecursoNoEncontradoException;
import com.biblioteca.fabrica.LibroFabrica;
import com.biblioteca.model.Libro;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.service.LibroService;
import com.biblioteca.web.dto.LibroRequest;
import com.biblioteca.web.dto.LibroResponse;

@Service
public class LibroServiceImpl implements LibroService {

	private final LibroRepository libroRepository;
	private final LibroFabrica libroFabrica;

	public LibroServiceImpl(LibroRepository libroRepository, LibroFabrica libroFabrica) {
		this.libroRepository = libroRepository;
		this.libroFabrica = libroFabrica;
	}

	@Override
	@Transactional(readOnly = true)
	public List<LibroResponse> listar() {
		return libroRepository.findAll().stream()
				.map(libroFabrica::aRespuesta)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public LibroResponse obtenerPorId(Long id) {
		return libroFabrica.aRespuesta(buscarPorId(id));
	}

	@Override
	@Transactional
	public LibroResponse crear(LibroRequest request) {
		Libro libro = libroRepository.save(libroFabrica.crear(request));
		return libroFabrica.aRespuesta(libro);
	}

	@Override
	@Transactional
	public LibroResponse actualizar(Long id, LibroRequest request) {
		Libro libro = buscarPorId(id);
		libroFabrica.actualizar(libro, request);
		return libroFabrica.aRespuesta(libroRepository.save(libro));
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		if (!libroRepository.existsById(id)) {
			throw new RecursoNoEncontradoException("Libro no encontrado con id " + id);
		}
		libroRepository.deleteById(id);
	}

	private Libro buscarPorId(Long id) {
		return libroRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Libro no encontrado con id " + id));
	}

}
