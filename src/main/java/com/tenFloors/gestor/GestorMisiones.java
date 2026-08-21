package main.java.com.tenFloors.gestor;

import main.java.com.tenFloors.model.Mision;
import main.java.com.tenFloors.tda.colaPrioridad.ColaPrioridad;

/**
 * Sistema de gestión síncrono encargado de administrar las misiones activas del servidor.
 */
public class GestorMisiones {

    private final ColaPrioridad<Mision> colaMisionesActivas;

    public GestorMisiones() {
        this.colaMisionesActivas = new ColaPrioridad<>();
    }

    /**
     * Registra una nueva misión en el pool global asignando automáticamente su prioridad
     * según las reglas del dominio (Jefes > Eventos > Estándar).
     */
    public void registrarMision(Mision mision) {
        if (mision == null) {
            throw new IllegalArgumentException("La misión a registrar no puede ser nula.");
        }

        // Se extrae el peso numérico del enum para alimentar la ordenación del TDA
        int prioridadAsignada = mision.getTipo().getNivelPrioridad();
        colaMisionesActivas.insertar(mision, prioridadAsignada);
    }

    /**
     * Extrae y despacha la misión más urgente que requiera la atención del servidor o jugadores.
     */
    public Mision procesarSiguienteMision() {
        if (colaMisionesActivas.estaVacio()) {
            System.out.println("No hay misiones activas pendientes en el registro.");
            return null;
        }
        return colaMisionesActivas.extraerMaximo();
    }

    /**
     * Muestra el estado actual del flujo de misiones.
     */
    public int getCantidadMisionesPendientes() {
        return colaMisionesActivas.getTamanio();
    }
}