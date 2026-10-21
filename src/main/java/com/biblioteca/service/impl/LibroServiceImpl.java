package com.biblioteca.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.exception.RecursoNoEncontradoException;
import com.biblioteca.mapper.LibroMapper;
import com.biblioteca.model.Libro;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.service.LibroService;
import com.biblioteca.web.dto.LibroRequest;
import com.biblioteca.web.dto.LibroResponse;

@Service
public class LibroServiceImpl implements LibroService {

	private final LibroRepository libroRepository;
	private final LibroMapper libroMapper;

	public LibroServiceImpl(LibroRepository libroRepository, LibroMapper libroMapper) {
		this.libroRepository = libroRepository;
		this.libroMapper = libroMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<LibroResponse> listar() {
		return libroRepository.findAll().stream()
				.map(libroMapper::toResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public LibroResponse obtenerPorId(Long id) {
		return libroMapper.toResponse(buscarPorId(id));
	}

	@Override
	@Transactional
	public LibroResponse crear(LibroRequest request) {
		Libro libro = libroRepository.save(libroMapper.toEntity(request));
		return libroMapper.toResponse(libro);
	}

	@Override
	@Transactional
	public LibroResponse actualizar(Long id, LibroRequest request) {
		Libro libro = buscarPorId(id);
		libroMapper.actualizar(libro, request);
		return libroMapper.toResponse(libroRepository.save(libro));
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
