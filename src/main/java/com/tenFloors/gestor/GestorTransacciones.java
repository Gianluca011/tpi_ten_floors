package main.java.com.tenFloors.gestor;

import main.java.com.tenFloors.model.Transaccion;
import main.java.com.tenFloors.tda.arbolB.ArbolB;

/**
 * Sistema de gestión síncrono encargado de administrar el registro histórico masivo
 * de transacciones en la Casa de Subastas. Preparado para altos volúmenes de datos.
 */
public class GestorTransacciones {

    // Se utiliza un Árbol B como TDA de indexación masiva por su eficiencia O(log n)
    private final ArbolB<Transaccion> indiceTransacciones;
    private int contadorTransacciones;

    public GestorTransacciones() {
        // Inicializamos el Árbol B con un grado mínimo 't' alto (ej. 64)
        // ideal para optimizar la paginación y manejo de volúmenes masivos.
        this.indiceTransacciones = new ArbolB<>(64);
        this.contadorTransacciones = 0;
    }

    /**
     * Registra de forma histórica una transacción en el índice masivo.
     * * @param transaccion Objeto con los datos de la subasta completada.
     */
    public void registrarTransaccion(Transaccion transaccion) {
        if (transaccion == null) {
            throw new IllegalArgumentException("La transacción a registrar no puede ser nula.");
        }

        // Se indexa en el Árbol B usando el ID primitivo (long) como clave de ordenamiento
        indiceTransacciones.insertar(transaccion.getId(), transaccion);
        contadorTransacciones++;
    }

    /**
     * Busca de forma indexada y ultrarrápida una transacción por su ID único.
     * * @param id Identificador numérico de la transacción.
     * @return La transacción encontrada o null si no existe.
     */
    public Transaccion buscarTransaccion(long id) {
        if (indiceTransacciones.estaVacio()) {
            System.out.println("El registro histórico de la Casa de Subastas está vacío.");
            return null;
        }
        return indiceTransacciones.buscar(id);
    }

    /**
     * Devuelve la cantidad total de transacciones almacenadas históricamente.
     */
    public int getCantidadTransaccionesRegistradas() {
        return this.contadorTransacciones;
    }
}
