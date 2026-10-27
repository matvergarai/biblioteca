package com.foro.web.dto;

import java.util.List;

public record TemaDetalleResponse(
		TemaResponse tema,
		List<ComentarioResponse> comentarios) {
}
