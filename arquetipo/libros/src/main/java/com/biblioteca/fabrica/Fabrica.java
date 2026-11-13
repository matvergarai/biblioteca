package com.biblioteca.fabrica;

/**
 * Patron Factory: el cliente pide un objeto y no conoce los pasos
 * con los que se construye.
 */
public interface Fabrica<T, R> {

	T crear(R datos);

}
