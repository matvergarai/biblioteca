package com.foro.moderacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "CATEGORIAS")
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria_seq")
	@SequenceGenerator(name = "categoria_seq", sequenceName = "CATEGORIA_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOMBRE", nullable = false, unique = true, length = 80)
	private String nombre;

	@Column(name = "DESCRIPCION", nullable = false, length = 250)
	private String descripcion;

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

}
