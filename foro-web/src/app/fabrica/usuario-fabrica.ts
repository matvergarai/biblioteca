import { Injectable } from '@angular/core';

import { Usuario } from '../modelos/foro.model';
import { Fabrica } from './fabrica';

export interface UsuarioDatos {
  id: number;
  nombre: string;
  correo: string;
  clave: string;
}

@Injectable({ providedIn: 'root' })
export class UsuarioFabrica extends Fabrica<Usuario, UsuarioDatos> {
  protected override nuevaInstancia(): Usuario {
    return { id: 0, nombre: '', correo: '', clave: '', rol: 'USUARIO' };
  }

  protected override completar(usuario: Usuario, datos: UsuarioDatos): void {
    usuario.id = datos.id;
    usuario.nombre = datos.nombre.trim();
    usuario.correo = datos.correo.trim().toLowerCase();
    usuario.clave = datos.clave;
    usuario.rol = 'USUARIO';
  }
}
