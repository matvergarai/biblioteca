package com.foro.usuarios.web;

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

import com.foro.usuarios.service.UsuarioService;
import com.foro.usuarios.web.dto.LoginRequest;
import com.foro.usuarios.web.dto.UsuarioActualizacionRequest;
import com.foro.usuarios.web.dto.UsuarioRequest;
import com.foro.usuarios.web.dto.UsuarioResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping
	public List<UsuarioResponse> listar() {
		return usuarioService.listar();
	}

	@GetMapping("/{id}")
	public UsuarioResponse obtenerPorId(@PathVariable Long id) {
		return usuarioService.obtenerPorId(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioResponse crear(@Valid @RequestBody UsuarioRequest request) {
		return usuarioService.crear(request);
	}

	@PutMapping("/{id}")
	public UsuarioResponse actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioActualizacionRequest request) {
		return usuarioService.actualizar(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		usuarioService.eliminar(id);
	}

	@PostMapping("/login")
	public UsuarioResponse iniciarSesion(@Valid @RequestBody LoginRequest request) {
		return usuarioService.iniciarSesion(request);
	}

}
