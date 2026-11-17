package com.foro.publicaciones.web.dto;

import java.util.List;

public record TemaDetalleResponse(TemaResponse tema, List<ComentarioResponse> comentarios) {
}
