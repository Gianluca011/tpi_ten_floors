package main.java.com.tenFloors.tda.arbolB;

/**
 * Implementación nativa y puramente manual de un Árbol B para la Casa de Subastas.
 * Indexa los elementos mediante una clave numérica primitiva (long) para evitar el uso
 * de interfaces nativas del lenguaje como java.lang.Comparable.
 *
 * @param <T> Tipo de objeto genérico a almacenar (ej. Transaccion).
 */
public class ArbolB<T> {

    /**
     * Nodo interno estructurado mediante arreglos estáticos nativos y paralelos.
     */
    public static class NodoB<T> {
        private int n;                 // Número actual de claves almacenadas
        private boolean esHoja;        // Verdadero si el nodo no tiene hijos
        private final long[] claves;   // Arreglo manual de claves numéricas de ordenamiento
        private final T[] datos;       // Arreglo manual de los objetos simulados asociados
        private final NodoB<T>[] hijos; // Arreglo manual de punteros a los nodos hijos

        @SuppressWarnings("unchecked")
        public NodoB(int t, boolean esHoja) {
            this.esHoja = esHoja;
            this.claves = new long[(2 * t) - 1];
            this.datos = (T[]) new Object[(2 * t) - 1];
            this.hijos = (NodoB<T>[]) new NodoB[2 * t];
            this.n = 0;
        }

        public int getN() {
            return this.n;
        }

        public boolean isEsHoja() {
            return this.esHoja;
        }

        public long[] getClaves() {
            return this.claves;
        }

        public T[] getDatos() {
            return this.datos;
        }

        public NodoB<T>[] getHijos() {
            return this.hijos;
        }
    }

    private NodoB<T> raiz;
    private final int t; // Grado mínimo

    /**
     * Constructor por defecto con grado mínimo t = 3.
     */
    public ArbolB() {
        this(3);
    }

    /**
     * Constructor con especificación de grado mínimo.
     */
    public ArbolB(int t) {
        if (t < 2) {
            throw new IllegalArgumentException("El grado mínimo 't' debe ser por lo menos 2.");
        }
        this.t = t;
        this.raiz = null;
    }

    /**
     * Inserta un nuevo objeto en el Árbol B usando su ID numérico manual como clave.
     *
     * @param clave Identificador numérico único de la transacción.
     * @param dato  Objeto con la información de la subasta.
     */
    public void insertar(long clave, T dato) {
        if (dato == null) {
            throw new IllegalArgumentException("No se puede insertar un dato nulo.");
        }

        // Caso base: El árbol está vacío
        if (estaVacio()) {
            this.raiz = new NodoB<>(this.t, true);
            this.raiz.claves[0] = clave;
            this.raiz.datos[0] = dato;
            this.raiz.n = 1;
            return;
        }

        // Caso crítico: La raíz está totalmente llena, se divide preventivamente
        if (this.raiz.n == (2 * this.t) - 1) {
            NodoB<T> nuevaRaiz = new NodoB<>(this.t, false);
            nuevaRaiz.hijos[0] = this.raiz;

            dividirHijo(nuevaRaiz, 0, this.raiz);

            this.raiz = nuevaRaiz;

            int i = 0;
            if (clave > nuevaRaiz.claves[0]) {
                i++;
            }
            insertarNoLleno(nuevaRaiz.hijos[i], clave, dato);
        } else {
            insertarNoLleno(this.raiz, clave, dato);
        }
    }

    private void insertarNoLleno(NodoB<T> x, long clave, T dato) {
        int i = x.n - 1;

        if (x.esHoja) {
            // Desplazamiento manual en arreglos paralelos
            while (i >= 0 && clave < x.claves[i]) {
                x.claves[i + 1] = x.claves[i];
                x.datos[i + 1] = x.datos[i];
                i--;
            }
            x.claves[i + 1] = clave;
            x.datos[i + 1] = dato;
            x.n++;
        } else {
            while (i >= 0 && clave < x.claves[i]) {
                i--;
            }
            i++;

            if (x.hijos[i].n == (2 * this.t) - 1) {
                dividirHijo(x, i, x.hijos[i]);

                if (clave > x.claves[i]) {
                    i++;
                }
            }
            insertarNoLleno(x.hijos[i], clave, dato);
        }
    }

    private void dividirHijo(NodoB<T> x, int i, NodoB<T> y) {
        NodoB<T> z = new NodoB<>(this.t, y.esHoja);
        z.n = this.t - 1;

        // Copiar claves y datos correspondientes al nuevo hermano z
        for (int j = 0; j < this.t - 1; j++) {
            z.claves[j] = y.claves[j + this.t];
            z.datos[j] = y.datos[j + this.t];
            y.claves[j + this.t] = 0;
            y.datos[j + this.t] = null;
        }

        if (!y.esHoja) {
            for (int j = 0; j < this.t; j++) {
                z.hijos[j] = y.hijos[j + this.t];
                y.hijos[j + this.t] = null;
            }
        }

        y.n = this.t - 1;

        for (int j = x.n; j >= i + 1; j--) {
            x.hijos[j + 1] = x.hijos[j];
        }
        x.hijos[i + 1] = z;

        for (int j = x.n - 1; j >= i; j--) {
            x.claves[j + 1] = x.claves[j];
            x.datos[j + 1] = x.datos[j];
        }

        x.claves[i] = y.claves[this.t - 1];
        x.datos[i] = y.datos[this.t - 1];
        y.claves[this.t - 1] = 0;
        y.datos[this.t - 1] = null;

        x.n++;
    }

    /**
     * Busca de forma indexada un objeto por su ID numérico.
     *
     * @param clave ID numérico a buscar en el índice.
     * @return El objeto asociado (T) o null si no se encuentra.
     */
    public T buscar(long clave) {
        if (estaVacio()) {
            return null;
        }
        return buscarRecursivo(this.raiz, clave);
    }

    private T buscarRecursivo(NodoB<T> x, long clave) {
        int i = 0;

        while (i < x.n && clave > x.claves[i]) {
            i++;
        }

        if (i < x.n && clave == x.claves[i]) {
            return x.datos[i];
        }

        if (x.esHoja) {
            return null;
        }

        return buscarRecursivo(x.hijos[i], clave);
    }

    /**
     * Imprime de forma jerárquica el árbol mostrando claves y datos en la consola interactiva.
     */
    public void mostrarHistorial() {
        if (estaVacio()) {
            System.out.println("[Historial de la Casa de Subastas Vacío]");
            return;
        }
        mostrarRecursivo(this.raiz, 0);
    }

    private void mostrarRecursivo(NodoB<T> nodo, int nivel) {
        String sangria = "    ".repeat(nivel);
        System.out.print(sangria + "└── [ Claves en Nodo (" + nodo.n + "): ");
        for (int i = 0; i < nodo.n; i++) {
            System.out.print(nodo.claves[i] + " (" + nodo.datos[i] + ")" + (i < nodo.n - 1 ? " | " : ""));
        }
        System.out.println(" ]");

        if (!nodo.esHoja) {
            for (int i = 0; i <= nodo.n; i++) {
                if (nodo.hijos[i] != null) {
                    mostrarRecursivo(nodo.hijos[i], nivel + 1);
                }
            }
        }
    }

    public NodoB<T> getRaiz() {
        return this.raiz;
    }

    public boolean estaVacio() {
        return this.raiz == null;
    }
}
