import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';

import { ForoDatos } from '../../datos/foro-datos';

@Component({
  selector: 'app-inicio',
  imports: [RouterLink],
  templateUrl: './inicio.html',
})
export class Inicio {
  readonly foro = inject(ForoDatos);

  cantidad(categoriaId: number): number {
    return this.foro.temasPublicados('', categoriaId).length;
  }
}
