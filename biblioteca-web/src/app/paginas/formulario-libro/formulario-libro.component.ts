import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { LibroRequest } from '../../modelos/libro.model';
import { LibroApi } from '../../servicios/libro-api';
import { mensajeError } from '../../servicios/mensaje-error';

@Component({
  selector: 'app-formulario-libro',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './formulario-libro.component.html',
  styleUrl: './formulario-libro.component.css',
})
export class FormularioLibroComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly librosApi = inject(LibroApi);
  private readonly fb = inject(FormBuilder);
  private readonly cambios = inject(ChangeDetectorRef);

  id: number | null = null;
  cargando = false;
  guardando = false;
  cargaFallida = false;
  error = '';

  readonly formulario = this.fb.nonNullable.group({
    titulo: ['', [Validators.required, Validators.maxLength(200)]],
    autor: ['', [Validators.required, Validators.maxLength(150)]],
    anioPublicacion: [
      new Date().getFullYear(),
      [Validators.required, Validators.min(1), Validators.max(2100)],
    ],
    genero: ['', [Validators.required, Validators.maxLength(80)]],
  });

  get editando(): boolean {
    return this.id !== null;
  }

  ngOnInit(): void {
    const raw = this.route.snapshot.paramMap.get('id');
    if (raw === null) {
      return;
    }
    this.id = Number(raw);
    this.cargando = true;
    this.librosApi.obtenerPorId(this.id).subscribe({
      next: (libro) => {
        this.formulario.setValue({
          titulo: libro.titulo,
          autor: libro.autor,
          anioPublicacion: libro.anioPublicacion,
          genero: libro.genero,
        });
        this.cargando = false;
        this.cambios.detectChanges();
      },
      error: (err: unknown) => {
        this.error = mensajeError(err);
        this.cargaFallida = true;
        this.cargando = false;
        this.cambios.detectChanges();
      },
    });
  }

  guardar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const valor = this.formulario.getRawValue();
    const cuerpo: LibroRequest = {
      titulo: valor.titulo.trim(),
      autor: valor.autor.trim(),
      anioPublicacion: Number(valor.anioPublicacion),
      genero: valor.genero.trim(),
    };

    this.guardando = true;
    this.error = '';
    const peticion =
      this.id === null ? this.librosApi.crear(cuerpo) : this.librosApi.actualizar(this.id, cuerpo);

    peticion.subscribe({
      next: (libro) => {
        this.router.navigate(['/libros', libro.id]);
      },
      error: (err: unknown) => {
        this.error = mensajeError(err);
        this.guardando = false;
        this.cambios.detectChanges();
      },
    });
  }

  invalido(campo: 'titulo' | 'autor' | 'anioPublicacion' | 'genero'): boolean {
    const control = this.formulario.controls[campo];
    return control.invalid && control.touched;
  }

  mensajeCampo(campo: 'titulo' | 'autor' | 'anioPublicacion' | 'genero'): string {
    const control = this.formulario.controls[campo];
    if (control.hasError('required')) {
      return 'Este campo es obligatorio.';
    }
    if (control.hasError('maxlength')) {
      const max = control.getError('maxlength').requiredLength as number;
      return `No puede superar ${max} caracteres.`;
    }
    if (control.hasError('min') || control.hasError('max')) {
      return 'El año debe estar entre 1 y 2100.';
    }
    return '';
  }
}
