export type Rol = 'USUARIO' | 'ADMINISTRADOR';
export type EstadoPublicacion = 'PUBLICADO' | 'BANEADO';

export interface Usuario {
  id: number;
  nombre: string;
  correo: string;
  clave: string;
  rol: Rol;
}

export interface Categoria {
  id: number;
  nombre: string;
  descripcion: string;
}

export interface Tema {
  id: number;
  titulo: string;
  contenido: string;
  fechaCreacion: string;
  estado: EstadoPublicacion;
  categoriaId: number;
  autorId: number;
}

export interface Comentario {
  id: number;
  contenido: string;
  fechaCreacion: string;
  estado: EstadoPublicacion;
  temaId: number;
  autorId: number;
}
