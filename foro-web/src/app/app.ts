import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

import { Sesion } from './servicios/sesion';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  readonly sesion = inject(Sesion);
  readonly menuAbierto = signal(false);
  private readonly router = inject(Router);

  constructor() {
    this.router.events.subscribe(() => this.menuAbierto.set(false));
  }

  alternarMenu(): void {
    this.menuAbierto.update((abierto) => !abierto);
  }

  cerrar(): void {
    this.sesion.cerrar();
    void this.router.navigateByUrl('/');
  }
}
