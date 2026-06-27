package main.java.com.tenFloors.tda.arbol;

import java.util.NoSuchElementException;

/**
 * Implementación nativa de un Arbol Genérico N-ario basado en punteros "Primer Hijo / Siguiente Hermano".
 *
 * @param <T> Tipo de elemento almacenado (ej. String para nombres de habilidades o Clase de personaje).
 */
public class ArbolGenerico<T> {

    /**
     * Nodo interno que representa una habilidad o clase dentro de la jerarquía.
     */
    public static class NodoArbol<T> {
        private final T dato;
        private NodoArbol<T> primerHijo;       // Puntero al primer hijo directo (rama hacia abajo)
        private NodoArbol<T> siguienteHermano; // Puntero al hermano contiguo (rama lateral)

        public NodoArbol(T dato) {
            this.dato = dato;
            this.primerHijo = null;
            this.siguienteHermano = null;
        }

        public T getDato() {
            return this.dato;
        }

        public NodoArbol<T> getPrimerHijo() {
            return this.primerHijo;
        }

        public NodoArbol<T> getSiguienteHermano() {
            return this.siguienteHermano;
        }
    }

    private NodoArbol<T> raiz;

    /**
     * Constructor para un árbol inicialmente vacío.
     */
    public ArbolGenerico() {
        this.raiz = null;
    }

    /**
     * Constructor alternativo para inicializar el árbol directamente con un nodo raíz.
     */
    public ArbolGenerico(T datoRaiz) {
        if (datoRaiz == null) {
            throw new IllegalArgumentException("El dato de la raíz no puede ser nulo.");
        }
        this.raiz = new NodoArbol<>(datoRaiz);
    }

    /**
     * Inserta un nodo hijo bajo un nodo padre específico localizándolo de forma dinámica.
     * Si el árbol está vacío y el padre es null, se inicializa la raíz del árbol de habilidades.
     *
     * @param datoPadre El elemento identificador del padre.
     * @param datoHijo  La nueva habilidad o subclase a añadir.
     */
    public void agregarHijo(T datoPadre, T datoHijo) {
        if (datoHijo == null) {
            throw new IllegalArgumentException("El dato del hijo no puede ser nulo.");
        }

        // Caso base: El árbol está completamente vacío
        if (estaVacio()) {
            if (datoPadre != null) {
                throw new IllegalStateException("El árbol está vacío. Debe definir la raíz enviando 'null' como padre.");
            }
            this.raiz = new NodoArbol<>(datoHijo);
            return;
        }

        // Si el árbol ya tiene raíz, no se puede pasar un padre nulo
        if (datoPadre == null) {
            throw new IllegalArgumentException("El padre no puede ser nulo una vez que la raíz ya existe.");
        }

        // Buscar el nodo padre en toda la estructura jerárquica
        NodoArbol<T> nodoPadre = buscarNodo(this.raiz, datoPadre);
        if (nodoPadre == null) {
            throw new NoSuchElementException("No se encontró el nodo padre '" + datoPadre + "' en el árbol de habilidades.");
        }

        NodoArbol<T> nuevoHijo = new NodoArbol<>(datoHijo);

        // Si el padre no tiene ningún hijo todavía, se asigna como el primer hijo
        if (nodoPadre.primerHijo == null) {
            nodoPadre.primerHijo = nuevoHijo;
        } else {
            // Si ya tiene hijos, recorremos de forma lineal la lista de hermanos hasta el último
            NodoArbol<T> actual = nodoPadre.primerHijo;
            while (actual.siguienteHermano != null) {
                actual = actual.siguienteHermano;
            }
            actual.siguienteHermano = nuevoHijo;
        }
    }

    /**
     * Realiza un recorrido en profundidad PREORDEN (Raíz -> Hijos).
     * Ideal para mostrar la estructura jerárquica indentada de las clases en la consola interactiva.
     */
    public void preorden() {
        if (estaVacio()) {
            System.out.println("[Árbol de Habilidades Vacío]");
            return;
        }
        preordenRecursivo(this.raiz, 0);
    }

    private void preordenRecursivo(NodoArbol<T> nodo, int nivel) {
        if (nodo == null) {
            return;
        }

        // Procesar nodo actual (con formato visual de sangría para simular subniveles)
        imprimirFormatoConsola(nodo.dato, nivel);

        // Recorrer recursivamente todos los hijos de izquierda a derecha
        NodoArbol<T> hijoActual = nodo.primerHijo;
        while (hijoActual != null) {
            preordenRecursivo(hijoActual, nivel + 1);
            hijoActual = hijoActual.siguienteHermano; // Avanza por la lista de hermanos
        }
    }

    /**
     * Realiza un recorrido en profundidad POSTORDEN (Hijos -> Raíz).
     * Útil para operaciones de limpieza, cálculo de dependencias o Auditorías de Gremios.
     */
    public void postorden() {
        if (estaVacio()) {
            System.out.println("[Árbol de Habilidades Vacío]");
            return;
        }
        postordenRecursivo(this.raiz, 0);
    }

    private void postordenRecursivo(NodoArbol<T> nodo, int nivel) {
        if (nodo == null) {
            return;
        }

        // Primero procesar recursivamente todos los subárboles de los hijos
        NodoArbol<T> hijoActual = nodo.primerHijo;
        while (hijoActual != null) {
            postordenRecursivo(hijoActual, nivel + 1);
            hijoActual = hijoActual.siguienteHermano;
        }

        // Procesar el nodo actual al final
        imprimirFormatoConsola(nodo.dato, nivel);
    }

    /**
     * Busca de manera recursiva en profundidad un nodo que coincida con el dato proporcionado.
     */
    private NodoArbol<T> buscarNodo(NodoArbol<T> nodoActual, T datoBuscado) {
        if (nodoActual == null) {
            return null;
        }

        if (nodoActual.dato.equals(datoBuscado)) {
            return nodoActual;
        }

        // Buscar recursivamente en la cadena de hijos
        NodoArbol<T> hijoActual = nodoActual.primerHijo;
        while (hijoActual != null) {
            NodoArbol<T> nodoEncontrado = buscarNodo(hijoActual, datoBuscado);
            if (nodoEncontrado != null) {
                return nodoEncontrado;
            }
            hijoActual = hijoActual.siguienteHermano;
        }

        return null;
    }

    /**
     * Formatea la salida de consola utilizando tabulaciones modernas acordes al nivel de profundidad.
     */
    private void imprimirFormatoConsola(T dato, int nivel) {
        String sangria = "    ".repeat(nivel);
        System.out.println(sangria + "└── [ " + dato + " ]");
    }

    public NodoArbol<T> getRaiz() {
        return this.raiz;
    }

    public boolean estaVacio() {
        return this.raiz == null;
    }
}
