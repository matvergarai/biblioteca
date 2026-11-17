package com.foro.publicaciones.fabrica;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.foro.publicaciones.model.EstadoPublicacion;
import com.foro.publicaciones.model.Tema;
import com.foro.publicaciones.web.dto.TemaResponse;

@Component
public class TemaFabrica extends FabricaBase<Tema, TemaDatos> {

	@Override
	protected Tema nuevaInstancia() {
		return new Tema();
	}

	@Override
	protected void completar(Tema tema, TemaDatos datos) {
		tema.setTitulo(datos.request().titulo().trim());
		tema.setContenido(datos.request().contenido().trim());
		tema.setCategoria(datos.categoria());
		tema.setAutor(datos.autor());
		if (tema.getFechaCreacion() == null) {
			tema.setFechaCreacion(LocalDateTime.now());
		}
		if (tema.getEstado() == null) {
			tema.setEstado(EstadoPublicacion.PUBLICADO);
		}
	}

	public void actualizar(Tema tema, TemaDatos datos) {
		tema.setTitulo(datos.request().titulo().trim());
		tema.setContenido(datos.request().contenido().trim());
		tema.setCategoria(datos.categoria());
		tema.setAutor(datos.autor());
	}

	public TemaResponse aRespuesta(Tema tema) {
		return new TemaResponse(
				tema.getId(),
				tema.getTitulo(),
				tema.getContenido(),
				tema.getFechaCreacion(),
				tema.getEstado().name(),
				tema.getCategoria().getId(),
				tema.getCategoria().getNombre(),
				tema.getAutor().getId(),
				tema.getAutor().getNombre());
	}

}
