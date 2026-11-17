import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { Sesion } from '../../servicios/sesion';
import { claveSegura, clavesIguales } from '../../validacion/clave';

@Component({
  selector: 'app-recuperar',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './recuperar.html',
})
export class Recuperar {
  private readonly sesion = inject(Sesion);
  mensaje = '';
  listo = false;

  readonly form = inject(FormBuilder).nonNullable.group(
    {
      correo: ['', [Validators.required, Validators.email]],
      clave: ['', [Validators.required, claveSegura()]],
      confirmar: ['', Validators.required],
    },
    { validators: clavesIguales('clave', 'confirmar') },
  );

  enviar(): void {
    this.mensaje = '';
    this.listo = false;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { correo, clave } = this.form.getRawValue();
    const error = this.sesion.recuperar(correo, clave);
    if (error) {
      this.mensaje = error;
      return;
    }
    this.listo = true;
    this.form.reset();
  }
}
