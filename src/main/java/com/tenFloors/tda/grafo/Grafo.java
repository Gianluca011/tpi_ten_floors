package main.java.com.tenFloors.tda.grafo;

// Importación de excepciones necesarias para el manejo de errores en el grafo
import java.util.NoSuchElementException;

/**
 * Implementacion nativa de un Grafo No Dirigido estructurado mediante Lista de Adyacencia.
 * Diseñado para representar el Mapa del Mundo (Vertices = Zonas/Pisos, Aristas = Portales).
 * @param <T> Tipo de elemento almacenado en los vértices (ej. Zona, String).
 */
public class Grafo<T> {

    /**
     * Nodo interno que representa un vértice del grafo (una Zona/Piso)
     * @param <T>
     */
    private static class NodoVertice<T> {
        private final T info; // Información almacenada en el vértice (ej. nombre de la zona)
        private NodoArista<T> listaAdyacencia; // Referencia a la lista de aristas (portales) que conectan este vértice con otros
        private NodoVertice<T> siguienteVertice; // Referencia al siguiente vértice en la lista de vértices del grafo
        private boolean visitado; // Flag auxiliar para los recorridos BFS y DFS

        /**
         * Constructor de la clase NodoVertice.
         * @param info Información del vértice (ej. nombre de la zona)
         */
        public NodoVertice(T info) {
            this.info = info;
            this.listaAdyacencia = null;
            this.siguienteVertice = null;
            this.visitado = false;
        }
    }

    /**
     * Nodo interno que representa una arista en la lista de adyacencia (un Portal de teletransporte)
     * @param <T> 
     */
    private static class NodoArista<T> {
        private final NodoVertice<T> destino; // Referencia al vértice de destino al que apunta esta arista
        private NodoArista<T> siguienteArista; // Referencia a la siguiente arista en la lista de adyacencia del vértice origen

        /**
         * Constructor de la clase NodoArista.
         * @param destino Referencia al vértice de destino al que apunta esta arista
         */
        public NodoArista(NodoVertice<T> destino) {
            this.destino = destino;
            this.siguienteArista = null;
        }
    }

    /**
     * Estructura de nodo para la cola auxiliar nativa requerida en el recorrido BFS
     * @param <T> 
     */
    private static class NodoColaAuxiliar<T> {
        private final NodoVertice<T> vertice; // Referencia al vértice que se encuentra en la cola auxiliar
        private NodoColaAuxiliar<T> siguiente; // Referencia al siguiente nodo en la cola auxiliar

        /**
         * Constructor de la clase NodoColaAuxiliar.
         * @param vertice Referencia al vértice que se encuentra en la cola auxiliar
         */
        public NodoColaAuxiliar(NodoVertice<T> vertice) {
            this.vertice = vertice;
            this.siguiente = null;
        }
    }

    private NodoVertice<T> primerVertice; // Referencia al primer vértice en la lista de vértices del grafo
    private int cantidadVertices; // Contador de la cantidad de vértices en el grafo

    /**
     * Constructor de la clase Grafo.
     */
    public Grafo() {
        this.primerVertice = null;
        this.cantidadVertices = 0;
    }

    /**
     * Añade un nuevo vértice (Zona/Piso) al mapa del mundo si no existe previamente.
     * @param info Información del vértice (ej. nombre de la zona)
     * @throws IllegalArgumentException si la información del vértice es nula
     */
    public void agregarVertice(T info) {
        // Validación de entrada para evitar vértices nulos
        if (info == null) {
            throw new IllegalArgumentException("El contenido del vértice no puede ser nulo.");
        }

        // Evitar duplicaciones de zonas en el mapa
        if (buscarNodoVertice(info) != null) {
            return;
        }

        // Crear un nuevo nodo de vértice y agregarlo al final de la lista de vértices
        NodoVertice<T> nuevoVertice = new NodoVertice<>(info);

        // Si la lista de vértices está vacía, el nuevo vértice se convierte en el primer vértice
        if (primerVertice == null) {
            primerVertice = nuevoVertice;
        } else {
            // Recorrer la lista de vértices hasta encontrar el último y agregar el nuevo vértice al final
            NodoVertice<T> actual = primerVertice;

            // Iterar hasta el último vértice
            while (actual.siguienteVertice != null) {
                actual = actual.siguienteVertice;
            }

            // Conectar el nuevo vértice al final de la lista
            actual.siguienteVertice = nuevoVertice;
        }

        // Incrementar el contador de vértices en el grafo
        this.cantidadVertices++;
    }

