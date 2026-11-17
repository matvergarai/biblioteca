import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { ForoDatos } from '../../datos/foro-datos';
import { Sesion } from '../../servicios/sesion';

@Component({
  selector: 'app-publicar',
  imports: [ReactiveFormsModule],
  templateUrl: './publicar.html',
})
export class Publicar {
  readonly foro = inject(ForoDatos);
  private readonly sesion = inject(Sesion);
  private readonly router = inject(Router);

  readonly form = inject(FormBuilder).nonNullable.group({
    titulo: ['', [Validators.required, Validators.maxLength(200)]],
    contenido: ['', [Validators.required, Validators.maxLength(4000)]],
    categoriaId: ['', Validators.required],
  });

  enviar(): void {
    const autor = this.sesion.actual();
    if (!autor) {
      return;
    }
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { titulo, contenido, categoriaId } = this.form.getRawValue();
    const tema = this.foro.crearTema(titulo, contenido, Number(categoriaId), autor.id);
    void this.router.navigate(['/temas', tema.id]);
  }
}
