import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { ForoDatos } from '../../datos/foro-datos';

@Component({
  selector: 'app-temas',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './temas.html',
})
export class Temas {
  readonly foro = inject(ForoDatos);
  private readonly route = inject(ActivatedRoute);

  readonly form = inject(FormBuilder).nonNullable.group({
    texto: [''],
    categoriaId: [this.route.snapshot.queryParamMap.get('categoriaId') ?? ''],
  });

  resultados() {
    const { texto, categoriaId } = this.form.getRawValue();
    const id = categoriaId ? Number(categoriaId) : undefined;
    return this.foro.temasPublicados(texto, id);
  }

  nombreCategoria(id: number): string {
    return this.foro.categoria(id)?.nombre ?? 'General';
  }

  nombreAutor(id: number): string {
    return this.foro.usuario(id)?.nombre ?? 'Usuario';
  }
}
