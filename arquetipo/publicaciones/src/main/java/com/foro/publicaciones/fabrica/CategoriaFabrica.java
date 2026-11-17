package com.foro.publicaciones.fabrica;

import org.springframework.stereotype.Component;

import com.foro.publicaciones.model.Categoria;
import com.foro.publicaciones.web.dto.CategoriaResponse;

@Component
public class CategoriaFabrica extends FabricaBase<Categoria, CategoriaDatos> {

	@Override
	protected Categoria nuevaInstancia() {
		return new Categoria();
	}

	@Override
	protected void completar(Categoria categoria, CategoriaDatos datos) {
		categoria.setNombre(datos.nombre().trim());
		categoria.setDescripcion(datos.descripcion().trim());
	}

	public CategoriaResponse aRespuesta(Categoria categoria) {
		return new CategoriaResponse(categoria.getId(), categoria.getNombre(), categoria.getDescripcion());
	}

}
