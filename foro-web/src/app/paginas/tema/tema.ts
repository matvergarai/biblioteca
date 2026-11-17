import { DatePipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { map } from 'rxjs';

import { ForoDatos } from '../../datos/foro-datos';
import { Sesion } from '../../servicios/sesion';

@Component({
  selector: 'app-tema',
  imports: [ReactiveFormsModule, RouterLink, DatePipe],
  templateUrl: './tema.html',
})
export class TemaDetalle {
  readonly foro = inject(ForoDatos);
  readonly sesion = inject(Sesion);
  private readonly temaId = toSignal(
    inject(ActivatedRoute).paramMap.pipe(map((params) => Number(params.get('id')))),
    { initialValue: 0 },
  );
  mensaje = '';

  readonly form = inject(FormBuilder).nonNullable.group({
    contenido: ['', [Validators.required, Validators.maxLength(2000)]],
  });

  tema() {
    return this.foro.temaPublicado(this.temaId());
  }

  comentarios() {
    const tema = this.tema();
    return tema ? this.foro.comentariosPublicados(tema.id) : [];
  }

  nombreAutor(id: number): string {
    return this.foro.usuario(id)?.nombre ?? 'Usuario';
  }

  nombreCategoria(id: number): string {
    return this.foro.categoria(id)?.nombre ?? 'General';
  }

  comentar(): void {
    this.mensaje = '';
    const tema = this.tema();
    const autor = this.sesion.actual();
    if (!tema || !autor) {
      return;
    }
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.foro.crearComentario(this.form.controls.contenido.getRawValue(), tema.id, autor.id);
    this.form.reset();
    this.mensaje = 'Comentario publicado.';
  }
}
