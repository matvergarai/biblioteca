package com.foro.usuarios.fabrica;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.foro.usuarios.model.Usuario;
import com.foro.usuarios.web.dto.UsuarioActualizacionRequest;
import com.foro.usuarios.web.dto.UsuarioRequest;
import com.foro.usuarios.web.dto.UsuarioResponse;

@Component
public class UsuarioFabrica extends FabricaBase<Usuario, UsuarioRequest> {

	private final PasswordEncoder passwordEncoder;

	public UsuarioFabrica(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected Usuario nuevaInstancia() {
		return new Usuario();
	}

	@Override
	protected void completar(Usuario usuario, UsuarioRequest datos) {
		usuario.setNombre(datos.nombre().trim());
		usuario.setCorreo(normalizarCorreo(datos.correo()));
		usuario.setClave(passwordEncoder.encode(datos.clave()));
		usuario.setRol(datos.rol());
	}

	public void actualizar(Usuario usuario, UsuarioActualizacionRequest datos) {
		usuario.setNombre(datos.nombre().trim());
		usuario.setCorreo(normalizarCorreo(datos.correo()));
		usuario.setRol(datos.rol());
		if (datos.clave() != null && !datos.clave().isBlank()) {
			usuario.setClave(passwordEncoder.encode(datos.clave()));
		}
	}

	public UsuarioResponse aRespuesta(Usuario usuario) {
		return new UsuarioResponse(
				usuario.getId(),
				usuario.getNombre(),
				usuario.getCorreo(),
				usuario.getRol());
	}

	public String normalizarCorreo(String correo) {
		return correo.trim().toLowerCase();
	}

}
