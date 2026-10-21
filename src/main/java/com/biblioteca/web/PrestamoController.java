package com.biblioteca.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.service.PrestamoService;
import com.biblioteca.web.dto.PrestamoRequest;
import com.biblioteca.web.dto.PrestamoResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

	private final PrestamoService prestamoService;

	public PrestamoController(PrestamoService prestamoService) {
		this.prestamoService = prestamoService;
	}

	@GetMapping
	public List<PrestamoResponse> listar() {
		return prestamoService.listar();
	}

	@GetMapping("/{id}")
	public PrestamoResponse obtenerPorId(@PathVariable Long id) {
		return prestamoService.obtenerPorId(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PrestamoResponse crear(@Valid @RequestBody PrestamoRequest request) {
		return prestamoService.crear(request);
	}

	@PutMapping("/{id}")
	public PrestamoResponse actualizar(@PathVariable Long id, @Valid @RequestBody PrestamoRequest request) {
		return prestamoService.actualizar(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		prestamoService.eliminar(id);
	}

}
