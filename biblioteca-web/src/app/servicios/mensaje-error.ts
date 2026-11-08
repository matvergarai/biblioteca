import { HttpErrorResponse } from '@angular/common/http';
import { TimeoutError } from 'rxjs';

const SIN_CONEXION =
  'No se pudo conectar con el microservicio en http://localhost:8080. Comprueba que esté en ejecución.';

export function mensajeError(err: unknown): string {
  if (err instanceof TimeoutError) {
    return SIN_CONEXION;
  }

  if (!(err instanceof HttpErrorResponse)) {
    return 'Ocurrió un error inesperado.';
  }

  if (err.status === 0) {
    return SIN_CONEXION;
  }

  const cuerpo = typeof err.error === 'string' ? parsear(err.error) : err.error;
  if (cuerpo && typeof cuerpo === 'object') {
    const mensaje = 'mensaje' in cuerpo && cuerpo.mensaje ? String(cuerpo.mensaje) : '';
    const campos =
      'campos' in cuerpo && cuerpo.campos && typeof cuerpo.campos === 'object'
        ? Object.values(cuerpo.campos as Record<string, string>).join(' ')
        : '';
    const texto = [mensaje, campos].filter(Boolean).join(' ');
    if (texto) {
      return texto;
    }
  }

  return `El microservicio respondió con el estado ${err.status}.`;
}

function parsear(texto: string): unknown {
  try {
    return JSON.parse(texto);
  } catch {
    return null;
  }
}
