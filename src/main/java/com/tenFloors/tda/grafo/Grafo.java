package main.java.com.tenFloors.tda.grafo;


import java.util.NoSuchElementException;

/**
 * Implementacion nativa de un Grafo No Dirigido estructurado mediante Lista de Adyacencia.
 * Diseñado para representar el Mapa del Mundo de Ten Floors (Vertices = Zonas/Pisos, Aristas = Portales).
 * @param <T> Tipo de elemento almacenado en los vértices (ej. Zona, String).
 */
public class Grafo<T> {

    // Nodo interno que representa un vértice del grafo (una Zona/Piso)
    private static class NodoVertice<T> {
        private final T info;
        private NodoArista<T> listaAdyacencia;
        private NodoVertice<T> siguienteVertice;
        private boolean visitado; // Flag auxiliar para los recorridos BFS y DFS

        public NodoVertice(T info) {
            this.info = info;
            this.listaAdyacencia = null;
            this.siguienteVertice = null;
            this.visitado = false;
        }
    }

    // Nodo interno que representa una arista en la lista de adyacencia (un Portal de teletransporte)
    private static class NodoArista<T> {
        private final NodoVertice<T> destino;
        private NodoArista<T> siguienteArista;

        public NodoArista(NodoVertice<T> destino) {
            this.destino = destino;
            this.siguienteArista = null;
        }
    }

    // Estructura de nodo para la cola auxiliar nativa requerida en el recorrido BFS
    private static class NodoColaAuxiliar<T> {
        private final NodoVertice<T> vertice;
        private NodoColaAuxiliar<T> siguiente;

        public NodoColaAuxiliar(NodoVertice<T> vertice) {
            this.vertice = vertice;
            this.siguiente = null;
        }
    }

    private NodoVertice<T> primerVertice;
    private int cantidadVertices;

    public Grafo() {
        this.primerVertice = null;
        this.cantidadVertices = 0;
    }

    /**
     * Añade un nuevo vértice (Zona/Piso) al mapa del mundo si no existe previamente.
     */
    public void agregarVertice(T info) {
        if (info == null) {
            throw new IllegalArgumentException("El contenido del vértice no puede ser nulo.");
        }

        // Evitar duplicaciones de zonas en el mapa
        if (buscarNodoVertice(info) != null) {
            return;
        }

        NodoVertice<T> nuevoVertice = new NodoVertice<>(info);

        if (primerVertice == null) {
            primerVertice = nuevoVertice;
        } else {
            NodoVertice<T> actual = primerVertice;
            while (actual.siguienteVertice != null) {
                actual = actual.siguienteVertice;
            }
            actual.siguienteVertice = nuevoVertice;
        }
        this.cantidadVertices++;
    }

