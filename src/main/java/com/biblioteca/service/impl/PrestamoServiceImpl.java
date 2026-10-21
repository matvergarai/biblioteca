package com.biblioteca.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.exception.ConflictoException;
import com.biblioteca.exception.RecursoNoEncontradoException;
import com.biblioteca.exception.SolicitudInvalidaException;
import com.biblioteca.mapper.PrestamoMapper;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.service.PrestamoService;
import com.biblioteca.web.dto.PrestamoRequest;
import com.biblioteca.web.dto.PrestamoResponse;

@Service
public class PrestamoServiceImpl implements PrestamoService {

	private final PrestamoRepository prestamoRepository;
	private final LibroRepository libroRepository;
	private final PrestamoMapper prestamoMapper;

	public PrestamoServiceImpl(
			PrestamoRepository prestamoRepository,
			LibroRepository libroRepository,
			PrestamoMapper prestamoMapper) {
		this.prestamoRepository = prestamoRepository;
		this.libroRepository = libroRepository;
		this.prestamoMapper = prestamoMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PrestamoResponse> listar() {
		return prestamoRepository.findAll().stream()
				.map(prestamoMapper::toResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public PrestamoResponse obtenerPorId(Long id) {
		return prestamoMapper.toResponse(buscarPorId(id));
	}

	@Override
	@Transactional
	public PrestamoResponse crear(PrestamoRequest request) {
		validarFechas(request.fechaPrestamo(), request.fechaDevolucion());
		Libro libro = buscarLibro(request.libroId());
		validarDisponibilidad(libro.getId(), request.fechaDevolucion(), null);
		Prestamo prestamo = prestamoRepository.save(prestamoMapper.toEntity(request, libro));
		return prestamoMapper.toResponse(prestamo);
	}

	@Override
	@Transactional
	public PrestamoResponse actualizar(Long id, PrestamoRequest request) {
		validarFechas(request.fechaPrestamo(), request.fechaDevolucion());
		Prestamo prestamo = buscarPorId(id);
		Libro libro = buscarLibro(request.libroId());
		validarDisponibilidad(libro.getId(), request.fechaDevolucion(), id);
		prestamoMapper.actualizar(prestamo, request, libro);
		return prestamoMapper.toResponse(prestamoRepository.save(prestamo));
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		if (!prestamoRepository.existsById(id)) {
			throw new RecursoNoEncontradoException("Prestamo no encontrado con id " + id);
		}
		prestamoRepository.deleteById(id);
	}

	private Prestamo buscarPorId(Long id) {
		return prestamoRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Prestamo no encontrado con id " + id));
	}

	private Libro buscarLibro(Long libroId) {
		return libroRepository.findById(libroId)
				.orElseThrow(() -> new RecursoNoEncontradoException("Libro no encontrado con id " + libroId));
	}

	private void validarFechas(LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
		if (fechaDevolucion != null && fechaDevolucion.isBefore(fechaPrestamo)) {
			throw new SolicitudInvalidaException(
					"La fecha de devolucion no puede ser anterior a la fecha de prestamo");
		}
	}

	private void validarDisponibilidad(Long libroId, LocalDate fechaDevolucion, Long prestamoId) {
		if (fechaDevolucion != null) {
			return;
		}
		boolean ocupado = prestamoId == null
				? prestamoRepository.existsByLibro_IdAndFechaDevolucionIsNull(libroId)
				: prestamoRepository.existsByLibro_IdAndFechaDevolucionIsNullAndIdNot(libroId, prestamoId);
		if (ocupado) {
			throw new ConflictoException("El libro ya tiene un prestamo activo");
		}
	}

}
