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

import com.biblioteca.service.LibroService;
import com.biblioteca.web.dto.LibroRequest;
import com.biblioteca.web.dto.LibroResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/libros")
public class LibroController {

	private final LibroService libroService;

	public LibroController(LibroService libroService) {
		this.libroService = libroService;
	}

	@GetMapping
	public List<LibroResponse> listar() {
		return libroService.listar();
	}

	@GetMapping("/{id}")
	public LibroResponse obtenerPorId(@PathVariable Long id) {
		return libroService.obtenerPorId(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LibroResponse crear(@Valid @RequestBody LibroRequest request) {
		return libroService.crear(request);
	}

	@PutMapping("/{id}")
	public LibroResponse actualizar(@PathVariable Long id, @Valid @RequestBody LibroRequest request) {
		return libroService.actualizar(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		libroService.eliminar(id);
	}

}