    /**
     * Añade una arista (Portal bidireccional) entre dos zonas del mapa del mundo.
     * Al ser un grafo no dirigido, la conexión se registra en ambas listas de adyacencia.
     */
    public void agregarArista(T origen, T destino) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Los vertices de origen y destino no pueden ser nulos.");
        }

        NodoVertice<T> nodoOrigen = buscarNodoVertice(origen);
        NodoVertice<T> nodoDestino = buscarNodoVertice(destino);

        if (nodoOrigen == null || nodoDestino == null) {
            throw new NoSuchElementException("Uno o ambos vertices no existen en el mapa de la torre.");
        }

        // Registrar ida y vuelta de manera segura evitando portales duplicados
        conectarDireccional(nodoOrigen, nodoDestino);
        conectarDireccional(nodoDestino, nodoOrigen);
    }

    /**
     * Helper privado para insertar un arco dirigido en la lista de adyacencia de un nodo.
     */
    private void conectarDireccional(NodoVertice<T> desde, NodoVertice<T> hasta) {
        NodoArista<T> actual = desde.listaAdyacencia;
        while (actual != null) {
            if (actual.destino == hasta) {
                return; // El portal ya existe, salimos para evitar redundancia
            }
            actual = actual.siguienteArista;
        }

        NodoArista<T> nuevaArista = new NodoArista<>(hasta);
        nuevaArista.siguienteArista = desde.listaAdyacencia;
        desde.listaAdyacencia = nuevaArista;
    }

    /**
     * Ejecuta el recorrido en anchura (BFS) imprimiendo el orden de visita por consola.
     */
    public void bfs(T inicio) {
        if (inicio == null) {
            throw new IllegalArgumentException("El punto de inicio del recorrido BFS no puede ser nulo.");
        }

        NodoVertice<T> nodoInicio = buscarNodoVertice(inicio);
        if (nodoInicio == null) {
            throw new NoSuchElementException("El vértice de inicio no existe en el grafo.");
        }

        resetearFlagsVisitado();

        // Inicialización de cola nativa local
        NodoColaAuxiliar<T> frente = null;
        NodoColaAuxiliar<T> fin = null;

        // Encolar nodo inicial
        frente = fin = new NodoColaAuxiliar<>(nodoInicio);
        nodoInicio.visitado = true;

        System.out.print("Recorrido BFS desde [" + inicio + "]: ");

        while (frente != null) {
            // Desencolar
            NodoVertice<T> verticeActual = frente.vertice;
            frente = frente.siguiente;
            if (frente == null) {
                fin = null;
            }

            // Procesar el nodo por consola
            System.out.print(verticeActual.info + " -> ");

            // Iterar sobre los portales adyacentes
            NodoArista<T> aristaActual = verticeActual.listaAdyacencia;
            while (aristaActual != null) {
                NodoVertice<T> vecino = aristaActual.destino;
                if (!vecino.visitado) {
                    vecino.visitado = true;
                    NodoColaAuxiliar<T> nuevoNodoCola = new NodoColaAuxiliar<>(vecino);
                    if (fin == null) {
                        frente = fin = nuevoNodoCola;
                    } else {
                        fin.siguiente = nuevoNodoCola;
                        fin = nuevoNodoCola;
                    }
                }
                aristaActual = aristaActual.siguienteArista;
            }
        }
        System.out.println("FIN");
    }

    /**
     * Ejecuta el recorrido en profundidad (DFS) imprimiendo el orden de visita por consola.
     */
    public void dfs(T inicio) {
        if (inicio == null) {
            throw new IllegalArgumentException("El punto de inicio del recorrido DFS no puede ser nulo.");
        }

        NodoVertice<T> nodoInicio = buscarNodoVertice(inicio);
        if (nodoInicio == null) {
            throw new NoSuchElementException("El vértice de inicio no existe en el grafo.");
        }

        resetearFlagsVisitado();
        System.out.print("Recorrido DFS desde [" + inicio + "]: ");
        dfsRecursivo(nodoInicio);
        System.out.println("FIN");
    }

    private void dfsRecursivo(NodoVertice<T> actual) {
        actual.visitado = true;
        System.out.print(actual.info + " -> ");

        NodoArista<T> aristaActual = actual.listaAdyacencia;
        while (aristaActual != null) {
            NodoVertice<T> vecino = aristaActual.destino;
            if (!vecino.visitado) {
                dfsRecursivo(vecino);
            }
            aristaActual = aristaActual.siguienteArista;
        }
    }

    /**
     * Helper para buscar un nodo de tipo vértice basado en el objeto de información provisto.
     */
    private NodoVertice<T> buscarNodoVertice(T info) {
        NodoVertice<T> actual = primerVertice;
        while (actual != null) {
            if (actual.info.equals(info)) {
                return actual;
            }
            actual = actual.siguienteVertice;
        }
        return null;
    }

    /**
     * Restablece el flag de visitado en todos los vértices antes de iniciar un nuevo recorrido.
     */
    private void resetearFlagsVisitado() {
        NodoVertice<T> actual = primerVertice;
        while (actual != null) {
            actual.visitado = false;
            actual = actual.siguienteVertice;
        }
    }

    public int getCantidadVertices() {
        return this.cantidadVertices;
    }
}