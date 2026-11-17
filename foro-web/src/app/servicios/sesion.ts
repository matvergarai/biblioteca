import { Injectable, computed, signal } from '@angular/core';

import { ForoDatos } from '../datos/foro-datos';
import { Usuario } from '../modelos/foro.model';

@Injectable({ providedIn: 'root' })
export class Sesion {
  private readonly usuarioId = signal<number | null>(null);

  readonly actual = computed(() => {
    const id = this.usuarioId();
    return id === null ? null : (this.foro.usuario(id) ?? null);
  });

  readonly esAdministrador = computed(() => this.actual()?.rol === 'ADMINISTRADOR');

  constructor(private readonly foro: ForoDatos) {}

  ingresar(correo: string, clave: string): Usuario | null {
    const usuario = this.foro.usuarioPorCorreo(correo);
    if (!usuario || usuario.clave !== clave) {
      return null;
    }
    this.usuarioId.set(usuario.id);
    return usuario;
  }

  registrar(nombre: string, correo: string, clave: string): string | null {
    if (this.foro.usuarioPorCorreo(correo)) {
      return 'Ya existe un usuario con ese correo.';
    }
    const usuario = this.foro.crearUsuario(nombre, correo, clave);
    this.usuarioId.set(usuario.id);
    return null;
  }

  recuperar(correo: string, clave: string): string | null {
    const usuario = this.foro.usuarioPorCorreo(correo);
    if (!usuario) {
      return 'No hay una cuenta con ese correo.';
    }
    this.foro.actualizarUsuario(usuario.id, usuario.nombre, usuario.correo, clave);
    return null;
  }

  actualizarPerfil(nombre: string, correo: string, clave?: string): string | null {
    const actual = this.actual();
    if (!actual) {
      return 'Debes iniciar sesion.';
    }
    const otro = this.foro.usuarioPorCorreo(correo);
    if (otro && otro.id !== actual.id) {
      return 'Ya existe un usuario con ese correo.';
    }
    this.foro.actualizarUsuario(actual.id, nombre, correo, clave);
    return null;
  }

  cerrar(): void {
    this.usuarioId.set(null);
  }
}
