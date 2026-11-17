package com.foro.moderacion.fabrica;

import org.springframework.stereotype.Component;

import com.foro.moderacion.model.Comentario;
import com.foro.moderacion.model.EstadoPublicacion;
import com.foro.moderacion.model.Tema;

@Component
public class SancionFabrica extends FabricaBase<DecisionModeracion, SolicitudBan> {

	@Override
	protected DecisionModeracion nuevaInstancia() {
		return new DecisionModeracion();
	}

	@Override
	protected void completar(DecisionModeracion decision, SolicitudBan datos) {
		decision.setEstado(EstadoPublicacion.BANEADO);
		decision.setAdministradorId(datos.administradorId());
	}

	public void aplicar(Tema tema, DecisionModeracion decision) {
		tema.setEstado(decision.getEstado());
	}

	public void aplicar(Comentario comentario, DecisionModeracion decision) {
		comentario.setEstado(decision.getEstado());
	}

}
