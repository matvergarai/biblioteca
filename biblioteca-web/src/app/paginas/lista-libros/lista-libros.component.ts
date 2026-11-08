import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Libro } from '../../modelos/libro.model';
import { LibroApi } from '../../servicios/libro-api';
import { mensajeError } from '../../servicios/mensaje-error';

@Component({
  selector: 'app-lista-libros',
  imports: [RouterLink],
  templateUrl: './lista-libros.component.html',
  styleUrl: './lista-libros.component.css',
})
export class ListaLibrosComponent implements OnInit {
  private readonly librosApi = inject(LibroApi);
  private readonly cambios = inject(ChangeDetectorRef);

  libros: Libro[] = [];
  cargando = false;
  error = '';
  pendiente: Libro | null = null;
  eliminando = false;

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    this.librosApi.listar().subscribe({
      next: (libros) => {
        this.libros = libros;
        this.cargando = false;
        this.cambios.detectChanges();
      },
      error: (err: unknown) => {
        this.error = mensajeError(err);
        this.cargando = false;
        this.cambios.detectChanges();
      },
    });
  }

  pedirEliminar(libro: Libro): void {
    this.pendiente = libro;
    this.error = '';
  }

  cancelarEliminar(): void {
    this.pendiente = null;
  }

  confirmarEliminar(): void {
    if (!this.pendiente) {
      return;
    }
    const id = this.pendiente.id;
    this.eliminando = true;
    this.librosApi.eliminar(id).subscribe({
      next: () => {
        this.libros = this.libros.filter((libro) => libro.id !== id);
        this.pendiente = null;
        this.eliminando = false;
        this.cambios.detectChanges();
      },
      error: (err: unknown) => {
        this.error = mensajeError(err);
        this.eliminando = false;
        this.cambios.detectChanges();
      },
    });
  }
}
