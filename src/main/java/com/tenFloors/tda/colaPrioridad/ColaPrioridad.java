package main.java.com.tenFloors.tda.colaPrioridad;

import java.util.NoSuchElementException;

/**
 * Implementación nativa de una Cola con Prioridad estructurada como lista enlazada ordenada.
 * Diseñada para la gestión de misiones activas y tickets de soporte de Ten Floors.
 * En cada insercion se analiza la prioridad para ubicar el elemento en la posicion correcta.
 * @param <T> Tipo de elemento almacenado (ej. Mision, Ticket).
 */
public class ColaPrioridad<T> {

    // Nodo interno que almacena el elemento y su nivel de prioridad explícito
    private static class Nodo<T> {
        private final T elemento;
        private final int prioridad;
        private Nodo<T> siguiente;

        public Nodo(T elemento, int prioridad) {
            this.elemento = elemento;
            this.prioridad = prioridad;
            this.siguiente = null;
        }
    }

    private Nodo<T> primero;
    private int tamanio;

    public ColaPrioridad() {
        this.primero = null;
        this.tamanio = 0;
    }

    /**
     * Inserta un elemento en la cola analizando su prioridad para ubicarlo de forma ordenada.
     * A mayor valor numerico de prioridad, mas cerca del frente quedara el elemento.
     */
    public void insertar(T elemento, int prioridad) {
        if (elemento == null) {
            throw new IllegalArgumentException("No se pueden insertar elementos nulos.");
        }

        Nodo<T> nuevoNodo = new Nodo<>(elemento, prioridad);

        // Caso 1: La cola está vacía o el nuevo elemento tiene una prioridad estrictamente mayor que el primero
        if (estaVacio() || prioridad > primero.prioridad) {
            nuevoNodo.siguiente = primero;
            primero = nuevoNodo;
        } else {
            // Caso 2: Recorrer la lista para encontrar la posicion de insercion adecuada (Orden Descendente)
            Nodo<T> actual = primero;
            while (actual.siguiente != null && actual.siguiente.prioridad >= prioridad) {
                actual = actual.siguiente;
            }
            nuevoNodo.siguiente = actual.siguiente;
            actual.siguiente = nuevoNodo;
        }

        this.tamanio++;
    }

    /**
     * Extrae y retorna el elemento con mayor prioridad (ubicado al frente de la cola).
     */
    public T extraerMaximo() {
        if (estaVacio()) {
            throw new NoSuchElementException("La cola de prioridad de misiones/tickets esta vacia.");
        }

        T elementoRetornado = this.primero.elemento;
        this.primero = this.primero.siguiente;
        this.tamanio--;

        return elementoRetornado;
    }

    /**
     * Determina si la cola de prioridad está vacia.
     */
    public boolean estaVacio() {
        return this.primero == null;
    }

    /**
     * Retorna la cantidad actual de elementos en la cola de prioridad.
     */
    public int getTamanio() {
        return this.tamanio;
    }
}