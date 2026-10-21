package com.biblioteca.model;

import java.time.LocalDate;

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
@Table(name = "PRESTAMOS")
public class Prestamo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prestamo_seq")
	@SequenceGenerator(name = "prestamo_seq", sequenceName = "PRESTAMO_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "LIBRO_ID", nullable = false)
	private Libro libro;

	@Column(name = "PRESTATARIO", nullable = false, length = 150)
	private String prestatario;

	@Column(name = "FECHA_PRESTAMO", nullable = false)
	private LocalDate fechaPrestamo;

	@Column(name = "FECHA_DEVOLUCION")
	private LocalDate fechaDevolucion;

	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO", nullable = false, length = 20)
	private EstadoPrestamo estado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	public String getPrestatario() {
		return prestatario;
	}

	public void setPrestatario(String prestatario) {
		this.prestatario = prestatario;
	}

	public LocalDate getFechaPrestamo() {
		return fechaPrestamo;
	}

	public void setFechaPrestamo(LocalDate fechaPrestamo) {
		this.fechaPrestamo = fechaPrestamo;
	}

	public LocalDate getFechaDevolucion() {
		return fechaDevolucion;
	}

	public void setFechaDevolucion(LocalDate fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}

	public EstadoPrestamo getEstado() {
		return estado;
	}

	public void setEstado(EstadoPrestamo estado) {
		this.estado = estado;
	}

}
