package main.java.com.tenFloors.model;

/**
 * Representa una misión dentro del MMORPG Ten Floors.
 */
public class Mision {

    // Enumerado para clasificar estrictamente el tipo de misión y mapear su prioridad
    public enum TipoMision {
        JEFE_MUNDO(3),
        EVENTO_TEMPORAL(2),
        RECOLECCION_ESTANDAR(1);

        private final int nivelPrioridad;

        TipoMision(int nivelPrioridad) {
            this.nivelPrioridad = nivelPrioridad;
        }

        public int getNivelPrioridad() {
            return this.nivelPrioridad;
        }
    }

    private final int id;
    private final String nombre;
    private final String descripcion;
    private final TipoMision tipo;
    private final int pisoRequerido;
    private final String idItemRecompensa;

    public Mision(int id, String nombre, String descripcion, TipoMision tipo, int pisoRequerido, String idItemRecompensa) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.pisoRequerido = pisoRequerido;
        this.idItemRecompensa = idItemRecompensa;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoMision getTipo() {
        return tipo;
    }

    public int getPisoRequerido() {
        return pisoRequerido;
    }

    public String getIdItemRecompensa() { return idItemRecompensa; }

    @Override
    public String toString() {
        return "Misión [ID=" + id + "] " + nombre + " (" + tipo + ") - Piso: " + pisoRequerido + " | Recompensa: " + idItemRecompensa;
    }
}
