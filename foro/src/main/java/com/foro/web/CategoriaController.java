package com.foro.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foro.model.Categoria;
import com.foro.repository.CategoriaRepository;
import com.foro.web.dto.CategoriaResponse;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	private final CategoriaRepository categoriaRepository;

	public CategoriaController(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
	}

	@GetMapping
	public List<CategoriaResponse> listar() {
		return categoriaRepository.findAll().stream()
				.map(this::toResponse)
				.toList();
	}

	private CategoriaResponse toResponse(Categoria categoria) {
		return new CategoriaResponse(categoria.getId(), categoria.getNombre(), categoria.getDescripcion());
	}

}
