package com.foro.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.foro.service.TemaService;
import com.foro.web.dto.TemaDetalleResponse;
import com.foro.web.dto.TemaRequest;
import com.foro.web.dto.TemaResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/temas")
public class TemaController {

	private final TemaService temaService;

	public TemaController(TemaService temaService) {
		this.temaService = temaService;
	}

	@GetMapping
	public List<TemaResponse> buscar(
			@RequestParam(required = false) String texto,
			@RequestParam(required = false) Long categoriaId) {
		return temaService.buscar(texto, categoriaId);
	}

	@GetMapping("/{id}")
	public TemaDetalleResponse obtenerPorId(@PathVariable Long id) {
		return temaService.obtenerPorId(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TemaResponse crear(@Valid @RequestBody TemaRequest request) {
		return temaService.crear(request);
	}

	@PutMapping("/{id}")
	public TemaResponse actualizar(@PathVariable Long id, @Valid @RequestBody TemaRequest request) {
		return temaService.actualizar(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		temaService.eliminar(id);
	}

}
