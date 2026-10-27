package com.foro.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "TEMAS")
public class Tema {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tema_seq")
	@SequenceGenerator(name = "tema_seq", sequenceName = "TEMA_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "TITULO", nullable = false, length = 200)
	private String titulo;

	@Column(name = "CONTENIDO", nullable = false, length = 4000)
	private String contenido;

	@Column(name = "FECHA_CREACION", nullable = false)
	private LocalDateTime fechaCreacion;

	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO", nullable = false, length = 20)
	private EstadoPublicacion estado;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CATEGORIA_ID", nullable = false)
	private Categoria categoria;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "AUTOR_ID", nullable = false)
	private Usuario autor;

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

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public EstadoPublicacion getEstado() {
		return estado;
	}

	public void setEstado(EstadoPublicacion estado) {
		this.estado = estado;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Usuario getAutor() {
		return autor;
	}

	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

}
