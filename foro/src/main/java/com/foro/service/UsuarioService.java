package com.foro.service;

import java.util.List;

import com.foro.web.dto.LoginRequest;
import com.foro.web.dto.UsuarioActualizacionRequest;
import com.foro.web.dto.UsuarioRequest;
import com.foro.web.dto.UsuarioResponse;

public interface UsuarioService {

	List<UsuarioResponse> listar();

	UsuarioResponse obtenerPorId(Long id);

	UsuarioResponse crear(UsuarioRequest request);

	UsuarioResponse actualizar(Long id, UsuarioActualizacionRequest request);

	void eliminar(Long id);

	UsuarioResponse iniciarSesion(LoginRequest request);

}
