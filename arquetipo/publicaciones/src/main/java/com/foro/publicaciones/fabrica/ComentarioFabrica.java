package com.foro.publicaciones.fabrica;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.foro.publicaciones.model.Comentario;
import com.foro.publicaciones.model.EstadoPublicacion;
import com.foro.publicaciones.web.dto.ComentarioResponse;

@Component
public class ComentarioFabrica extends FabricaBase<Comentario, ComentarioDatos> {

	@Override
	protected Comentario nuevaInstancia() {
		return new Comentario();
	}

	@Override
	protected void completar(Comentario comentario, ComentarioDatos datos) {
		comentario.setContenido(datos.request().contenido().trim());
		comentario.setTema(datos.tema());
		comentario.setAutor(datos.autor());
		if (comentario.getFechaCreacion() == null) {
			comentario.setFechaCreacion(LocalDateTime.now());
		}
		if (comentario.getEstado() == null) {
			comentario.setEstado(EstadoPublicacion.PUBLICADO);
		}
	}

	public void actualizar(Comentario comentario, ComentarioDatos datos) {
		comentario.setContenido(datos.request().contenido().trim());
		comentario.setAutor(datos.autor());
	}

	public ComentarioResponse aRespuesta(Comentario comentario) {
		return new ComentarioResponse(
				comentario.getId(),
				comentario.getContenido(),
				comentario.getFechaCreacion(),
				comentario.getEstado().name(),
				comentario.getTema().getId(),
				comentario.getAutor().getId(),
				comentario.getAutor().getNombre());
	}

}
