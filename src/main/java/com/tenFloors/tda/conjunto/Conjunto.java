package main.java.com.tenFloors.tda.conjunto;

import main.java.com.tenFloors.tda.cola.Cola;

/**
 * Implementación nativa de un Conjunto (Set) basado en una Tabla Hash.
 * de los jugadores actualmente online en el servidor, evitando inicios de sesión duplicados.
 *
 * @param <T> Tipo de elemento a almacenar (ej. Jugador o String para IDs de cuenta).
 */
public class Conjunto<T> {

    /**
     * Clase interna que representa un nodo de la tabla hash
     * para manejar colisiones mediante encadenamiento (Linked List).
     * @param <T>
     */
    private static class Nodo<T> {
        private final T elemento; // Dato almacenado en el nodo, final para que no pueda ser modificado después de la creación
        private Nodo<T> siguiente; // Referencia al siguiente nodo en la lista enlazada del balde

        /**
         * Constructor de la clase Nodo.
         * @param elemento
         */
        public Nodo(T elemento) {
            this.elemento = elemento;
            this.siguiente = null;
        }
    }

    // Arreglo de nodos que representa la tabla hash, donde cada índice es un balde que puede contener una lista enlazada de nodos para manejar colisiones
    private Nodo<T>[] tabla;
    private int tamanio; // Contador de elementos en el conjunto

    // Configuración para el redimensionamiento dinámico de la tabla
    private static final int CAPACIDAD_INICIAL = 16; // Tamaño inicial de la tabla hash
    private static final double FACTOR_CARGA_MAXIMO = 0.75; // Factor de carga máximo antes de redimensionar la tabla hash

    /**
     * Constructor de la clase Conjunto.
     * Inicializa la tabla hash con la capacidad inicial y establece el tamaño en 0.
     */
    @SuppressWarnings("unchecked")
    public Conjunto() {
        this.tabla = (Nodo<T>[]) new Nodo[CAPACIDAD_INICIAL]; // Inicializa la tabla hash con la capacidad inicial
        this.tamanio = 0;
    }

    /**
     * Calcula el índice de la tabla aplicando la función Hash nativa del objeto
     * y mitigando valores de dispersión negativos.
     * @param elemento Elemento a calcular su índice en la tabla hash
     * @return Índice calculado para el elemento en la tabla hash
     */
    private int calcularIndice(T elemento) {
        // Si el elemento es nulo, se asigna al índice 0 para evitar excepciones
        if (elemento == null) {
            return 0;
        }
        // Se utiliza Math.abs para asegurar que el índice sea positivo
        // y se aplica el módulo con la longitud de la tabla para obtener un índice válido
        return Math.abs(elemento.hashCode()) % this.tabla.length;
    }

    /**
     * Agrega un elemento al conjunto.
     * Si el elemento ya existe, rechaza la inserción para evitar duplicados.
     *
     * @param elemento Objeto a registrar en el estado online.
     * @return true si el jugador se conectó exitosamente; false si ya estaba online.
     */
    public boolean agregar(T elemento) {
        // Validación de entrada: no se permiten elementos nulos en el conjunto
        if (elemento == null) {
            throw new IllegalArgumentException("No se pueden agregar elementos nulos al conjunto.");
        }

        // Evitar inicios de sesión duplicados
        if (contiene(elemento)) {
            return false;
        }

        // Si superamos el factor de carga, duplicamos el tamaño para mantener el acceso en O(1)
        if ((double) this.tamanio / this.tabla.length >= FACTOR_CARGA_MAXIMO) {
            rehash();
        }

        int indice = calcularIndice(elemento); // Calcula el índice del balde correspondiente para el elemento a insertar
        Nodo<T> nuevoNodo = new Nodo<>(elemento); // Crea un nuevo nodo con el elemento a insertar

        // Inserción al inicio de la lista enlazada del balde (Bucket) correspondiente
        if (this.tabla[indice] != null) {
            nuevoNodo.siguiente = this.tabla[indice];
        }
        this.tabla[indice] = nuevoNodo;
        this.tamanio++;

        return true;
    }

    /**
     * Verifica la existencia de un elemento en el conjunto (Equivalente a 'contains' / 'get').
     *
     * @param elemento Elemento a buscar.
     * @return true si el elemento está presente (Online), false de lo contrario.
     */
    public boolean contiene(T elemento) {
        // Validación de entrada: si el elemento es nulo, no puede estar presente en el conjunto
        if (elemento == null) {
            return false;
        }

        // Calcula el índice del balde correspondiente para el elemento a buscar
        int indice = calcularIndice(elemento);

        
        Nodo<T> actual = this.tabla[indice];

        // Mientras haya nodos en la lista enlazada, compara cada elemento con el buscado
        while (actual != null) {
            if (actual.elemento.equals(elemento)) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Remueve un elemento del conjunto (Equivalente a 'remove').
     * Utilizado cuando un jugador se desconecta del MMORPG de forma segura.
     *
     * @param elemento Elemento a remover.
     * @return true si el elemento fue encontrado y removido; false en caso contrario.
     */
    public boolean eliminar(T elemento) {
        if (elemento == null) {
            return false;
        }

        int indice = calcularIndice(elemento);
        Nodo<T> actual = this.tabla[indice];
        Nodo<T> anterior = null;

        while (actual != null) {
            if (actual.elemento.equals(elemento)) {
                if (anterior == null) {
                    // El elemento a eliminar era el primero del balde
                    this.tabla[indice] = actual.siguiente;
                } else {
                    // El elemento estaba en el medio o final de la cadena
                    anterior.siguiente = actual.siguiente;
                }
                this.tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Devuelve si el conjunto carece de elementos cargados.
     */
    public boolean estaVacio() {
        return this.tamanio == 0;
    }

    /**
     * Retorna la cantidad actual de cuentas mapeadas como online en el servidor.
     */
    public int getTamanio() {
        return this.tamanio;
    }

    /**
     * Reestructura internamente la tabla hash cuando se alcanza el límite del factor de carga.
     * Duplica el espacio disponible de baldes y redistribuye los nodos para mitigar colisiones.
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        Nodo<T>[] tablaVieja = this.tabla;
        // Duplicamos el tamaño de la capacidad de alojamiento
        this.tabla = (Nodo<T>[]) new Nodo[tablaVieja.length * 2];
        this.tamanio = 0;

        for (Nodo<T> nodoInicial : tablaVieja) {
            Nodo<T> actual = nodoInicial;
            while (actual != null) {
                // Reaprovechamos el metodo público de inserción bajo la nueva longitud de tabla
                agregar(actual.elemento);
                actual = actual.siguiente;
            }
        }
    }

    /**
     * Exporta todos los elementos del conjunto a una estructura lineal (Cola)
     * para permitir su recorrido y lectura externa.
     *
     * @return Una Cola con todos los elementos actuales del conjunto.
     */
    public Cola<T> obtenerElementos() {
        Cola<T> colaResultado = new Cola<>();

        // Recorremos cada uno de los baldes (buckets) de la tabla hash
        for (int i = 0; i < this.tabla.length; i++) {
            Nodo<T> actual = this.tabla[i];

            // Si el balde tiene colisiones o elementos, recorremos su lista enlazada
            while (actual != null) {
                colaResultado.encolar(actual.elemento);
                // NOTA: Si en la firma exacta de tu TDA Cola usaron 'enqueue',
                // reemplaza 'encolar' por 'enqueue'. Dado que en Main.java usan 'desencolar',
                // lo lógico y simétrico es que se llame 'encolar'.

                actual = actual.siguiente;
            }
        }

        return colaResultado;
    }
}