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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.foro.service.ComentarioService;
import com.foro.web.dto.ComentarioRequest;
import com.foro.web.dto.ComentarioResponse;

import jakarta.validation.Valid;

@RestController
public class ComentarioController {

	private final ComentarioService comentarioService;

	public ComentarioController(ComentarioService comentarioService) {
		this.comentarioService = comentarioService;
	}

	@GetMapping("/temas/{temaId}/comentarios")
	public List<ComentarioResponse> listar(@PathVariable Long temaId) {
		return comentarioService.listarPorTema(temaId);
	}

	@PostMapping("/temas/{temaId}/comentarios")
	@ResponseStatus(HttpStatus.CREATED)
	public ComentarioResponse crear(@PathVariable Long temaId, @Valid @RequestBody ComentarioRequest request) {
		return comentarioService.crear(temaId, request);
	}

	@PutMapping("/comentarios/{id}")
	public ComentarioResponse actualizar(@PathVariable Long id, @Valid @RequestBody ComentarioRequest request) {
		return comentarioService.actualizar(id, request);
	}

	@DeleteMapping("/comentarios/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		comentarioService.eliminar(id);
	}

}
