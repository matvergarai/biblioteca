package com.foro.publicaciones.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.foro.publicaciones.fabrica.CategoriaDatos;
import com.foro.publicaciones.fabrica.CategoriaFabrica;
import com.foro.publicaciones.repository.CategoriaRepository;

@Configuration
public class DatosIniciales {

	@Bean
	ApplicationRunner categoriasIniciales(CategoriaRepository categoriaRepository, CategoriaFabrica categoriaFabrica) {
		return args -> {
			if (categoriaRepository.count() > 0) {
				return;
			}
			guardar(categoriaRepository, categoriaFabrica, "Programacion", "Codigo, lenguajes y buenas practicas");
			guardar(categoriaRepository, categoriaFabrica, "Bases de datos", "Modelado, SQL y Oracle");
			guardar(categoriaRepository, categoriaFabrica, "Redes", "Protocolos, infraestructura y conectividad");
			guardar(categoriaRepository, categoriaFabrica, "Seguridad informatica", "Proteccion de datos y accesos");
			guardar(categoriaRepository, categoriaFabrica, "Desarrollo web", "APIs, frontend y arquitectura web");
		};
	}

	private void guardar(
			CategoriaRepository categoriaRepository,
			CategoriaFabrica categoriaFabrica,
			String nombre,
			String descripcion) {
		categoriaRepository.save(categoriaFabrica.crear(new CategoriaDatos(nombre, descripcion)));
	}

}
