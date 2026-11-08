import { HttpClient } from '@angular/common/http';
import { Injectable, NgZone, inject } from '@angular/core';
import { Observable, map, timeout } from 'rxjs';

import { Libro, LibroRequest } from '../modelos/libro.model';
import { LibroApi } from './libro-api';

/** URL de ejecución del microservicio de libros. */
export const LIBROS_API_URL = 'http://localhost:8080/libros';

/**
 * Único punto que llama al CRUD del microservicio.
 * Cada operación usa la URL real de ejecución.
 */
@Injectable()
export class LibroHttpService extends LibroApi {
  private readonly http = inject(HttpClient);
  private readonly zone = inject(NgZone);

  listar(): Observable<Libro[]> {
    return this.entregar(this.http.get<Libro[]>(LIBROS_API_URL));
  }

  obtenerPorId(id: number): Observable<Libro> {
    return this.entregar(this.http.get<Libro>(`${LIBROS_API_URL}/${id}`));
  }

  crear(libro: LibroRequest): Observable<Libro> {
    return this.entregar(this.http.post<Libro>(LIBROS_API_URL, libro));
  }

  actualizar(id: number, libro: LibroRequest): Observable<Libro> {
    return this.entregar(this.http.put<Libro>(`${LIBROS_API_URL}/${id}`, libro));
  }

  eliminar(id: number): Observable<void> {
    return this.entregar(
      this.http.delete(`${LIBROS_API_URL}/${id}`, { responseType: 'text' }).pipe(map(() => undefined)),
    );
  }

  private entregar<T>(peticion: Observable<T>): Observable<T> {
    return new Observable((observador) => {
      const suscripcion = peticion.pipe(timeout(8000)).subscribe({
        next: (valor) => this.zone.run(() => observador.next(valor)),
        error: (err: unknown) => this.zone.run(() => observador.error(err)),
        complete: () => this.zone.run(() => observador.complete()),
      });
      return () => suscripcion.unsubscribe();
    });
  }
}
