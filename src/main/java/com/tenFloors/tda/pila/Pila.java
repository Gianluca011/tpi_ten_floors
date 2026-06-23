package main.java.com.tenFloors.tda.pila;

/**
 * TDA Pila Genérica con comportamiento LIFO.
 */
public class Pila<T> {

    private Nodo<T> tope;
    private int tamanio;

    // Nodo interno anidado para el manejo de memoria de forma dinamica
    private static class Nodo<T> {
        final T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    public Pila() {
        this.tope = null;
        this.tamanio = 0;
    }

    /**
     * Inserta un elemento en el tope de la pila.
     */
    public void apilar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        nuevoNodo.siguiente = tope;
        tope = nuevoNodo;
        tamanio++;
    }

    /**
     * Remueve y devuelve el elemento en la tope de la pila.
     */
    public T desapilar() {
        if (estaVacia()) {
            throw new IllegalStateException("[ERROR] La pila de navegacion esta vacia.");
        }
        T dato = tope.dato;
        tope = tope.siguiente;
        tamanio--;
        return dato;
    }

    /**
     * Devuelve el elemento en la tope sin removerlo.
     */
    public T verTope() {
        if (estaVacia()) {
            throw new IllegalStateException("[ERROR] No hay elementos en el tope.");
        }
        return tope.dato;
    }

    /**
     * Verifica si la pila no contiene elementos.
     */
    public boolean estaVacia() {
        return tope == null;
    }

    /**
     * Devuelve la cantidad actual de elementos en la pila.
     */
    public int getTamanio() {
        return tamanio;
    }
}