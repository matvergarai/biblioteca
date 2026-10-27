package com.foro.mapper;

import org.springframework.stereotype.Component;

import com.foro.model.Comentario;
import com.foro.model.Tema;
import com.foro.web.dto.ComentarioResponse;
import com.foro.web.dto.TemaResponse;

@Component
public class ForoMapper {

	public TemaResponse toResponse(Tema tema) {
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

	public ComentarioResponse toResponse(Comentario comentario) {
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
