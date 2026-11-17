package com.foro.publicaciones.fabrica;

import com.foro.publicaciones.model.Tema;
import com.foro.publicaciones.model.Usuario;
import com.foro.publicaciones.web.dto.ComentarioRequest;

public record ComentarioDatos(ComentarioRequest request, Tema tema, Usuario autor) {
}
