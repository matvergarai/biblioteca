#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.fabrica;

/**
 * Factory Method. El algoritmo de creacion queda fijo y cada
 * subclase decide que instancia crear y como completarla.
 */
public abstract class FabricaBase<T, R> implements Fabrica<T, R> {

	@Override
	public final T crear(R datos) {
		T entidad = nuevaInstancia();
		completar(entidad, datos);
		return entidad;
	}

	protected abstract T nuevaInstancia();

	protected abstract void completar(T entidad, R datos);

}
