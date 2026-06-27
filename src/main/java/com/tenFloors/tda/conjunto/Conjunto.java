package main.java.com.tenFloors.tda.conjunto;

/**
 * Implementación nativa de un Conjunto (Set) basado en una Tabla Hash.
 * de los jugadores actualmente online en el servidor, evitando inicios de sesión duplicados.
 *
 * @param <T> Tipo de elemento a almacenar (ej. Jugador o String para IDs de cuenta).
 */
public class Conjunto<T> {
    // Nodo interno para el manejo de colisiones
    private static class Nodo<T> {
        private final T elemento;
        private Nodo<T> siguiente;

        public Nodo(T elemento) {
            this.elemento = elemento;
            this.siguiente = null;
        }
    }

    private Nodo<T>[] tabla;
    private int tamanio;

    // Configuración para el redimensionamiento dinámico de la tabla
    private static final int CAPACIDAD_INICIAL = 16;
    private static final double FACTOR_CARGA_MAXIMO = 0.75;

    @SuppressWarnings("unchecked")
    public Conjunto() {
        this.tabla = (Nodo<T>[]) new Nodo[CAPACIDAD_INICIAL];
        this.tamanio = 0;
    }

    /**
     * Calcula el índice de la tabla aplicando la función Hash nativa del objeto
     * y mitigando valores de dispersión negativos.
     */
    private int calcularIndice(T elemento) {
        if (elemento == null) {
            return 0;
        }
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

        int indice = calcularIndice(elemento);
        Nodo<T> nuevoNodo = new Nodo<>(elemento);

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
        if (elemento == null) {
            return false;
        }

        int indice = calcularIndice(elemento);
        Nodo<T> actual = this.tabla[indice];

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
}