import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { Libro } from '../../modelos/libro.model';
import { LibroApi } from '../../servicios/libro-api';
import { mensajeError } from '../../servicios/mensaje-error';

@Component({
  selector: 'app-detalle-libro',
  imports: [RouterLink],
  templateUrl: './detalle-libro.component.html',
  styleUrl: './detalle-libro.component.css',
})
export class DetalleLibroComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly librosApi = inject(LibroApi);
  private readonly cambios = inject(ChangeDetectorRef);

  libro: Libro | null = null;
  cargando = false;
  eliminando = false;
  error = '';
  confirmar = false;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.cargando = true;
    this.librosApi.obtenerPorId(id).subscribe({
      next: (libro) => {
        this.libro = libro;
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

  eliminar(): void {
    if (!this.libro) {
      return;
    }
    this.eliminando = true;
    this.error = '';
    this.librosApi.eliminar(this.libro.id).subscribe({
      next: () => {
        this.router.navigate(['/libros']);
      },
      error: (err: unknown) => {
        this.error = mensajeError(err);
        this.eliminando = false;
        this.confirmar = false;
        this.cambios.detectChanges();
      },
    });
  }
}
