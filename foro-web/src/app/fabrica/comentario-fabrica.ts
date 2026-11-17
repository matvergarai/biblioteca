import { Injectable } from '@angular/core';

import { Comentario } from '../modelos/foro.model';
import { Fabrica } from './fabrica';

export interface ComentarioDatos {
  id: number;
  contenido: string;
  temaId: number;
  autorId: number;
}

@Injectable({ providedIn: 'root' })
export class ComentarioFabrica extends Fabrica<Comentario, ComentarioDatos> {
  protected override nuevaInstancia(): Comentario {
    return {
      id: 0,
      contenido: '',
      fechaCreacion: '',
      estado: 'PUBLICADO',
      temaId: 0,
      autorId: 0,
    };
  }

  protected override completar(comentario: Comentario, datos: ComentarioDatos): void {
    comentario.id = datos.id;
    comentario.contenido = datos.contenido.trim();
    comentario.temaId = datos.temaId;
    comentario.autorId = datos.autorId;
    comentario.fechaCreacion = new Date().toISOString();
    comentario.estado = 'PUBLICADO';
  }
}
