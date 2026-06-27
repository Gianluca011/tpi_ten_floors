package main.java.com.tenFloors.tda.cola;

import java.util.NoSuchElementException;

/**
 * Implementación nativa de una Estructura de Datos FIFO (First In, First Out).
 * Diseñada para la gestión de salas de espera de mazmorras de forma síncrona.
 * * @param <T> Tipo de elemento almacenado en la cola (ej. Jugador).
 */
public class Cola<T> {

    // Nodo interno para la estructura enlazada
    private static class Nodo<T> {
        private final T elemento;
        private Nodo<T> siguiente;

        public Nodo(T elemento) {
            this.elemento = elemento;
            this.siguiente = null;
        }
    }

    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int tamanio;

    public Cola() {
        this.primero = null;
        this.ultimo = null;
        this.tamanio = 0;
    }

    /**
     * Encola un elemento al final de la sala de espera.
     */
    public void encolar(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("No se pueden encolar elementos nulos.");
        }

        Nodo<T> nuevoNodo = new Nodo<>(elemento);

        if (estaVacia()) {
            this.primero = nuevoNodo;
        } else {
            this.ultimo.siguiente = nuevoNodo;
        }

        this.ultimo = nuevoNodo;
        this.tamanio++;
    }

    /**
     * Desencola y retorna el elemento al frente de la sala de espera (el que más esperó).
     */
    public T desencolar() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola de espera de mazmorra está vacía.");
        }

        T elementoRetornado = this.primero.elemento;
        this.primero = this.primero.siguiente;

        // Si la cola quedo vacia, limpiamos la referencia al ultimo
        if (this.primero == null) {
            this.ultimo = null;
        }

        // Reducimos el tamaño
        this.tamanio--;
        return elementoRetornado;
    }

    /**
     * Inspecciona el elemento al frente de la cola sin removerlo.
     */
    public T frente() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola de espera esta vacia.");
        }
        return this.primero.elemento;
    }

    /**
     * Determina si la sala de espera esta vacia y devuelve
     * un boolean que indica su estado.
     */
    public boolean estaVacia() {
        return this.primero == null;
    }

    /**
     * Retorna la cantidad actual de elementos en espera.
     */
    public int getTamanio() {
        return this.tamanio;
    }
}