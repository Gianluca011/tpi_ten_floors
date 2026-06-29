package main.java.com.tenFloors.tda.pila;

/**
 * TDA Pila Genérica con comportamiento LIFO.
 */
public class Pila<T> {

    private Nodo<T> tope; // Referencia al nodo que representa el tope de la pila
    private int tamanio; // Contador de elementos en la pila


    /**
     * Clase interna que representa un nodo de la pila.
     * @param <T>
     */
    private static class Nodo<T> {
        final T dato; // Dato almacenado en el nodo, final para que no pueda ser modificado después de la creación
        Nodo<T> siguiente; // Referencia al siguiente nodo en la pila

        /**
         * Constructor de la clase Nodo.
         * @param dato
         */
        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    /**
     * Constructor de la clase Pila.
     */
    public Pila() {
        this.tope = null; // Inicializa la pila como vacía
        this.tamanio = 0; // Inicializa el tamaño de la pila en 0
    }

    /**
     * Inserta un elemento en el tope de la pila.
     * @param elemento El elemento a insertar en la pila.
     */
    public void apilar(T elemento) {
        // Crea un nuevo nodo con el elemento a insertar
        Nodo<T> nuevoNodo = new Nodo<>(elemento);

        nuevoNodo.siguiente = tope; // El nuevo nodo apunta al nodo que actualmente está en el tope
        tope = nuevoNodo; // Actualiza el tope de la pila para que sea el nuevo nodo
        tamanio++; // Incrementa el tamaño de la pila
    }

    /**
     * Remueve y devuelve el elemento en la tope de la pila.
     * @return El elemento removido.
     */
    public T desapilar() {
        // Verifica si la pila está vacía antes de intentar desapilar
        if (estaVacia()) {
            throw new IllegalStateException("[ERROR] La pila de navegacion esta vacia.");
        }

        T dato = tope.dato; // Guarda el dato del nodo que está en el tope
        tope = tope.siguiente; // Actualiza el tope de la pila para que sea el siguiente nodo
        tamanio--; // Decrementa el tamaño de la pila
        
        return dato;
    }

    /**
     * Devuelve el elemento en la tope sin removerlo.
     * @return El elemento en la tope.
     */
    public T verTope() {
        // Verifica si la pila está vacía antes de intentar ver el tope
        if (estaVacia()) {
            throw new IllegalStateException("[ERROR] No hay elementos en el tope.");
        }
        return tope.dato;
    }

    /**
     * Verifica si la pila no contiene elementos.
     * @return true si la pila está vacía, false en caso contrario.
     */
    public boolean estaVacia() {
        return tope == null;
    }

    /**
     * Devuelve la cantidad actual de elementos en la pila.
     * @return La cantidad de elementos en la pila.
     */
    public int getTamanio() {
        return tamanio;
    }
}