package com.foro.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.foro.model.Categoria;
import com.foro.repository.CategoriaRepository;

@Configuration
public class DatosIniciales {

	@Bean
	ApplicationRunner categoriasIniciales(CategoriaRepository categoriaRepository) {
		return args -> {
			if (categoriaRepository.count() > 0) {
				return;
			}
			guardar(categoriaRepository, "Programacion", "Codigo, lenguajes y buenas practicas");
			guardar(categoriaRepository, "Bases de datos", "Modelado, SQL y Oracle");
			guardar(categoriaRepository, "Redes", "Protocolos, infraestructura y conectividad");
			guardar(categoriaRepository, "Seguridad informatica", "Proteccion de datos y accesos");
			guardar(categoriaRepository, "Desarrollo web", "APIs, frontend y arquitectura web");
		};
	}

	private void guardar(CategoriaRepository categoriaRepository, String nombre, String descripcion) {
		Categoria categoria = new Categoria();
		categoria.setNombre(nombre);
		categoria.setDescripcion(descripcion);
		categoriaRepository.save(categoria);
	}

}
