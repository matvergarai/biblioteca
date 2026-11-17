export abstract class Fabrica<T, R> {
  crear(datos: R): T {
    const entidad = this.nuevaInstancia();
    this.completar(entidad, datos);
    return entidad;
  }

  protected abstract nuevaInstancia(): T;

  protected abstract completar(entidad: T, datos: R): void;
}
