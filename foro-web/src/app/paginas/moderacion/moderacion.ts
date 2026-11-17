import { Component, inject } from '@angular/core';

import { ForoDatos } from '../../datos/foro-datos';

@Component({
  selector: 'app-moderacion',
  imports: [],
  templateUrl: './moderacion.html',
})
export class Moderacion {
  readonly foro = inject(ForoDatos);

  nombreAutor(id: number): string {
    return this.foro.usuario(id)?.nombre ?? 'Usuario';
  }

  banearTema(id: number): void {
    this.foro.banearTema(id);
  }

  banearComentario(id: number): void {
    this.foro.banearComentario(id);
  }
}
