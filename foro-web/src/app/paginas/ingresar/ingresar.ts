import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { Sesion } from '../../servicios/sesion';

@Component({
  selector: 'app-ingresar',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './ingresar.html',
})
export class Ingresar {
  private readonly sesion = inject(Sesion);
  private readonly router = inject(Router);
  mensaje = '';

  readonly form = inject(FormBuilder).nonNullable.group({
    correo: ['', [Validators.required, Validators.email]],
    clave: ['', Validators.required],
  });

  enviar(): void {
    this.mensaje = '';
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { correo, clave } = this.form.getRawValue();
    if (!this.sesion.ingresar(correo, clave)) {
      this.mensaje = 'Correo o clave incorrectos.';
      return;
    }
    void this.router.navigateByUrl('/');
  }
}
