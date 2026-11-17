import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { Sesion } from '../../servicios/sesion';
import { claveSegura, clavesIguales } from '../../validacion/clave';

@Component({
  selector: 'app-perfil',
  imports: [ReactiveFormsModule],
  templateUrl: './perfil.html',
})
export class Perfil {
  readonly sesion = inject(Sesion);
  mensaje = '';
  listo = false;

  readonly form = inject(FormBuilder).nonNullable.group(
    {
      nombre: [this.sesion.actual()?.nombre ?? '', [Validators.required, Validators.maxLength(120)]],
      correo: [this.sesion.actual()?.correo ?? '', [Validators.required, Validators.email, Validators.maxLength(150)]],
      clave: ['', claveSegura()],
      confirmar: [''],
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
    const { nombre, correo, clave } = this.form.getRawValue();
    const error = this.sesion.actualizarPerfil(nombre, correo, clave);
    if (error) {
      this.mensaje = error;
      return;
    }
    this.listo = true;
    this.form.controls.clave.reset('');
    this.form.controls.confirmar.reset('');
  }
}
