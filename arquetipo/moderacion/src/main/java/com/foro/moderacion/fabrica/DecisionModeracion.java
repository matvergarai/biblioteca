package com.foro.moderacion.fabrica;

import com.foro.moderacion.model.EstadoPublicacion;

public class DecisionModeracion {

	private EstadoPublicacion estado;
	private Long administradorId;

	public EstadoPublicacion getEstado() {
		return estado;
	}

	public void setEstado(EstadoPublicacion estado) {
		this.estado = estado;
	}

	public Long getAdministradorId() {
		return administradorId;
	}

	public void setAdministradorId(Long administradorId) {
		this.administradorId = administradorId;
	}

}