    /**
     * Añade una arista (Portal bidireccional) entre dos zonas del mapa del mundo.
     * Al ser un grafo no dirigido, la conexión se registra en ambas listas de adyacencia.
     * @param origen Vertice de origen
     * @param destino Vertice de destino
     * @throws IllegalArgumentException si los vértices de origen o destino son nulos
     * @throws NoSuchElementException si uno o ambos vértices no existen en el grafo
     */
    public void agregarArista(T origen, T destino) {
        // Validación de entrada para evitar vértices nulos
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Los vertices de origen y destino no pueden ser nulos.");
        }

        NodoVertice<T> nodoOrigen = buscarNodoVertice(origen); // Buscar el nodo de vértice correspondiente al origen
        NodoVertice<T> nodoDestino = buscarNodoVertice(destino); // Buscar el nodo de vértice correspondiente al destino

        // Validación para asegurar que ambos vértices existen en el grafo antes de crear la arista
        if (nodoOrigen == null || nodoDestino == null) {
            throw new NoSuchElementException("Uno o ambos vertices no existen en el mapa de la torre.");
        }

        // Registrar ida y vuelta de manera segura evitando portales duplicados
        conectarDireccional(nodoOrigen, nodoDestino);
        conectarDireccional(nodoDestino, nodoOrigen);
    }

    /**
     * Método auxiliar para conectar dos vértices de manera direccional, evitando duplicados.
     * @param desde Nodo de vértice de origen
     * @param hasta Nodo de vértice de destino
     */
    private void conectarDireccional(NodoVertice<T> desde, NodoVertice<T> hasta) {
        NodoArista<T> actual = desde.listaAdyacencia; // actualiza la referencia al primer nodo de la lista de adyacencia del vértice de origen

        // Iterar sobre la lista de adyacencia del vértice de origen para verificar si ya existe una arista hacia el destino
        while (actual != null) {
            if (actual.destino == hasta) {
                return; // El portal ya existe, salimos para evitar redundancia
            }

            // Avanzar al siguiente nodo de la lista de adyacencia
            actual = actual.siguienteArista;
        }

        NodoArista<T> nuevaArista = new NodoArista<>(hasta); // Crear un nuevo nodo de arista apuntando al vértice de destino
        nuevaArista.siguienteArista = desde.listaAdyacencia; // Insertar la nueva arista al inicio de la lista de adyacencia del vértice de origen
        desde.listaAdyacencia = nuevaArista; // Actualizar la referencia al primer nodo de la lista de adyacencia del vértice de origen
    }

    /**
     * Realiza un recorrido en anchura (BFS) desde un vértice de inicio, imprimiendo el orden de visita por consola.
     * @param inicio Vértice desde el cual iniciar el recorrido BFS
     * @throws IllegalArgumentException si el vértice de inicio es nulo
     * @throws NoSuchElementException si el vértice de inicio no existe en el grafo
     */
    public void bfs(T inicio) {
        // Validación de entrada para evitar vértices nulos
        if (inicio == null) {
            throw new IllegalArgumentException("El punto de inicio del recorrido BFS no puede ser nulo.");
        }

        // Buscar el nodo de vértice correspondiente al inicio del recorrido
        NodoVertice<T> nodoInicio = buscarNodoVertice(inicio);

        // Validación para asegurar que el vértice de inicio existe en el grafo antes de iniciar el recorrido
        if (nodoInicio == null) {
            throw new NoSuchElementException("El vértice de inicio no existe en el grafo.");
        }

        // Restablecer los flags de visitado en todos los vértices antes de iniciar el recorrido BFS
        resetearFlagsVisitado();

        // Inicialización de cola nativa local
        NodoColaAuxiliar<T> frente = null;
        NodoColaAuxiliar<T> fin = null;

        // Encolar nodo inicial
        frente = fin = new NodoColaAuxiliar<>(nodoInicio);

        // Marcar el nodo inicial como visitado para evitar ciclos
        nodoInicio.visitado = true;

        System.out.print("Recorrido BFS desde [" + inicio + "]: ");

        // Bucle principal del recorrido BFS
        while (frente != null) {
            // Desencolar
            NodoVertice<T> verticeActual = frente.vertice;

            // Avanzar el frente de la cola
            frente = frente.siguiente;

            // Si la cola queda vacía, actualizar el fin a null
            if (frente == null) {
                fin = null;
            }

            // Procesar el nodo por consola
            System.out.print(verticeActual.info + " -> ");

            // Iterar sobre los portales adyacentes
            NodoArista<T> aristaActual = verticeActual.listaAdyacencia; // Referencia al primer portal de la lista de adyacencia del vértice actual

            while (aristaActual != null) {
                NodoVertice<T> vecino = aristaActual.destino;

                // Si el vecino no ha sido visitado, marcarlo como visitado y encolarlo
                if (!vecino.visitado) {
                    vecino.visitado = true;
                    NodoColaAuxiliar<T> nuevoNodoCola = new NodoColaAuxiliar<>(vecino);
                    
                    // Encolar el vecino al final de la cola
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
     * Realiza un recorrido en profundidad (DFS) desde un vértice de inicio, imprimiendo el orden de visita por consola.
     * @param inicio Vértice desde el cual iniciar el recorrido DFS
     * @throws IllegalArgumentException si el vértice de inicio es nulo
     * @throws NoSuchElementException si el vértice de inicio no existe en el grafo
     */
    public void dfs(T inicio) {
        // Validación de entrada para evitar vértices nulos
        if (inicio == null) {
            throw new IllegalArgumentException("El punto de inicio del recorrido DFS no puede ser nulo.");
        }

        NodoVertice<T> nodoInicio = buscarNodoVertice(inicio); // Buscar el nodo de vértice correspondiente al inicio del recorrido
        if (nodoInicio == null) {
            throw new NoSuchElementException("El vértice de inicio no existe en el grafo.");
        }

        resetearFlagsVisitado(); // Restablecer los flags de visitado en todos los vértices antes de iniciar el recorrido DFS
        
        // Indicar el inicio del recorrido DFS por consola
        System.out.print("Recorrido DFS desde [" + inicio + "]: ");
        
        // Llamada recursiva al método auxiliar para realizar el recorrido DFS
        dfsRecursivo(nodoInicio);
        
        System.out.println("FIN");
    }

    /**
     * Método auxiliar recursivo para realizar el recorrido DFS desde un vértice dado.
     * @param actual
     */
    private void dfsRecursivo(NodoVertice<T> actual) {
        actual.visitado = true; // Marcar el vértice actual como visitado para evitar ciclos

        // Procesar el vértice actual por consola
        System.out.print(actual.info + " -> ");

        // Iterar sobre los portales adyacentes del vértice actual
        NodoArista<T> aristaActual = actual.listaAdyacencia;

        // Mientras haya portales adyacentes, continuar el recorrido DFS
        while (aristaActual != null) {
            NodoVertice<T> vecino = aristaActual.destino;

            // Si el vecino no ha sido visitado, realizar una llamada recursiva para continuar el recorrido DFS
            if (!vecino.visitado) {
                dfsRecursivo(vecino);
            }

            // Avanzar al siguiente portal adyacente
            aristaActual = aristaActual.siguienteArista;
        }
    }

    /**
     * Busca un nodo de vértice en la lista de vértices del grafo por su información.
     * @param info Información del vértice a buscar
     * @return El nodo de vértice encontrado, o null si no se encuentra
     */
    private NodoVertice<T> buscarNodoVertice(T info) {
        NodoVertice<T> actual = primerVertice; // Comenzar la búsqueda desde el primer vértice

        // Mientras haya vértices en la lista, continuar la búsqueda
        while (actual != null) {
            // Comparar la información del vértice actual con la información buscada
            if (actual.info.equals(info)) {
                return actual;
            }

            // Avanzar al siguiente vértice en la lista
            actual = actual.siguienteVertice;
        }
        return null; // Retornar null si no se encuentra el vértice con la información especificada
    }

    /**
     * Resetea los flags de visitado de todos los vértices del grafo, preparándolos para un nuevo recorrido BFS o DFS.
     */
    private void resetearFlagsVisitado() {
        NodoVertice<T> actual = primerVertice; // Comenzar desde el primer vértice del grafo

        // Recorrer todos los vértices del grafo y restablecer su flag de visitado a false
        while (actual != null) {
            actual.visitado = false; // Restablecer el flag de visitado del vértice actual
            actual = actual.siguienteVertice; // Avanzar al siguiente vértice en la lista
        }
    }

    /**
     * Devuelve la cantidad de vértices (Zonas/Pisos) actualmente en el grafo.
     * @return La cantidad de vértices en el grafo
     */
    public int getCantidadVertices() {
        return this.cantidadVertices;
    }
}