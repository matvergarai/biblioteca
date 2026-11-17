import { Injectable, signal } from '@angular/core';

import { ComentarioFabrica } from '../fabrica/comentario-fabrica';
import { TemaFabrica } from '../fabrica/tema-fabrica';
import { UsuarioFabrica } from '../fabrica/usuario-fabrica';
import { Categoria, Comentario, Tema, Usuario } from '../modelos/foro.model';

@Injectable({ providedIn: 'root' })
export class ForoDatos {
  readonly categorias = signal<Categoria[]>([
    { id: 1, nombre: 'Programacion', descripcion: 'Codigo, lenguajes y buenas practicas' },
    { id: 2, nombre: 'Bases de datos', descripcion: 'Modelado, SQL y Oracle' },
    { id: 3, nombre: 'Redes', descripcion: 'Protocolos, infraestructura y conectividad' },
    { id: 4, nombre: 'Seguridad informatica', descripcion: 'Proteccion de datos y accesos' },
    { id: 5, nombre: 'Desarrollo web', descripcion: 'APIs, frontend y arquitectura web' },
  ]);

  readonly usuarios = signal<Usuario[]>([
    { id: 1, nombre: 'Administrador Nexo', correo: 'admin@nexo.cl', clave: 'Admin123!', rol: 'ADMINISTRADOR' },
    { id: 2, nombre: 'Ana Riquelme', correo: 'ana@nexo.cl', clave: 'Ana1234!', rol: 'USUARIO' },
    { id: 3, nombre: 'Leo Soto', correo: 'leo@nexo.cl', clave: 'Leo1234!', rol: 'USUARIO' },
  ]);

  readonly temas = signal<Tema[]>([
    {
      id: 1,
      titulo: 'Como ordenar un repositorio de microservicios',
      contenido: 'Cada servicio vive en su propia carpeta y comparte la misma base Oracle.',
      fechaCreacion: '2026-11-10T15:00:00.000Z',
      estado: 'PUBLICADO',
      categoriaId: 1,
      autorId: 2,
    },
    {
      id: 2,
      titulo: 'Wallet de Oracle Cloud en Spring Boot',
      contenido: 'La clave no va en el codigo: se lee desde la variable ORACLE_PASSWORD.',
      fechaCreacion: '2026-11-11T18:30:00.000Z',
      estado: 'PUBLICADO',
      categoriaId: 2,
      autorId: 3,
    },
    {
      id: 3,
      titulo: 'Grilla de 12 columnas en el front',
      contenido: 'Con Bootstrap la misma pantalla se acomoda en telefono, tablet y escritorio.',
      fechaCreacion: '2026-11-12T12:15:00.000Z',
      estado: 'PUBLICADO',
      categoriaId: 5,
      autorId: 2,
    },
  ]);

  readonly comentarios = signal<Comentario[]>([
    {
      id: 1,
      contenido: 'Me sirvio separar usuarios, publicaciones y moderacion.',
      fechaCreacion: '2026-11-10T16:10:00.000Z',
      estado: 'PUBLICADO',
      temaId: 1,
      autorId: 3,
    },
    {
      id: 2,
      contenido: 'El alias biblio_low alcanza para las pruebas.',
      fechaCreacion: '2026-11-11T19:00:00.000Z',
      estado: 'PUBLICADO',
      temaId: 2,
      autorId: 2,
    },
  ]);

  constructor(
    private readonly usuarioFabrica: UsuarioFabrica,
    private readonly temaFabrica: TemaFabrica,
    private readonly comentarioFabrica: ComentarioFabrica,
  ) {}

  categoria(id: number): Categoria | undefined {
    return this.categorias().find((categoria) => categoria.id === id);
  }

  usuario(id: number): Usuario | undefined {
    return this.usuarios().find((usuario) => usuario.id === id);
  }

  usuarioPorCorreo(correo: string): Usuario | undefined {
    const normalizado = correo.trim().toLowerCase();
    return this.usuarios().find((usuario) => usuario.correo === normalizado);
  }

  temasPublicados(texto = '', categoriaId?: number): Tema[] {
    const filtro = texto.trim().toLowerCase();
    return this.temas().filter((tema) => {
      if (tema.estado !== 'PUBLICADO') {
        return false;
      }
      if (categoriaId && tema.categoriaId !== categoriaId) {
        return false;
      }
      if (!filtro) {
        return true;
      }
      return tema.titulo.toLowerCase().includes(filtro) || tema.contenido.toLowerCase().includes(filtro);
    });
  }

  temaPublicado(id: number): Tema | undefined {
    return this.temas().find((tema) => tema.id === id && tema.estado === 'PUBLICADO');
  }

  comentariosPublicados(temaId: number): Comentario[] {
    return this.comentarios().filter((comentario) => comentario.temaId === temaId && comentario.estado === 'PUBLICADO');
  }

  crearUsuario(nombre: string, correo: string, clave: string): Usuario {
    const usuario = this.usuarioFabrica.crear({
      id: this.siguienteId(this.usuarios()),
      nombre,
      correo,
      clave,
    });
    this.usuarios.update((lista) => [...lista, usuario]);
    return usuario;
  }

  actualizarUsuario(id: number, nombre: string, correo: string, clave?: string): void {
    this.usuarios.update((lista) =>
      lista.map((usuario) =>
        usuario.id === id
          ? {
              ...usuario,
              nombre: nombre.trim(),
              correo: correo.trim().toLowerCase(),
              clave: clave && clave.length > 0 ? clave : usuario.clave,
            }
          : usuario,
      ),
    );
  }

  crearTema(titulo: string, contenido: string, categoriaId: number, autorId: number): Tema {
    const tema = this.temaFabrica.crear({
      id: this.siguienteId(this.temas()),
      titulo,
      contenido,
      categoriaId,
      autorId,
    });
    this.temas.update((lista) => [tema, ...lista]);
    return tema;
  }

  crearComentario(contenido: string, temaId: number, autorId: number): Comentario {
    const comentario = this.comentarioFabrica.crear({
      id: this.siguienteId(this.comentarios()),
      contenido,
      temaId,
      autorId,
    });
    this.comentarios.update((lista) => [...lista, comentario]);
    return comentario;
  }

  banearTema(id: number): void {
    this.temas.update((lista) => lista.map((tema) => (tema.id === id ? { ...tema, estado: 'BANEADO' } : tema)));
  }

  banearComentario(id: number): void {
    this.comentarios.update((lista) =>
      lista.map((comentario) => (comentario.id === id ? { ...comentario, estado: 'BANEADO' } : comentario)),
    );
  }

  private siguienteId(lista: { id: number }[]): number {
    return lista.reduce((maximo, item) => Math.max(maximo, item.id), 0) + 1;
  }
}
