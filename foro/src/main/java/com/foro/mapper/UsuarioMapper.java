package com.foro.mapper;

import org.springframework.stereotype.Component;

import com.foro.model.Usuario;
import com.foro.web.dto.UsuarioResponse;

@Component
public class UsuarioMapper {

	public UsuarioResponse toResponse(Usuario usuario) {
		return new UsuarioResponse(
				usuario.getId(),
				usuario.getNombre(),
				usuario.getCorreo(),
				usuario.getRol());
	}

}
