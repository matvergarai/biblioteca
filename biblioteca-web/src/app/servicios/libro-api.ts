import { Observable } from 'rxjs';

import { Libro, LibroRequest } from '../modelos/libro.model';

/**
 * Contrato de la capa de servicio.
 * Las pantallas dependen de esta abstracción y no conocen HttpClient ni la URL.
 */
export abstract class LibroApi {
  abstract listar(): Observable<Libro[]>;

  abstract obtenerPorId(id: number): Observable<Libro>;

  abstract crear(libro: LibroRequest): Observable<Libro>;

  abstract actualizar(id: number, libro: LibroRequest): Observable<Libro>;

  abstract eliminar(id: number): Observable<void>;
}
