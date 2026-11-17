package com.foro.publicaciones.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foro.publicaciones.fabrica.CategoriaFabrica;
import com.foro.publicaciones.repository.CategoriaRepository;
import com.foro.publicaciones.web.dto.CategoriaResponse;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	private final CategoriaRepository categoriaRepository;
	private final CategoriaFabrica categoriaFabrica;

	public CategoriaController(CategoriaRepository categoriaRepository, CategoriaFabrica categoriaFabrica) {
		this.categoriaRepository = categoriaRepository;
		this.categoriaFabrica = categoriaFabrica;
	}

	@GetMapping
	public List<CategoriaResponse> listar() {
		return categoriaRepository.findAll().stream()
				.map(categoriaFabrica::aRespuesta)
				.toList();
	}

}
