package com.biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "LIBROS")
public class Libro {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "libro_seq")
	@SequenceGenerator(name = "libro_seq", sequenceName = "LIBRO_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "TITULO", nullable = false, length = 200)
	private String titulo;

	@Column(name = "AUTOR", nullable = false, length = 150)
	private String autor;

	@Column(name = "ANIO_PUBLICACION", nullable = false)
	private Integer anioPublicacion;

	@Column(name = "GENERO", nullable = false, length = 80)
	private String genero;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public Integer getAnioPublicacion() {
		return anioPublicacion;
	}

	public void setAnioPublicacion(Integer anioPublicacion) {
		this.anioPublicacion = anioPublicacion;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

}
