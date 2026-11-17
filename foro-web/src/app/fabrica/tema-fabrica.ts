import { Injectable } from '@angular/core';

import { Tema } from '../modelos/foro.model';
import { Fabrica } from './fabrica';

export interface TemaDatos {
  id: number;
  titulo: string;
  contenido: string;
  categoriaId: number;
  autorId: number;
}

@Injectable({ providedIn: 'root' })
export class TemaFabrica extends Fabrica<Tema, TemaDatos> {
  protected override nuevaInstancia(): Tema {
    return {
      id: 0,
      titulo: '',
      contenido: '',
      fechaCreacion: '',
      estado: 'PUBLICADO',
      categoriaId: 0,
      autorId: 0,
    };
  }

  protected override completar(tema: Tema, datos: TemaDatos): void {
    tema.id = datos.id;
    tema.titulo = datos.titulo.trim();
    tema.contenido = datos.contenido.trim();
    tema.categoriaId = datos.categoriaId;
    tema.autorId = datos.autorId;
    tema.fechaCreacion = new Date().toISOString();
    tema.estado = 'PUBLICADO';
  }
}
