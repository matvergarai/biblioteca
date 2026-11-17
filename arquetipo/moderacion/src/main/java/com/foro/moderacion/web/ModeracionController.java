package com.foro.moderacion.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foro.moderacion.service.ModeracionService;
import com.foro.moderacion.web.dto.BanRequest;
import com.foro.moderacion.web.dto.ComentarioModeracionResponse;
import com.foro.moderacion.web.dto.TemaModeracionResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/moderacion")
public class ModeracionController {

	private final ModeracionService moderacionService;

	public ModeracionController(ModeracionService moderacionService) {
		this.moderacionService = moderacionService;
	}

	@GetMapping("/temas")
	public List<TemaModeracionResponse> listarTemas() {
		return moderacionService.listarTemas();
	}

	@GetMapping("/comentarios")
	public List<ComentarioModeracionResponse> listarComentarios() {
		return moderacionService.listarComentarios();
	}

	@PutMapping("/temas/{id}/banear")
	public TemaModeracionResponse banearTema(@PathVariable Long id, @Valid @RequestBody BanRequest request) {
		return moderacionService.banearTema(id, request);
	}

	@PutMapping("/comentarios/{id}/banear")
	public ComentarioModeracionResponse banearComentario(@PathVariable Long id, @Valid @RequestBody BanRequest request) {
		return moderacionService.banearComentario(id, request);
	}

}
