package com.foro.publicaciones.fabrica;

import com.foro.publicaciones.model.Categoria;
import com.foro.publicaciones.model.Usuario;
import com.foro.publicaciones.web.dto.TemaRequest;

public record TemaDatos(TemaRequest request, Categoria categoria, Usuario autor) {
}
