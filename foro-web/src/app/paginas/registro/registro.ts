import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { Sesion } from '../../servicios/sesion';
import { claveSegura, clavesIguales } from '../../validacion/clave';

@Component({
  selector: 'app-registro',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './registro.html',
})
export class Registro {
  private readonly sesion = inject(Sesion);
  private readonly router = inject(Router);
  mensaje = '';

  readonly form = inject(FormBuilder).nonNullable.group(
    {
      nombre: ['', [Validators.required, Validators.maxLength(120)]],
      correo: ['', [Validators.required, Validators.email, Validators.maxLength(150)]],
      clave: ['', [Validators.required, claveSegura()]],
      confirmar: ['', Validators.required],
    },
    { validators: clavesIguales('clave', 'confirmar') },
  );

  enviar(): void {
    this.mensaje = '';
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { nombre, correo, clave } = this.form.getRawValue();
    const error = this.sesion.registrar(nombre, correo, clave);
    if (error) {
      this.mensaje = error;
      return;
    }
    void this.router.navigateByUrl('/');
  }
}
